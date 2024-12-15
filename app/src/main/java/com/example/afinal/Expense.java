package com.example.afinal;

public class Expense {
    private String name;
    private double amount;
    private String category;

    // Constructor
    public Expense(String name, double amount, String category) {
        this.name = name;
        this.amount = amount;
        this.category = category;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }
}
