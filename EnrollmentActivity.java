package com.example.finalenroll;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EnrollmentActivity extends AppCompatActivity {
    private DatabaseReference database;
    private int totalCredits = 0;
    private final int maxCreditsPerSubject = 3; // Maksimal kredit per mata kuliah
    private final int maxTotalCredits = 24; // Batas maksimal total kredit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        database = FirebaseDatabase.getInstance().getReference("students");

        EditText subjectNameInput = findViewById(R.id.subjectNameInput);
        EditText subjectCreditsInput = findViewById(R.id.subjectCreditsInput);
        TextView subjectList = findViewById(R.id.subjectList);
        Button addSubjectButton = findViewById(R.id.addSubjectButton);
        Button summaryButton = findViewById(R.id.summaryButton);
        Button viewSummaryButton = findViewById(R.id.viewSummaryButton);
        Button hideSummaryButton = findViewById(R.id.hideSummaryButton);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        // Tambahkan mata kuliah
        addSubjectButton.setOnClickListener(v -> {
            String subjectName = subjectNameInput.getText().toString().trim();
            String creditsText = subjectCreditsInput.getText().toString().trim();

            // Validasi input
            if (TextUtils.isEmpty(subjectName) || TextUtils.isEmpty(creditsText)) {
                Toast.makeText(this, "Please enter both subject name and credits", Toast.LENGTH_SHORT).show();
                return;
            }

            int subjectCredits;
            try {
                subjectCredits = Integer.parseInt(creditsText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid credit value", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validasi kredit per mata kuliah
            if (subjectCredits > maxCreditsPerSubject) {
                Toast.makeText(this, "Cannot add more than 3 credits per subject", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validasi total kredit
            if (totalCredits + subjectCredits > maxTotalCredits) {
                Toast.makeText(this, "Cannot exceed 24 total credits", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tambahkan mata kuliah
            totalCredits += subjectCredits;
            subjectList.append("\nSubject Added: " + subjectName + " (" + subjectCredits + " credits)");
            addSubjectToDatabase(subjectName, subjectCredits);
            Toast.makeText(this, "Subject Added", Toast.LENGTH_SHORT).show();
            subjectNameInput.setText("");
            subjectCreditsInput.setText("");
        });

        // Lihat ringkasan lokal
        summaryButton.setOnClickListener(v -> {
            String message = "Enrollment Summary:\nTotal Credits: " + totalCredits;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });

        // Lihat ringkasan dari Firebase
        viewSummaryButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE); // Tampilkan progress bar
            subjectList.setVisibility(View.VISIBLE);
            hideSummaryButton.setVisibility(View.VISIBLE);

            String userId = "uid1"; // Ganti dengan user ID sebenarnya dari Firebase Auth
            DatabaseReference userRef = database.child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressBar.setVisibility(View.GONE); // Sembunyikan progress bar
                    StringBuilder summary = new StringBuilder("Firebase Enrollment Summary:\n");
                    int firebaseTotalCredits = 0;

                    for (DataSnapshot subjectSnapshot : snapshot.child("subjects").getChildren()) {
                        String subject = subjectSnapshot.child("subject").getValue(String.class);
                        Integer credits = subjectSnapshot.child("credits").getValue(Integer.class);

                        if (subject != null && credits != null) {
                            summary.append(subject).append(" (").append(credits).append(" credits)\n");
                            firebaseTotalCredits += credits;
                        }
                    }

                    summary.append("Total Credits: ").append(firebaseTotalCredits);
                    subjectList.setText(summary.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE); // Sembunyikan progress bar
                    Toast.makeText(EnrollmentActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Sembunyikan ringkasan
        hideSummaryButton.setOnClickListener(v -> {
            subjectList.setVisibility(View.GONE);
            hideSummaryButton.setVisibility(View.GONE);
        });
    }

    // Menambahkan mata kuliah ke Firebase
    private void addSubjectToDatabase(String subjectName, int subjectCredits) {
        String userId = "uid1"; // Ganti dengan user ID sebenarnya dari Firebase Auth
        DatabaseReference userRef = database.child(userId);
        Map<String, Object> subjectData = new HashMap<>();
        subjectData.put("subject", subjectName);
        subjectData.put("credits", subjectCredits);

        userRef.child("subjects").push().setValue(subjectData);
        userRef.child("totalCredits").setValue(totalCredits);
    }
}
