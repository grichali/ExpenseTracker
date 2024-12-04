package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private BarChart barChart;
    private PieChart pieChart;
    private ExtendedFloatingActionButton addExpenseFab;
    private TextView totalBalanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Setup Toolbar


        // Initialize UI components
        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);
        totalBalanceText = findViewById(R.id.total_balance_text);
        addExpenseFab = findViewById(R.id.add_expense_fab);

        addExpenseFab.setOnClickListener(v -> {
            // Create an Intent to open AddExpenseActivity
            Intent intent = new Intent(DashboardActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });

        // Fetch and set dynamic data
        setDynamicData();
    }

    private void setDynamicData() {
        // Fetch dynamic data directly from the class (e.g., database or API)
        // Here, I’m using hardcoded values for the sake of this example.
        // You can replace this with real-time data fetch logic.

        // Data for the bar chart (monthly expenses)
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 1200)); // January
        barEntries.add(new BarEntry(1, 1500)); // February
        barEntries.add(new BarEntry(2, 1100)); // March
        barEntries.add(new BarEntry(3, 1900)); // April
        barEntries.add(new BarEntry(4, 2000)); // Jun

        // Data for the pie chart (expense categories)
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(500, "Food"));
        pieEntries.add(new PieEntry(300, "Transport"));
        pieEntries.add(new PieEntry(400, "Entertainment"));
        pieEntries.add(new PieEntry(200, "Utilities"));

        // Total balance data
        String totalBalance = "$5,420.50";

        // Update UI with the data
        totalBalanceText.setText(totalBalance);
        setupBarChart(barEntries);
        setupPieChart(pieEntries);
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
