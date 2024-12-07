package com.example.expensetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private LinearLayout budgetContainer;
    private FloatingActionButton addBudgetFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        budgetContainer = findViewById(R.id.budget_container);
        addBudgetFab = findViewById(R.id.add_budget_fab);

        // Add dummy data
        List<Budget> budgets = getDummyBudgets();
        displayBudgets(budgets);

        // Add new budget functionality
        addBudgetFab.setOnClickListener(v -> {
            // You can open a new activity or dialog for adding a budget.
            Toast.makeText(BudgetActivity.this, "Add new budget", Toast.LENGTH_SHORT).show();
            // Implement the Add Budget logic here
        });
    }

    private List<Budget> getDummyBudgets() {
        // Replace this with actual data from a database or API
        List<Budget> budgets = new ArrayList<>();
        budgets.add(new Budget(1, 500, "2024-12-01", "2024-12-07"));
        budgets.add(new Budget(2, 1000, "2024-12-08", "2024-12-14"));
        budgets.add(new Budget(3, 1200, "2024-12-15", "2024-12-21"));
        return budgets;
    }

    private void displayBudgets(List<Budget> budgets) {
        for (Budget budget : budgets) {
            View budgetView = LayoutInflater.from(this).inflate(R.layout.activity_item_budget, budgetContainer, false);

            TextView amountTextView = budgetView.findViewById(R.id.budget_amount);
            TextView dateRangeTextView = budgetView.findViewById(R.id.budget_date_range);
            Button editButton = budgetView.findViewById(R.id.edit_button);
            Button removeButton = budgetView.findViewById(R.id.remove_button);

            amountTextView.setText("$" + budget.getAmount());
            dateRangeTextView.setText(budget.getStartDate() + " to " + budget.getEndDate());

            // Edit Button click listener
            editButton.setOnClickListener(v -> {
                Toast.makeText(this, "Edit Budget ID: " + budget.getId(), Toast.LENGTH_SHORT).show();
                // Implement edit logic, open dialog to modify budget
            });

            // Remove Button click listener
            removeButton.setOnClickListener(v -> {
                Toast.makeText(this, "Remove Budget ID: " + budget.getId(), Toast.LENGTH_SHORT).show();
                budgetContainer.removeView(budgetView);
                // Implement remove logic, update data source
            });

            budgetContainer.addView(budgetView);
        }
    }
}
