package com.example.finalenroll;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Mengatur teks pada TextView dengan resource string
        TextView aboutTitle = findViewById(R.id.aboutTitle);
        if (aboutTitle != null) {
            aboutTitle.setText(R.string.about_title);
        }

        TextView aboutContent = findViewById(R.id.aboutContent);
        if (aboutContent != null) {
            aboutContent.setText(R.string.about_content);
        }

        Button backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                finish(); // Tutup AboutActivity dan kembali ke aktivitas sebelumnya
            });
        }
    }
}

