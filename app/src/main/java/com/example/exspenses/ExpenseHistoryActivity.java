package com.example.exspenses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
                showEditDialog(expense);                // Implement edit logic here
            });

            removeButton.setOnClickListener(v -> {
                Toast.makeText(this, "Remove ID: " + expense.getId() + " - " + expense.getName(), Toast.LENGTH_SHORT).show();
                expensesContainer.removeView(expenseView); // Remove item from UI
                // Implement additional remove logic if needed
            });

            expensesContainer.addView(expenseView);
        }
    }
    private void showEditDialog(Expense expense) {
        // Create an EditText for the name, category, and date
        final EditText nameEditText = new EditText(this);
        nameEditText.setText(expense.getName());

        final EditText categoryEditText = new EditText(this);
        categoryEditText.setText(expense.getCategory());

        final EditText dateEditText = new EditText(this);
        dateEditText.setText(expense.getDate());

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Expense");
        builder.setMessage("Edit the expense details");

        // Set the layout for the dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(nameEditText);
        layout.addView(categoryEditText);
        layout.addView(dateEditText);

        builder.setView(layout);

        // Handle the Save button click
        builder.setPositiveButton("Save", (dialog, which) -> {
            // Update the expense data
            expense.setName(nameEditText.getText().toString());
            expense.setCategory(categoryEditText.getText().toString());
            expense.setDate(dateEditText.getText().toString());

            // Show a Toast with the updated data
            Toast.makeText(this, "Updated Expense: " + expense.getName() + ", " + expense.getCategory() + ", " + expense.getDate(), Toast.LENGTH_SHORT).show();

            // Refresh the expenses list and UI (you can call displayExpenses or update a single view)
            refreshExpensesUI();
        });

        // Handle the Cancel button click
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void refreshExpensesUI() {
        expensesContainer.removeAllViews();
        List<Expense> updatedExpenses = getDummyExpenses();  // Get updated data
        displayExpenses(updatedExpenses);  // Display the updated list
    }
}
