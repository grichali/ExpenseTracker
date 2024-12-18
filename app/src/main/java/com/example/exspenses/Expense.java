package com.example.exspenses;

public class Expense {

    private int id;
    private String name;
    private String category;
    private String date;
    private double amount;

    public Expense(int id, String name, String category, String date, double amount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.date = date;
        this.amount = amount;
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

    public double getAmount() {
        return amount; // Getter for amount
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
