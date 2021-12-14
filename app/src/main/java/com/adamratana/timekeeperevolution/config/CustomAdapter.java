package com.adamratana.timekeeperevolution.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.adamratana.timekeeperevolution.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<ItemContents> {
	int selectedIndex = -1;

	public CustomAdapter(Context context, int activity_radio_button, List<ItemContents> saveItems) {
		super(context, activity_radio_button, saveItems);
	}

	public void setSelectedIndex(int index) {
		selectedIndex = index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			LayoutInflater layoutInflater;
			layoutInflater = LayoutInflater.from(getContext());
			v = layoutInflater.inflate(R.layout.radio_button_item, null);
		}

		RadioButton rbSelect = (RadioButton) v
				.findViewById(R.id.radioButton);

		rbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) {
					setSelectedIndex(position);
					notifyDataSetChanged();
				}
			}
		});
		// check the position to update correct radio button.
		rbSelect.setChecked(selectedIndex == position);

		ItemContents itemContents = getItem(position);

		if (itemContents != null) {
			TextView textCode = (TextView) v.findViewById(R.id.text);


			textCode.setText(itemContents.getText());
		}

		return v;
	}
}

