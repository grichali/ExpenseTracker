package com.example.exspenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BudgetRepository {
    private final SQLiteDatabase database;
    private final DBHelper dbHelper;

    public BudgetRepository(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Add a new budget
    public long addBudget(Budget budget) {
        ContentValues values = new ContentValues();
        values.put("amount", budget.getAmount());
        values.put("startDate", budget.getStartDate());
        values.put("endDate", budget.getEndDate());

        return database.insert("budgets", null, values);
    }

    // Get all budgets
    public List<Budget> getAllBudgets() {
        List<Budget> budgets = new ArrayList<>();
        Cursor cursor = database.query("budgets", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex("id");
            int amountColumnIndex = cursor.getColumnIndex("amount");
            int startDateColumnIndex = cursor.getColumnIndex("startDate");
            int endDateColumnIndex = cursor.getColumnIndex("endDate");

            if (idColumnIndex != -1 && amountColumnIndex != -1 && startDateColumnIndex != -1 && endDateColumnIndex != -1) {
                do {
                    int budgetId = cursor.getInt(idColumnIndex);
                    double amount = cursor.getDouble(amountColumnIndex);
                    String startDate = cursor.getString(startDateColumnIndex);
                    String endDate = cursor.getString(endDateColumnIndex);

                    Budget budget = new Budget(budgetId, amount, startDate, endDate);
                    budgets.add(budget);
                } while (cursor.moveToNext());
            } else {
                Log.e("DBHelper", "One or more columns are missing from the cursor.");
            }

            cursor.close();
        }

        return budgets;
    }

    // Update the budget
    public int updateBudget(Budget budget) {
        ContentValues values = new ContentValues();
        values.put("amount", budget.getAmount());
        values.put("startDate", budget.getStartDate());
        values.put("endDate", budget.getEndDate());

        return database.update("budgets", values, "id = ?", new String[]{String.valueOf(budget.getId())});
    }

    // Remove a budget
    public int removeBudget(int budgetId) {
        return database.delete("budgets", "id = ?", new String[]{String.valueOf(budgetId)});
    }

    // Close the database
    public void close() {
        dbHelper.close();
    }
}
