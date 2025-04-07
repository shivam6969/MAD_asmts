package com.example.unitconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatDelegate;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue1, inputValue2;
    private AutoCompleteTextView unit1, unit2;
    private boolean isInput1Active = true;

    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_IS_DARK = "is_dark_mode";

    // Conversion rates for length units (base unit: meters)
    private final Map<String, Double> conversionRates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDark = prefs.getBoolean(KEY_IS_DARK, false);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        if (isDark) {
            setTheme(R.style.Theme_UnitConverter_Dark);
        } else {
            setTheme(R.style.Theme_UnitConverter);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupConversionRates();
        setupUnitSelectors();
        setupInputListeners();
        setupThemeSwitch(prefs, isDark);
    }

    private void initializeViews() {
        inputValue1 = findViewById(R.id.inputValue1);
        inputValue2 = findViewById(R.id.inputValue2);
        unit1 = findViewById(R.id.unit1);
        unit2 = findViewById(R.id.unit2);
    }

    private void setupConversionRates() {
        // Length conversions (base unit: meters)
        conversionRates.put("Meters", 1.0);
        conversionRates.put("Centimeters", 100.0);
        conversionRates.put("Feet", 3.28084);
        conversionRates.put("Inches", 39.3701);
        conversionRates.put("Yards", 1.09361);
    }

    private void setupUnitSelectors() {
        String[] units = conversionRates.keySet().toArray(new String[0]);
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this,
                R.layout.custom_dropdown_item,
                R.id.dropdown_text,
                units
        );

        unit1.setAdapter(unitAdapter);
        unit2.setAdapter(unitAdapter);

        // Set default units
        unit1.setText("Meters", false);
        unit2.setText("Centimeters", false);
    }

    private void setupInputListeners() {
        inputValue1.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                isInput1Active = true;
                inputValue2.setText("");
            }
        });

        inputValue2.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                isInput1Active = false;
                inputValue1.setText("");
            }
        });

        findViewById(R.id.calculateButton).setOnClickListener(v -> convert());
    }

    private void setupThemeSwitch(SharedPreferences prefs, boolean isDark) {
        SwitchMaterial themeSwitch = findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(isDark);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_IS_DARK, isChecked).apply();
            recreate();
        });
    }

    private void convert() {
        String valueStr;
        String fromUnit, toUnit;
        EditText fromField, toField;

        if (isInput1Active) {
            valueStr = inputValue1.getText().toString();
            fromUnit = unit1.getText().toString();
            toUnit = unit2.getText().toString();
            fromField = inputValue1;
            toField = inputValue2;
        } else {
            valueStr = inputValue2.getText().toString();
            fromUnit = unit2.getText().toString();
            toUnit = unit1.getText().toString();
            fromField = inputValue2;
            toField = inputValue1;
        }

        if (valueStr.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double value = Double.parseDouble(valueStr);
            // Convert to base unit (meters) first, then to target unit
            double valueInMeters = value / conversionRates.get(fromUnit);
            double result = valueInMeters * conversionRates.get(toUnit);
            toField.setText(String.format("%.4f", result));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }
}
