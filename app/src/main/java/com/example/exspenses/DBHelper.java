package com.example.exspenses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense_tracker.db";
    private static final int DATABASE_VERSION = 1;

    // Expense table creation
    private static final String CREATE_EXPENSE_TABLE = "CREATE TABLE expenses (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "category TEXT," +
            "date TEXT" +
            ");";

    // Budget table creation
    private static final String CREATE_BUDGET_TABLE = "CREATE TABLE budgets (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "amount REAL," +
            "startDate TEXT," +
            "endDate TEXT" +
            ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EXPENSE_TABLE);
        db.execSQL(CREATE_BUDGET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        db.execSQL("DROP TABLE IF EXISTS budgets");
        onCreate(db);
    }
}
