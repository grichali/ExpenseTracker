package com.example.exspenses;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText etAmount, etDate, etDescription;
    private Spinner spCategory;
    private Button btnSave;
    private ExpenseRepository expenseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);
        spCategory = findViewById(R.id.spCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.expense_categories,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCategory.setAdapter(adapter);

        btnSave = findViewById(R.id.btnSave);
        expenseRepository = new ExpenseRepository(this);

        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpenseActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String amountText = etAmount.getText().toString();
        String category = spCategory.getSelectedItem().toString();
        String date = etDate.getText().toString();
        String description = etDescription.getText().toString();

        if (!amountText.isEmpty() && !category.isEmpty() && !date.isEmpty() && !description.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountText);
                Expense newExpense = new Expense(0, description, category, date, amount);
                expenseRepository.addExpense(newExpense);
                Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddExpenseActivity.this, DashboardActivity.class);
                intent.putExtra("refresh_dashboard", true);
                startActivity(intent);
                finish();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

}
