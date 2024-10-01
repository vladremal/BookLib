package com.example.booklib.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.booklib.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public  class SettingsActivity extends AppCompatActivity {

    private boolean isNightTheme = false;
    private SwitchMaterial switchMaterial;
    private MaterialToolbar toolbar;

    /**
     * This activity displays the settings screen of the application.
     * It contains a switch to toggle between light and dark themes.
     * It also contains a MaterialToolbar with a navigation menu.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        switchMaterial = findViewById(R.id.st_switch);
        toolbar = findViewById(R.id.topOtherBar);

        syncTheme();
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !isNightTheme){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else if (!isChecked && isNightTheme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            finish();
        });
    }
    /**
     * This method checks the current theme of the application and sets the switch accordingly.
     */

    private void syncTheme() {
        TypedValue typedValue = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getTheme().resolveAttribute(android.R.attr.isLightTheme, typedValue, true);
        }
        isNightTheme = typedValue.data == 0;
        switchMaterial.setChecked(isNightTheme);
    }
}