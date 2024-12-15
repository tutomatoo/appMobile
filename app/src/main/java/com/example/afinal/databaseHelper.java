package com.example.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class databaseHelper {

    private static final String DATABASE_NAME = "userDB";
    private static final int DATABASE_VERSION = 3;

    // Table Users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Table Expenses
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_EXPENSE_ID = "expense_id";
    public static final String COLUMN_EXPENSE_NAME = "expense_name";
    public static final String COLUMN_EXPENSE_AMOUNT = "expense_amount";
    public static final String COLUMN_EXPENSE_CATEGORY = "expense_category";

    private SQLiteDatabase db;
    private final Context context;
    private DBHelper dbHelper;

    public databaseHelper(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    // Method to open the database
    public void open() throws SQLException {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
    }

    // Method to close the database
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // Register a new user with encrypted password
    public boolean registerUser(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false;  // Ensure username and password are not empty
        }

        String encryptedPassword = encryptPassword(password);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, encryptedPassword);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;  // Return true if registration is successful
    }

    // Check if login credentials are correct
    public boolean loginUser(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false;  // Ensure username and password are not empty
        }

        String encryptedPassword = encryptPassword(password);

        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, encryptedPassword}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;  // Login successful
        }

        if (cursor != null) {
            cursor.close();
        }

        return false;  // Login failed
    }

    // Method to encrypt the password
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("databaseHelper", "Password encryption failed", e);
            return password;  // Return the password itself if encryption fails
        }
    }

    // Add an expense to the database
    public boolean addExpense(String name, double amount, String category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_NAME, name);
        values.put(COLUMN_EXPENSE_AMOUNT, amount);
        values.put(COLUMN_EXPENSE_CATEGORY, category);

        long result = db.insert(TABLE_EXPENSES, null, values);
        return result != -1;  // Return true if insertion is successful
    }

    // Update an expense by its name
    public boolean updateExpenseByName(String expenseName, double newAmount, String newCategory) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_AMOUNT, newAmount);  // Update the amount
        values.put(COLUMN_EXPENSE_CATEGORY, newCategory);  // Update the category

        // Update the expense where the name matches
        int rowsAffected = db.update(TABLE_EXPENSES, values, COLUMN_EXPENSE_NAME + "=?", new String[]{expenseName});
        return rowsAffected > 0;  // Return true if at least one row was affected
    }

    // Retrieve all expenses from the database
    public Cursor getAllExpenses() {
        return db.query(TABLE_EXPENSES,
                new String[]{COLUMN_EXPENSE_NAME, COLUMN_EXPENSE_AMOUNT, COLUMN_EXPENSE_CATEGORY},
                null, null, null, null, null);
    }

    // Delete an expense by its name
    public boolean deleteExpenseByName(String expenseName) {
        int rowsAffected = db.delete(TABLE_EXPENSES, COLUMN_EXPENSE_NAME + "=?", new String[]{expenseName});
        return rowsAffected > 0;  // Return true if at least one row was affected
    }

    // Method to get the total spending from the expenses table
    public double getTotalSpending() {
        double total = 0.0;
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_EXPENSE_AMOUNT + ") FROM " + TABLE_EXPENSES, null);
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);  // Get the total sum of amounts
            cursor.close();
        }
        return total;
    }

    // Helper class to manage database creation and version management
    private static class DBHelper extends android.database.sqlite.SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create "users" table
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERNAME + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT" + ")";
            db.execSQL(CREATE_USERS_TABLE);

            // Create "expenses" table
            String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                    + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_EXPENSE_NAME + " TEXT,"
                    + COLUMN_EXPENSE_AMOUNT + " REAL,"
                    + COLUMN_EXPENSE_CATEGORY + " TEXT" + ")";
            db.execSQL(CREATE_EXPENSES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop existing tables if upgrading database version
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
            onCreate(db); // Recreate tables
        }
    }
}
