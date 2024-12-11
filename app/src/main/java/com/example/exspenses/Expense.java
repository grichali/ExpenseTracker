package com.example.exspenses;

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
}
