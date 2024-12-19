package com.example.finalenroll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Menetapkan layout

        // Inisialisasi TextView untuk menampilkan 'Welcome'
        TextView welcomeText = findViewById(R.id.welcomeText);
        if (welcomeText != null) {
            welcomeText.setText(R.string.welcome_message); // Menggunakan string resource untuk pesan selamat datang
        }

        // Inisialisasi Button untuk mulai login atau registrasi
        Button startButton = findViewById(R.id.startButton);
        if (startButton != null) {
            startButton.setOnClickListener(v -> {
                // Intent untuk menuju ke halaman AuthenticationActivity
                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                // Tambahkan animasi transisi
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }

        // Inisialisasi Button untuk melihat informasi tentang aplikasi
        Button aboutButton = findViewById(R.id.aboutButton);
        if (aboutButton != null) {
            aboutButton.setOnClickListener(v -> {
                // Intent untuk menuju ke halaman AboutActivity
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                // Tambahkan animasi transisi
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            });
        }
    }
}
