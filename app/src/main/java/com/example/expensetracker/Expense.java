package com.example.expensetracker;

public class Expense {

    private int id;
    private String name;
    private String category;
    private String date;

    public Expense(int id, String name, String category, String date) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.date = date;
    }

    public int getId() {
        return id;
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
