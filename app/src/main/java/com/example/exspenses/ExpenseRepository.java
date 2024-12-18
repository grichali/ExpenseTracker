package com.example.exspenses;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    private static final String TABLE_NAME = "expenses";
    private final SQLiteDatabase database;
    private final DBHelper dbHelper;

    public ExpenseRepository(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put("name", expense.getName());
        values.put("category", expense.getCategory());
        values.put("date", expense.getDate());
        values.put("amount", expense.getAmount());

        return database.insert(TABLE_NAME, null, values);
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nameColumnIndex = cursor.getColumnIndex("name");
                int categoryColumnIndex = cursor.getColumnIndex("category");
                int dateColumnIndex = cursor.getColumnIndex("date");
                int amountColumnIndex = cursor.getColumnIndex("amount");

                if (idColumnIndex != -1 && nameColumnIndex != -1 && categoryColumnIndex != -1 &&
                        dateColumnIndex != -1 && amountColumnIndex != -1) {

                    int id = cursor.getInt(idColumnIndex);
                    String name = cursor.getString(nameColumnIndex);
                    String category = cursor.getString(categoryColumnIndex);
                    String date = cursor.getString(dateColumnIndex);
                    double amount = cursor.getDouble(amountColumnIndex);

                    expenses.add(new Expense(id, name, category, date, amount));
                } else {
                    Log.e("ExpenseRepository", "One or more columns are missing from the database.");
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return expenses;
    }

    public int updateExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put("name", expense.getName());
        values.put("category", expense.getCategory());
        values.put("date", expense.getDate());
        values.put("amount", expense.getAmount());

        return database.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(expense.getId())});
    }

    public int removeExpense(int expenseId) {
        return database.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(expenseId)});
    }

    public double calculateTotalBalance() {
        double total = 0;
        Cursor cursor = database.rawQuery("SELECT SUM(amount) AS total FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            int totalColumnIndex = cursor.getColumnIndex("total");
            if (totalColumnIndex != -1) {
                total = cursor.getDouble(totalColumnIndex);
                Log.d("ExpenseRepository", "Calculated total: " + total);

            }
            cursor.close();
        }
        else {
            Log.e("ExpenseRepository", "Cursor is null or empty.");

        }

        return total;
    }

    public List<Expense> getExpensesByCategory(String category) {
        List<Expense> expenses = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, "category = ?", new String[]{category}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));

                expenses.add(new Expense(id, name, category, date, amount));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return expenses;
    }

    public void close() {
        dbHelper.close();
    }
}
