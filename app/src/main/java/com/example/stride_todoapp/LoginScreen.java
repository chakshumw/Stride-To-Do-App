package com.example.stride_todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity {
    private EditText etLSEmail, etLSPassword;
    private Button loginButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        etLSEmail = findViewById(R.id.etLSEmail);
        etLSPassword = findViewById(R.id.etLSPassword);
        loginButton = findViewById(R.id.login_button);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        TextView tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login() {
        String email = etLSEmail.getText().toString();
        String password = etLSPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String savedEmail = sharedPreferences.getString("email", null);
        String savedPassword = sharedPreferences.getString("password", null);

        if (email.equals(savedEmail) && password.equals(savedPassword)) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
