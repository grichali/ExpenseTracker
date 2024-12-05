package com.example.expensetracker;

public class Expense {
    private String name;
    private String category;
    private String date;

    public Expense(String name, String category, String date) {
        this.name = name;
        this.category = category;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }
}
