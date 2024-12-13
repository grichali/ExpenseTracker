package com.example.exspenses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    private BarChart barChart;
    private PieChart pieChart;
    private MaterialButton addExpenseFab;
    private TextView totalBalanceText;
    private BudgetRepository budgetRepository;

    private ExpenseRepository expenseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        budgetRepository = new BudgetRepository(this);
        expenseRepository = new ExpenseRepository(this);
        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);
        totalBalanceText = findViewById(R.id.total_balance_text);
        addExpenseFab = findViewById(R.id.add_expense_fab);

        addExpenseFab.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
        Button viewHistoryButton = findViewById(R.id.view_history_fab);

        viewHistoryButton.setOnClickListener(v -> {
            // Create an Intent to open ExpenseHistoryActivity
            Intent intent = new Intent(DashboardActivity.this, ExpenseHistoryActivity.class);
            startActivity(intent);
        });
        setDynamicData();

        Button viewBudgetButton = findViewById(R.id.view_budget_fab);

        // Set the OnClickListener to navigate to BudgetActivity
        viewBudgetButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, BudgetActivity.class);
            startActivity(intent);
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if data needs to be refreshed
        if (getIntent().getBooleanExtra("refresh_dashboard", false)) {
            setDynamicData();  // Refresh the data
        }
    }

    private void setDynamicData() {
        // Fetch budget data from the database
        Budget budget = budgetRepository.getLatestBudget();
        if (budget != null) {
            String budgetInfo = "Budget: $" + budget.getAmount() + " (" + budget.getStartDate() + " to " + budget.getEndDate() + ")";
            TextView budgetTextView = findViewById(R.id.budget_text_view);
            budgetTextView.setText(budgetInfo);
        } else {
            // Handle case where no budget exists
            TextView budgetTextView = findViewById(R.id.budget_text_view);
            budgetTextView.setText("No budget available");
        }

        // Fetch expenses data from the database
        List<Expense> expenses = expenseRepository.getAllExpenses();
        if (expenses != null && !expenses.isEmpty()) {
            // Process data for bar chart (group expenses by month)
            Map<String, Float> monthlyExpenses = new HashMap<>();
            for (Expense expense : expenses) {
                String month = getMonthFromDate(expense.getDate()); // Helper method to extract the month
                monthlyExpenses.put(month, monthlyExpenses.getOrDefault(month, 0f) + (float) expense.getAmount() );
            }
            Log.d("DashboardActivity", "Monthly Expenses: " + monthlyExpenses);

            List<BarEntry> barEntries = new ArrayList<>();
            String[] months = {
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"

            };
            for (int i = 0; i < months.length; i++) {
                barEntries.add(new BarEntry(i, monthlyExpenses.getOrDefault(months[i], 0f)));
            }

            setupBarChart(barEntries);

            // Process data for pie chart (group expenses by category)
            Map<String, Float> categoryExpenses = new HashMap<>();
            for (Expense expense : expenses) {
                categoryExpenses.put(expense.getCategory(), categoryExpenses.getOrDefault(expense.getCategory(), 0f) + (float)expense.getAmount());
            }
            Log.d("DashboardActivity", "Bar Entries: " + barEntries);

            List<PieEntry> pieEntries = new ArrayList<>();
            for (Map.Entry<String, Float> entry : categoryExpenses.entrySet()) {
                pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
            }
            Log.d("DashboardActivity", "Category Expenses: " + categoryExpenses);
            Log.d("DashboardActivity", "Pie Entries: " + pieEntries);

            setupPieChart(pieEntries);
        } else {
            // Handle case where no expenses exist
            setupBarChart(new ArrayList<>());
            setupPieChart(new ArrayList<>());
        }

        // Fetch and display total balance
        double totalBalance = expenseRepository.calculateTotalBalance(); // Ensure you have a method in ExpenseRepository for this
        totalBalanceText.setText("$" + totalBalance);
    }

    // Helper method to extract the month name from a date string
    public String getMonthFromDate(String dateString) {
        // Define the expected format
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = format.parse(dateString);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
            return monthFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("DashboardActivity", "Error parsing date: " + dateString);
            return "";
        }
    }


    private void setupBarChart(List<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "Monthly Expenses");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        // X-axis customization
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Jan", "Feb", "Mar", "Apr","Jun"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void setupPieChart(List<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}
