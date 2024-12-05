package com.example.expensetracker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExpenseHistoryActivity extends AppCompatActivity {
    private RecyclerView expenseRecyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_history);

        expenseRecyclerView = findViewById(R.id.expense_recycler_view);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize data (this should be replaced with actual data fetch logic)
        expenseList = getDummyExpenses();

        // Setup adapter
        expenseAdapter = new ExpenseAdapter(expenseList, this::onEditExpense, this::onRemoveExpense);
        expenseRecyclerView.setAdapter(expenseAdapter);
    }

    private List<Expense> getDummyExpenses() {
        // Replace this with actual data from a database or API
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Lunch", "Food", "2024-12-01"));
        expenses.add(new Expense("Bus Ticket", "Transport", "2024-12-02"));
        expenses.add(new Expense("Netflix Subscription", "Entertainment", "2024-12-03"));
        return expenses;
    }

    private void onEditExpense(Expense expense) {
        Toast.makeText(this, "Edit " + expense.getName(), Toast.LENGTH_SHORT).show();
        // Navigate to an edit screen or open a dialog
    }

    private void onRemoveExpense(Expense expense) {
        expenseList.remove(expense);
        expenseAdapter.notifyDataSetChanged();
        Toast.makeText(this, expense.getName() + " removed", Toast.LENGTH_SHORT).show();
    }
}
