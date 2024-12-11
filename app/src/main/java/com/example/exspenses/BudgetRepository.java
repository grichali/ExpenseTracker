package com.example.exspenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BudgetRepository {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public BudgetRepository(Context context) {
        dbHelper = new DBHelper(context); // DBHelper is a helper class for managing the SQLite database
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

    // Get a budget by its category
    public Budget getBudgetById(int id) {
        Cursor cursor = database.query("budgets", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int budgetId = cursor.getInt(cursor.getColumnIndex("id"));
            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
            String endDate = cursor.getString(cursor.getColumnIndex("endDate"));

            cursor.close();
            return new Budget(budgetId, amount, startDate, endDate);
        }

        return null;
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
