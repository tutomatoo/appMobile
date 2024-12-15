package com.example.afinal;

import android.text.TextUtils;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Update_Expense extends AppCompatActivity {

    private EditText etExpenseName, etAmount;
    private Spinner expenseCategorySpinner;
    private Button btnSaveExpense, btnBack;
    private databaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_expenseve);  // Thay đổi layout cho phù hợp

        // Khởi tạo các view
        etExpenseName = findViewById(R.id.et_expense_name);
        etAmount = findViewById(R.id.et_amount);
        expenseCategorySpinner = findViewById(R.id.expense_categories);
        btnSaveExpense = findViewById(R.id.btn_save_expense);
        btnBack = findViewById(R.id.btn_back);

        dbHelper = new databaseHelper(this);
        dbHelper.open();

        // Lấy danh sách các danh mục chi phí (giả sử bạn đã có danh sách này)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseCategorySpinner.setAdapter(adapter);

        // Lưu chi phí mới hoặc cập nhật chi phí
        btnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expenseName = etExpenseName.getText().toString();
                double amount = Double.parseDouble(etAmount.getText().toString());
                String category = expenseCategorySpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(expenseName) || amount <= 0) {
                    Toast.makeText(Update_Expense.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isUpdated = dbHelper.updateExpenseByName(expenseName, amount, category);
                if (isUpdated) {
                    Toast.makeText(Update_Expense.this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Hoàn thành và quay lại
                } else {
                    Toast.makeText(Update_Expense.this, "Failed to update expense", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Quay lại Activity trước
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();  // Đóng cơ sở dữ liệu khi activity bị hủy
    }
}

