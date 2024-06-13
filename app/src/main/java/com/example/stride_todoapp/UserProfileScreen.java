package com.example.stride_todoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserProfileScreen extends AppCompatActivity {
    private TextView tvUIUsernameValue, tvUINameValue, tvUIEmailValue;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageButton backArrow = findViewById(R.id.userinfo_drawer_toggle);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the activity and go back to the previous screen
            }
        });

        tvUIUsernameValue = findViewById(R.id.tvUIUsernameValue);
        tvUINameValue = findViewById(R.id.tvUINameValue);
        tvUIEmailValue = findViewById(R.id.tvUIEmailValue);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loadUserInfo();

        Button editButton = findViewById(R.id.UIEditButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditUserInfoDialog();
            }
        });

        Button signOutButton = findViewById(R.id.UISignOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutConfirmationDialog();
            }
        });
    }

    private void loadUserInfo() {
        String username = sharedPreferences.getString("username", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String name = sharedPreferences.getString("name", "N/A");

        tvUIUsernameValue.setText(username);
        tvUINameValue.setText(name);
        tvUIEmailValue.setText(email);
    }

    private void showEditUserInfoDialog() {
        final Dialog dialog = new Dialog(UserProfileScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_user_info);

        final EditText etEditUsername = dialog.findViewById(R.id.etEditUsername);
        final EditText etEditName = dialog.findViewById(R.id.etEditName);
        final EditText etEditEmail = dialog.findViewById(R.id.etEditEmail);
        Button btnSaveUserInfo = dialog.findViewById(R.id.btnSaveUserInfo);

        etEditUsername.setText(sharedPreferences.getString("username", ""));
        etEditName.setText(sharedPreferences.getString("name", ""));
        etEditEmail.setText(sharedPreferences.getString("email", ""));

        btnSaveUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = etEditUsername.getText().toString();
                String newName = etEditName.getText().toString();
                String newEmail = etEditEmail.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", newUsername);
                editor.putString("name", newName);
                editor.putString("email", newEmail);
                editor.apply();

                loadUserInfo(); // Refresh the displayed user information
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showSignOutConfirmationDialog() {
        new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void signOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Intent to redirect to the login screen
        Intent intent = new Intent(UserProfileScreen.this, LoginScreen.class);
        startActivity(intent);
        finish(); // Finish the current activity
    }
}
