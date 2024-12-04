package com.example.expensetracker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.animation.Easing;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private BarChart barChart;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize charts
        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);

        // Set up bar chart
        setupBarChart();

        // Set up pie chart
        setupPieChart();
    }

    private void setupBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 1200)); // Total expenses
        entries.add(new BarEntry(1, 1500)); // Budget

        BarDataSet dataSet = new BarDataSet(entries, "Expenses vs Budget");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // Set bar width

        barChart.setData(barData);
        barChart.setFitBars(true); // Makes bars fit into the x-axis
        barChart.getDescription().setEnabled(false); // Disable description
        barChart.animateY(1000, Easing.EaseInOutCubic); // Animation
        barChart.invalidate(); // Refresh chart
    }

    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(500, "Food"));
        entries.add(new PieEntry(300, "Transport"));
        entries.add(new PieEntry(400, "Entertainment"));

        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false); // Disable description
        pieChart.setUsePercentValues(true); // Show percentages
        pieChart.animateY(1000, Easing.EaseInOutCubic); // Animation
        pieChart.invalidate(); // Refresh chart
    }
}
