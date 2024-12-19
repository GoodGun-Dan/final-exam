package com.example.finalenroll;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText emailField, passwordField;
    private Button authButton, toggleAuthModeButton, forgotPasswordButton;

    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi field dan tombol
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        authButton = findViewById(R.id.authButton);
        toggleAuthModeButton = findViewById(R.id.toggleAuthModeButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton); // Tambahkan inisialisasi untuk forgotPasswordButton

        // Set klik listener untuk tombol
        authButton.setOnClickListener(v -> handleAuthentication());
        toggleAuthModeButton.setOnClickListener(v -> toggleAuthMode());
        forgotPasswordButton.setOnClickListener(v -> handleForgotPassword()); // Menambahkan onClickListener untuk tombol forgotPassword
    }

    private void handleAuthentication() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isLoginMode) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    navigateToEnrollment();
                } else {
                    Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    toggleAuthMode();
                } else {
                    Toast.makeText(this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void toggleAuthMode() {
        isLoginMode = !isLoginMode;
        authButton.setText(isLoginMode ? "Login" : "Register");
        toggleAuthModeButton.setText(isLoginMode ? "Don't have an account? Register" : "Already have an account? Login");
        forgotPasswordButton.setVisibility(isLoginMode ? Button.VISIBLE : Button.GONE);
    }

    private void handleForgotPassword() {
        String email = emailField.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToEnrollment() {
        Intent intent = new Intent(AuthenticationActivity.this, EnrollmentActivity.class);
        startActivity(intent);
        finish();
    }
}
