package com.adamratana.timekeeperevolution.config;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.adamratana.timekeeperevolution.R;

public class Configs {
	// support variables
	private static final String GENERAL_CONFIGS = "generalConfigs";
	private final SharedPreferences settings;

	private Configs(Context context) {
		settings = context.getSharedPreferences(GENERAL_CONFIGS, 0);
	}

	public static Configs instance(AppCompatActivity pActivity) {
		return instance(pActivity.getBaseContext());
	}

	public static Configs instance(Context context) {
		return new Configs(context);
	}

	public int getInt(ConfigKey key) {
		return settings.getInt(key.storeKeyID, (int) key.defaultVal);
	}

	public void setInt(ConfigKey key, int value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key.storeKeyID, value);

		editor.apply();
	}

	public float getFloat(ConfigKey key) {
		return settings.getFloat(key.storeKeyID, (float) key.defaultVal);
	}

	public void setFloat(ConfigKey key, float value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key.storeKeyID, value);

		editor.apply();

	}

	public boolean getBoolean(ConfigKey key) {
		return settings.getBoolean(key.storeKeyID, (boolean) key.defaultVal);
	}

	public void setBoolean(ConfigKey key, boolean value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key.storeKeyID, value);

		editor.apply();

	}

	public String getString(ConfigKey key) {
		return settings.getString(key.storeKeyID, (String) key.defaultVal);
	}

	public void setString(ConfigKey key, String value) {

		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key.storeKeyID, value);

		editor.apply();

	}

	private <T> T getValue(ConfigKey key, Class<T> dataType) {

		return dataType.cast(key.defaultVal);

	}

	public enum ConfigKey {
		showClock("showClock", true),
		clockStyle("clockStyle", 0),
		showCalendar("showCalendar", true),
		background("background", R.drawable.orrery_bg_supernova);

		public String storeKeyID;
		public Object defaultVal;
		ConfigKey(String storeKeyID, Object defValue) {
			this.storeKeyID = storeKeyID;
			this.defaultVal = defValue;
		}
	}
}

