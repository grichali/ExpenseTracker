package com.example.exspenses;

import android.app.DatePickerDialog;
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
        Spinner spCategory = findViewById(R.id.spCategory);

// Get the categories from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.expense_categories, // Name of the string array in strings.xml
                android.R.layout.simple_spinner_item // Default layout for each item in the spinner
        );

// Specify the layout for the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spCategory.setAdapter(adapter);
        btnSave = findViewById(R.id.btnSave);
        expenseRepository = new ExpenseRepository(this);
        EditText etDate = findViewById(R.id.etDate);
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpenseActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format and set the date in the EditText
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String amount = etAmount.getText().toString();
        String category = spCategory.getSelectedItem().toString();
        String date = etDate.getText().toString();
        String description = etDescription.getText().toString();

        if (!amount.isEmpty() && !category.isEmpty() && !date.isEmpty()) {
            Expense newExpense = new Expense(0, amount, category, date);
            expenseRepository.addExpense(newExpense);
            Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show();
            finish();  // Close the activity after saving
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
