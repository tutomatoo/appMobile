package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button registerButton;
    private TextView loginRedirect;
    private databaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Ánh xạ view từ XML
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        registerButton = findViewById(R.id.registerButton);
        loginRedirect = findViewById(R.id.loginRedirect);

        // Khởi tạo databaseHelper
        dbHelper = new databaseHelper(this);
        dbHelper.open();

        // Xử lý nút đăng ký
        registerButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(Register.this, "Enter full field!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(Register.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isRegistered = dbHelper.registerUser(username, password);
            if (isRegistered) {
                Toast.makeText(Register.this, "Register Sucess!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register.this, Login.class); // Quay lại màn hình đăng nhập
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Register.this, "Failed Acount Exited", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý chuyển sang màn hình đăng nhập
        loginRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class); // Quay lại màn hình đăng nhập
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
