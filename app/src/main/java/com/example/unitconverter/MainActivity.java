package com.example.unitconverter;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // UI Components
    private EditText inputValue;
    private Spinner inputUnitSpinner;
    private Spinner outputUnitSpinner;
    private TextView resultTextView;

    // Unit conversion factors (to meters)
    private final Map<String, Double> conversionFactors = new HashMap<String, Double>() {{
        put("Feet", 0.3048);
        put("Inches", 0.0254);
        put("Centimeters", 0.01);
        put("Meters", 1.0);
        put("Yards", 0.9144);
    }};

    private String inputUnit = "Meters";
    private String outputUnit = "Feet";
    private boolean isUpdating = false;
    private DecimalFormat decimalFormat = new DecimalFormat("#.#####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        inputValue = findViewById(R.id.input_value);
        inputUnitSpinner = findViewById(R.id.input_unit_spinner);
        outputUnitSpinner = findViewById(R.id.output_unit_spinner);
        resultTextView = findViewById(R.id.result_text_view);

        // Setup spinners with unit options
        setupSpinners();

        // Setup input text change listener
        inputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdating) {
                    updateConversion();
                }
            }
        });
    }

    private void setupSpinners() {
        // Create adapter for the spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Feet", "Inches", "Centimeters", "Meters", "Yards"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapters to spinners
        inputUnitSpinner.setAdapter(adapter);
        outputUnitSpinner.setAdapter(adapter);

        // Set default selections
        inputUnitSpinner.setSelection(3); // Meters
        outputUnitSpinner.setSelection(0); // Feet

        // Set selection listeners
        inputUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputUnit = parent.getItemAtPosition(position).toString();
                updateConversion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not needed
            }
        });

        outputUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                outputUnit = parent.getItemAtPosition(position).toString();
                updateConversion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not needed
            }
        });
    }

    private void updateConversion() {
        isUpdating = true;

        String inputText = inputValue.getText().toString();
        if (inputText.isEmpty()) {
            resultTextView.setText("0");
            isUpdating = false;
            return;
        }

        try {
            double value = Double.parseDouble(inputText);
            double result = convertUnits(value, inputUnit, outputUnit);
            resultTextView.setText(decimalFormat.format(result));
        } catch (NumberFormatException e) {
            resultTextView.setText("Invalid input");
        }

        isUpdating = false;
    }

    private double convertUnits(double value, String fromUnit, String toUnit) {
        // First convert to meters (our base unit)
        double valueInMeters = value * conversionFactors.get(fromUnit);

        // Then convert from meters to target unit
        return valueInMeters / conversionFactors.get(toUnit);
    }
}