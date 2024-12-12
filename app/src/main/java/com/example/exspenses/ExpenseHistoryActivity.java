package com.example.exspenses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ExpenseHistoryActivity extends AppCompatActivity {
    private LinearLayout expensesContainer;
    private ExpenseRepository expenseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_history);

        expensesContainer = findViewById(R.id.expenses_container);
        expenseRepository = new ExpenseRepository(this);

        // Fetch all expenses from the database
        List<Expense> expenses = expenseRepository.getAllExpenses();
        displayExpenses(expenses);
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
                showEditDialog(expense); // Implement edit logic here
            });

            removeButton.setOnClickListener(v -> {
                confirmRemoveExpense(expense, expenseView); // Remove with confirmation
            });

            expensesContainer.addView(expenseView);
        }
    }

    private void confirmRemoveExpense(Expense expense, View expenseView) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Expense")
                .setMessage("Are you sure you want to remove this expense?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    expenseRepository.removeExpense(expense.getId());
                    expensesContainer.removeView(expenseView);
                    Toast.makeText(this, "Expense removed.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showEditDialog(Expense expense) {
        // Create the EditTexts for the name, category, and date
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
            // Update the expense object
            expense.setName(nameEditText.getText().toString());
            expense.setCategory(categoryEditText.getText().toString());
            expense.setDate(dateEditText.getText().toString());

            // Save the updated expense
            expenseRepository.updateExpense(expense);

            // Show a Toast with the updated data
            Toast.makeText(this, "Expense updated: " + expense.getName(), Toast.LENGTH_SHORT).show();

            // Refresh the expenses list and UI
            refreshExpensesUI();
        });

        // Handle the Cancel button click
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void refreshExpensesUI() {
        expensesContainer.removeAllViews();
        List<Expense> updatedExpenses = expenseRepository.getAllExpenses();  // Get updated data
        displayExpenses(updatedExpenses);  // Display the updated list
    }
}
