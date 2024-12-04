package com.example.expensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        initializeViews();
        setupCategorySpinner();
        setupDateField();
        setupSaveButton();
    }

    private void initializeViews() {
        // Initialize EditText fields
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);

        // Initialize the Save button
        btnSave = findViewById(R.id.btnSave);

        // Initialize the Spinner for categories
        spCategory = findViewById(R.id.spCategory);
    }

    private void setupCategorySpinner() {
        // Create an ArrayAdapter for the Spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);

        // Set the drop-down view style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        spCategory.setAdapter(adapter);

        // Set the item selected listener for the Spinner
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Optionally handle the selected category
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected
            }
        });
    }

    private void setupDateField() {
        // Set up the onClickListener for the Date field (EditText)
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the DatePickerDialog when the user clicks on the Date field
                showDatePickerDialog();
            }
        });
    }

    private void setupSaveButton() {
        // Set up the onClickListener for the Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddExpenseActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    // Format the selected date and set it to the Date field
                    String formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                    etDate.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveExpense() {
        // Retrieve the values from the EditText fields and Spinner
        String amount = etAmount.getText().toString();
        String date = etDate.getText().toString();
        String description = etDescription.getText().toString();
        String category = spCategory.getSelectedItem().toString();

        // Display the entered information using a Toast
        String message = String.format("Amount: %s\nCategory: %s\nDate: %s\nDescription: %s",
                amount, category, date, description);
        Toast.makeText(AddExpenseActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
