package com.example.exspenses;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private LinearLayout budgetContainer;
    private FloatingActionButton addBudgetFab;
    private BudgetRepository budgetRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        budgetContainer = findViewById(R.id.budget_container);
        addBudgetFab = findViewById(R.id.add_budget_fab);

        // Initialize the repository
        budgetRepository = new BudgetRepository(this);

        // Fetch all budgets from the database and display them
        List<Budget> budgets = getBudgetsFromDatabase();
        displayBudgets(budgets);

        // Add new budget functionality
        addBudgetFab.setOnClickListener(v -> showAddBudgetDialog());

        // Initialize the calendar for date picker
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Null check before finding views
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText startDateEditText = findViewById(R.id.edit_start_date);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText endDateEditText = findViewById(R.id.edit_end_date);

        // Additional null checks
        if (startDateEditText != null) {
            startDateEditText.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                            // Format the date in yyyy-MM-dd format
                            startDateEditText.setText(String.format("%d-%02d-%02d",
                                    selectedYear, selectedMonth + 1, selectedDayOfMonth));
                        }, year, month, day);
                datePickerDialog.show();
            });
        }

        if (endDateEditText != null) {
            endDateEditText.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                            // Format the date in yyyy-MM-dd format
                            endDateEditText.setText(String.format("%d-%02d-%02d",
                                    selectedYear, selectedMonth + 1, selectedDayOfMonth));
                        }, year, month, day);
                datePickerDialog.show();
            });
        }
    }

    // Fetch all budgets from the database
    private List<Budget> getBudgetsFromDatabase() {
        return budgetRepository.getAllBudgets();  // Get all budgets from the database
    }

    // Display the list of budgets
// Display the list of budgets
    private void displayBudgets(List<Budget> budgets) {
        budgetContainer.removeAllViews();  // Clear the current list
        for (Budget budget : budgets) {
            View budgetView = LayoutInflater.from(this).inflate(R.layout.activity_item_budget, budgetContainer, false);

            // Bind the views - use TextView
            TextView amountTextView = budgetView.findViewById(R.id.budget_amount);
            TextView dateRangeTextView = budgetView.findViewById(R.id.budget_date_range);
            Button editButton = budgetView.findViewById(R.id.edit_button);
            Button removeButton = budgetView.findViewById(R.id.remove_button);

            // Set the initial values
            amountTextView.setText(String.format("$%.2f", budget.getAmount()));
            dateRangeTextView.setText(budget.getStartDate() + " to " + budget.getEndDate());

            // Edit Button click listener
            editButton.setOnClickListener(v -> showEditBudgetDialog(budget));

            // Remove Button click listener
            removeButton.setOnClickListener(v -> {
                removeBudget(budget);
                budgetContainer.removeView(budgetView);  // Remove the budget view from the UI
            });

            budgetContainer.addView(budgetView);
        }
    }
    // Show the dialog to add a new budget
    private void showAddBudgetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Budget");

        // Inflate the layout for the dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_budget, null);
        builder.setView(dialogView);

        EditText amountEditText = dialogView.findViewById(R.id.edit_amount);
        EditText startDateEditText = dialogView.findViewById(R.id.edit_start_date);
        EditText endDateEditText = dialogView.findViewById(R.id.edit_end_date);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String amountText = amountEditText.getText().toString();
            if (amountText.isEmpty()) {
                Toast.makeText(BudgetActivity.this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                String startDate = startDateEditText.getText().toString();
                String endDate = endDateEditText.getText().toString();

                // Create a new budget and add it to the database
                Budget newBudget = new Budget(0, amount, startDate, endDate);
                long newBudgetId = budgetRepository.addBudget(newBudget);
                if (newBudgetId != -1) {
                    Toast.makeText(BudgetActivity.this, "Budget Added", Toast.LENGTH_SHORT).show();
                    displayBudgets(getBudgetsFromDatabase());  // Refresh the list
                } else {
                    Toast.makeText(BudgetActivity.this, "Error Adding Budget", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(BudgetActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });


        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    // Show the dialog to edit an existing budget
    private void showEditBudgetDialog(Budget budget) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Budget");

        // Inflate the layout for the dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_budget, null);
        builder.setView(dialogView);

        EditText amountEditText = dialogView.findViewById(R.id.edit_amount);
        EditText startDateEditText = dialogView.findViewById(R.id.edit_start_date);
        EditText endDateEditText = dialogView.findViewById(R.id.edit_end_date);

        // Pre-fill the existing values
        amountEditText.setText(String.valueOf(budget.getAmount()));
        startDateEditText.setText(budget.getStartDate());
        endDateEditText.setText(budget.getEndDate());

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Get the updated values from the input fields
            double updatedAmount = Double.parseDouble(amountEditText.getText().toString());
            String updatedStartDate = startDateEditText.getText().toString();
            String updatedEndDate = endDateEditText.getText().toString();

            // Update the budget in the database
            budget.setAmount(updatedAmount);
            budget.setStartDate(updatedStartDate);
            budget.setEndDate(updatedEndDate);
            int rowsUpdated = budgetRepository.updateBudget(budget);

            if (rowsUpdated > 0) {
                Toast.makeText(BudgetActivity.this, "Budget Updated", Toast.LENGTH_SHORT).show();
                displayBudgets(getBudgetsFromDatabase());  // Refresh the list
            } else {
                Toast.makeText(BudgetActivity.this, "Error Updating Budget", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    // Remove a budget from the database
    private void removeBudget(Budget budget) {
        int rowsDeleted = budgetRepository.removeBudget(budget.getId());
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Budget Removed", Toast.LENGTH_SHORT).show();
            displayBudgets(getBudgetsFromDatabase());  // Refresh the list
        } else {
            Toast.makeText(this, "Error Removing Budget", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        budgetRepository.close();
    }
}
