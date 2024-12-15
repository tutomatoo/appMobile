package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tìm các button trong layout
        Button btnAddExpense = findViewById(R.id.btnAddExpense);
        Button btnViewExpenses = findViewById(R.id.btnViewExpenses);
        Button btnDeleteExpense = findViewById(R.id.btnDeleteExpense);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnUpdateExpense = findViewById(R.id.btnUpdateExpense);
        Button btnTotalExpense = findViewById(R.id.btnTotalExpense);  // Nút Total Expense

        // Xử lý sự kiện khi nhấn nút "Add Expense"
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới activity Add_Expense
                Intent intent = new Intent(MainActivity.this, Add_Expense.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn nút "View Expenses"
        btnViewExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới activity View_Expense
                Intent intent = new Intent(MainActivity.this, View_Expense.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn nút "Delete Expense"
        btnDeleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới activity Delete_Expense
                Intent intent = new Intent(MainActivity.this, Delete_Expense.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn nút "Update Expense"
        btnUpdateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới activity Update_Expense
                Intent intent = new Intent(MainActivity.this, Update_Expense.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn nút "Total Expense"
        btnTotalExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới activity Total_Expense để hiển thị tổng số tiền chi tiêu
                Intent intent = new Intent(MainActivity.this, Total_Expense.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn nút "Logout"
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng màn hình hiện tại để đăng xuất
                finish();
                // Chuyển về màn hình login
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
