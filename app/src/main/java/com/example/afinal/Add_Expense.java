package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Add_Expense extends AppCompatActivity {

    private EditText edtExpenseName, edtExpenseAmount;
    private Spinner spinnerCategory;
    private Button btnAddExpense, btnBack;
    private databaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense); // XML layout of your activity

        // Bind the views from the layout XML
        edtExpenseName = findViewById(R.id.edtExpenseName);
        edtExpenseAmount = findViewById(R.id.edtExpenseAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnBack = findViewById(R.id.btnBack);

        // Initialize the database helper
        dbHelper = new databaseHelper(this);

        // Open the database when the activity is created
        dbHelper.open();

        // Set up the Spinner with categories
        String[] categories = {"Food", "Entertainment", "Education", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Handle Add Expense button click event
        btnAddExpense.setOnClickListener(v -> {
            String expenseName = edtExpenseName.getText().toString().trim();
            String expenseAmountStr = edtExpenseAmount.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            // Check for empty fields
            if (expenseName.isEmpty() || expenseAmountStr.isEmpty() || category.isEmpty()) {
                Toast.makeText(Add_Expense.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double expenseAmount = Double.parseDouble(expenseAmountStr);
                // Add the expense to the database
                boolean isInserted = dbHelper.addExpense(expenseName, expenseAmount, category);
                if (isInserted) {
                    Toast.makeText(Add_Expense.this, "Expense added successfully", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newExpenseAmount", expenseAmount);  // Pass the amount back to MainActivity
                    setResult(RESULT_OK, resultIntent);
                    finish();  // Close Add_Expense activity
                } else {
                    Toast.makeText(Add_Expense.this, "Failed to add expense", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(Add_Expense.this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Back button click event
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();  // Close the database when the activity is destroyed
    }
}
