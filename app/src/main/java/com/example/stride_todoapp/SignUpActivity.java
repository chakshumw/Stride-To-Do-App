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

public class SignUpActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etConfirmPassword, etEmailAddress;
    private Button signupButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        signupButton = findViewById(R.id.signup_button);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        TextView tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signUp() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String email = etEmailAddress.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("email", email);
        editor.apply();

        Toast.makeText(this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
