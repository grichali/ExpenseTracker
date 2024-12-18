package com.example.exspenses;

import android.annotation.SuppressLint;
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

    public long addBudget(Budget budget) {
        ContentValues values = new ContentValues();
        values.put("amount", budget.getAmount());
        values.put("startDate", budget.getStartDate());
        values.put("endDate", budget.getEndDate());

        return database.insert("budgets", null, values);
    }

    public List<Budget> getAllBudgets() {
        List<Budget> budgets = new ArrayList<>();
        Cursor cursor = database.query("budgets", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex("endDate"));

                budgets.add(new Budget(id, amount, startDate, endDate));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return budgets;
    }

    public Budget getLatestBudget() {
        Cursor cursor = database.query(
                "budgets",
                null,
                null,
                null,
                null,
                null,
                "endDate DESC",
                "1"
        );

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
            @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
            cursor.close();
            return new Budget(id, amount, startDate, endDate);
        }

        return null;
    }

    public int updateBudget(Budget budget) {
        ContentValues values = new ContentValues();
        values.put("amount", budget.getAmount());
        values.put("startDate", budget.getStartDate());
        values.put("endDate", budget.getEndDate());

        return database.update("budgets", values, "id = ?", new String[]{String.valueOf(budget.getId())});
    }

    public int removeBudget(int budgetId) {
        return database.delete("budgets", "id = ?", new String[]{String.valueOf(budgetId)});
    }

    public void close() {
        dbHelper.close();
    }
}
