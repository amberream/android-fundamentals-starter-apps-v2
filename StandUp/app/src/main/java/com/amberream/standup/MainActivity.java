package com.amberream.standup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private ToggleButton alarmToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmToggle = findViewById(R.id.toggleButton);
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastMessage;
                if (isChecked)
                {
                    toastMessage = getString(R.string.standup_alarm_on);
                }
                else
                {
                    toastMessage = getString(R.string.standup_alarm_off);
                }
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT);
            }
        });
    }
}
