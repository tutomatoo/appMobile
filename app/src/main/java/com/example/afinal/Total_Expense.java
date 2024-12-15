package com.example.afinal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Total_Expense extends AppCompatActivity {

    private SQLiteDatabase database;
    private ListView listViewExpenses;
    private TextView totalAmountText;
    private Button btnBack;
    private ArrayList<String> expenseList;
    private double totalAmount = 0.0;
    private databaseHelper dbHelper;  // Declare the databaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_expense);

        // Initialize views
        listViewExpenses = findViewById(R.id.listViewExpenses);
        totalAmountText = findViewById(R.id.totalAmountText);
        btnBack = findViewById(R.id.btnBack);

        // Initialize the databaseHelper
        dbHelper = new databaseHelper(this);

        // Open the database
        dbHelper.open();  // Open the database

        // Initialize the expense list
        expenseList = new ArrayList<>();
        loadExpensesFromDatabase();  // Load expenses

        // Calculate the total amount
        calculateTotalAmount();

        // Update the total amount TextView
        totalAmountText.setText("Total Amount: $" + totalAmount);

        // Set up the ArrayAdapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewExpenses.setAdapter(adapter);

        // Set up the Back button to return to the main activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the current activity
            }
        });
    }

    // Method to load expenses from the database
    private void loadExpensesFromDatabase() {
        Cursor cursor = dbHelper.getAllExpenses();  // Use the method from databaseHelper

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("expense_name"));
                double amount = cursor.getDouble(cursor.getColumnIndex("expense_amount"));
                expenseList.add(name + " - $" + amount);
            }
            cursor.close();
        }
    }

    // Method to calculate the total amount spent
    private void calculateTotalAmount() {
        // Use the method from databaseHelper to get the total spending
        totalAmount = dbHelper.getTotalSpending();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure the database is closed when the activity is destroyed
        dbHelper.close();
    }
}

