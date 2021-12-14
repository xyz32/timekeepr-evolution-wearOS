package com.adamratana.timekeeperevolution.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.adamratana.timekeeperevolution.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<ItemContents> {
	int selectedIndex = -1;

	public CustomAdapter(Context context, int activity_radio_button, List<ItemContents> saveItems, int selectedIndex) {
		super(context, activity_radio_button, saveItems);
		this.selectedIndex = selectedIndex;
	}

	public void setSelectedIndex(int index) {
		selectedIndex = index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			LayoutInflater layoutInflater;
			layoutInflater = LayoutInflater.from(getContext());
			view = layoutInflater.inflate(R.layout.radio_button_item, null);
		}

		RadioButton rbSelect = (RadioButton) view
				.findViewById(R.id.radioButton);

		// check the position to update correct radio button.
		rbSelect.setChecked(selectedIndex == position);

		ItemContents itemContents = getItem(position);

		if (itemContents != null) {
			((TextView)view.findViewById(R.id.backgroundText)).setText(itemContents.getText());
			((ImageView)view.findViewById(R.id.backgroundImage)).setImageResource(itemContents.getImage());
		}
		return view;
	}
}

