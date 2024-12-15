package com.example.afinal;



import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class View_Expense extends AppCompatActivity {

    private ListView listViewExpenses;
    private Button btnBack;
    private databaseHelper dbHelper;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_expensive); // Ensure this is the correct layout

        // Initialize views
        listViewExpenses = findViewById(R.id.listViewExpenses);
        btnBack = findViewById(R.id.btnBack);

        // Initialize the database helper and expense list
        dbHelper = new databaseHelper(this);
        expenseList = new ArrayList<>();

        // Load expenses from the database
        loadExpenses();

        // Set the adapter for the ListView
        expenseAdapter = new ExpenseAdapter(this, expenseList);
        listViewExpenses.setAdapter(expenseAdapter);

        // Back button click listener
        btnBack.setOnClickListener(v -> finish());

    }

    // Load expenses from the database and add them to the list
    private void loadExpenses() {
        dbHelper.open();
        Cursor cursor = dbHelper.getAllExpenses();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_EXPENSE_NAME));
                double amount = cursor.getDouble(cursor.getColumnIndex(databaseHelper.COLUMN_EXPENSE_AMOUNT));
                String category = cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_EXPENSE_CATEGORY));

                Expense expense = new Expense(name, amount, category);
                expenseList.add(expense);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Toast.makeText(this, "No expenses found", Toast.LENGTH_SHORT).show();
        }
        dbHelper.close();
    }
}

