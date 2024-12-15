package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private databaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        dbHelper = new databaseHelper(this);

        // Bind view components
        EditText edtUsername = findViewById(R.id.edtUsername);
        EditText edtPassword = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        // Set up Login button click listener
        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // Check if username and password are empty
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.open();  // Open the database
            boolean isUserValid = dbHelper.loginUser(username, password);

            if (isUserValid) {
                // If login is successful, open MainActivity
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();  // Close the login activity
            } else {
                // Show error message if login fails
                Toast.makeText(Login.this, "Login failed. Invalid username or password.", Toast.LENGTH_SHORT).show();
            }
            dbHelper.close();  // Close the database after use
        });

        // Set up Register link click listener
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
    }
}
