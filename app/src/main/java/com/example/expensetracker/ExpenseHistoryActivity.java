package com.example.expensetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ExpenseHistoryActivity extends AppCompatActivity {
    private LinearLayout expensesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_history);

        expensesContainer = findViewById(R.id.expenses_container);

        // Add dummy data
        List<Expense> expenses = getDummyExpenses();
        displayExpenses(expenses);
    }

    private List<Expense> getDummyExpenses() {
        // Replace this with actual data from a database or API
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1, "Lunch", "Food", "2024-12-01"));
        expenses.add(new Expense(2, "Bus Ticket", "Transport", "2024-12-02"));
        expenses.add(new Expense(3, "Netflix Subscription", "Entertainment", "2024-12-03"));
        return expenses;
    }

    private void displayExpenses(List<Expense> expenses) {
        for (Expense expense : expenses) {
            View expenseView = LayoutInflater.from(this).inflate(R.layout.item_expense, expensesContainer, false);

            TextView nameTextView = expenseView.findViewById(R.id.expense_name);
            TextView categoryTextView = expenseView.findViewById(R.id.expense_category);
            TextView dateTextView = expenseView.findViewById(R.id.expense_date);
            Button editButton = expenseView.findViewById(R.id.edit_button);
            Button removeButton = expenseView.findViewById(R.id.remove_button);

            nameTextView.setText(expense.getName());
            categoryTextView.setText(expense.getCategory());
            dateTextView.setText(expense.getDate());

            editButton.setOnClickListener(v -> {
                Toast.makeText(this, "Edit ID: " + expense.getId() + " - " + expense.getName(), Toast.LENGTH_SHORT).show();
                // Implement edit logic here
            });

            removeButton.setOnClickListener(v -> {
                Toast.makeText(this, "Remove ID: " + expense.getId() + " - " + expense.getName(), Toast.LENGTH_SHORT).show();
                expensesContainer.removeView(expenseView); // Remove item from UI
                // Implement additional remove logic if needed
            });

            expensesContainer.addView(expenseView);
        }
    }
}
