package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public ExpenseRepository(Context context) {
        dbHelper = new DBHelper(context); // DBHelper is a helper class for managing the SQLite database
        database = dbHelper.getWritableDatabase();
    }

    // Add a new expense
    public long addExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put("name", expense.getName());
        values.put("category", expense.getCategory());
        values.put("date", expense.getDate());

        return database.insert("expenses", null, values);
    }

    // Get all expenses
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        Cursor cursor = database.query("expenses", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                expenses.add(new Expense(id, name, category, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return expenses;
    }

    // Update an expense
    public int updateExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put("name", expense.getName());
        values.put("category", expense.getCategory());
        values.put("date", expense.getDate());

        return database.update("expenses", values, "id = ?", new String[]{String.valueOf(expense.getId())});
    }

    // Remove an expense
    public int removeExpense(int expenseId) {
        return database.delete("expenses", "id = ?", new String[]{String.valueOf(expenseId)});
    }

    // Close the database
    public void close() {
        dbHelper.close();
    }
}
