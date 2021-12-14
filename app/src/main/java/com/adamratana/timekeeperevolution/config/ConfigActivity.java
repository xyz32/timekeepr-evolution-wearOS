package com.adamratana.timekeeperevolution.config;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.ComponentActivity;

import com.adamratana.timekeeperevolution.R;

public class ConfigActivity extends ComponentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);

		findViewById(R.id.menuBackground).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConfigActivity.this, ConfigBackgroundActivity.class);
				startActivity(intent);
			}
		});

		Configs configs = Configs.instance(this);

		CheckBox showClock = findViewById(R.id.showClock);
		showClock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((CheckBox)v).isChecked()) {
					configs.setBoolean(Configs.ConfigKey.showClock, true);
					findViewById(R.id.clockStyleRadioGroup).setVisibility(View.VISIBLE);
				} else {
					configs.setBoolean(Configs.ConfigKey.showClock, false);
					findViewById(R.id.clockStyleRadioGroup).setVisibility(View.GONE);
				}
			}
		});
		showClock.setChecked(configs.getBoolean(Configs.ConfigKey.showClock));
		showClock.callOnClick();

		CheckBox showCalendar = findViewById(R.id.showCalendar);
		showCalendar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((CheckBox)v).isChecked()) {
					configs.setBoolean(Configs.ConfigKey.showCalendar, true);
				} else {
					configs.setBoolean(Configs.ConfigKey.showCalendar, false);
				}
			}
		});
		showCalendar.setChecked(configs.getBoolean(Configs.ConfigKey.showCalendar));

		RadioGroup clockStyleRadioGroup = findViewById(R.id.clockStyleRadioGroup);
		int clockStyle = configs.getInt(Configs.ConfigKey.clockStyle);
		switch (clockStyle) {
			case 0:
				((RadioButton)findViewById(R.id.smallClock)).setChecked(true);
				break;
			case 1:
				((RadioButton)findViewById(R.id.largeClock)).setChecked(true);
				break;
		}
		clockStyleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				if (i == R.id.smallClock) {
					configs.setInt(Configs.ConfigKey.clockStyle, 0);
				}

				if (i == R.id.largeClock) {
					configs.setInt(Configs.ConfigKey.clockStyle, 1);
				}
			}
		});
	}
}