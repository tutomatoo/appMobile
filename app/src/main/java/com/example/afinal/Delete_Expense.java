package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Delete_Expense extends AppCompatActivity {

    private EditText edtExpenseName;
    private Button btnDeleteExpense, btnBackToMain;
    private databaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_expense); // The XML layout file

        // Bind the views from the layout XML
        edtExpenseName = findViewById(R.id.edtExpenseName);
        btnDeleteExpense = findViewById(R.id.btnDeleteExpense);
        btnBackToMain = findViewById(R.id.btnBackToMain);  // Back button

        // Initialize the database helper
        dbHelper = new databaseHelper(this);

        // Open the database when the activity is created
        dbHelper.open();

        // Handle Delete Expense button click event
        btnDeleteExpense.setOnClickListener(v -> {
            String expenseName = edtExpenseName.getText().toString().trim();

            // Check if the expense name is empty
            if (expenseName.isEmpty()) {
                Toast.makeText(Delete_Expense.this, "Please enter an expense name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Try to delete the expense from the database
            boolean isDeleted = dbHelper.deleteExpenseByName(expenseName);
            if (isDeleted) {
                Toast.makeText(Delete_Expense.this, "Expense deleted successfully", Toast.LENGTH_SHORT).show();
                edtExpenseName.setText("");  // Clear the expense name field
            } else {
                Toast.makeText(Delete_Expense.this, "Expense not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle "Back to Main Screen" button click event
        btnBackToMain.setOnClickListener(v -> {
            // Navigate back to MainActivity
            Intent intent = new Intent(Delete_Expense.this, MainActivity.class);
            startActivity(intent);
            finish();  // Optionally call finish to close this activity
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();  // Close the database when the activity is destroyed
    }
}
