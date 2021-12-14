package com.adamratana.timekeeperevolution.config;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adamratana.timekeeperevolution.R;

import java.util.ArrayList;
import java.util.List;

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
	}
}