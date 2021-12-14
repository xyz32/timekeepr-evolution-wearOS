package com.adamratana.timekeeperevolution.config;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.ComponentActivity;

import com.adamratana.timekeeperevolution.R;

import java.util.ArrayList;
import java.util.List;

public class ConfigBackgroundActivity extends ComponentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_background);

		Configs configs = Configs.instance(this);

		final List<ItemContents> saveItems = new ArrayList<>();
		saveItems.add(itemContents(0, R.string.background_supernova, R.drawable.orrery_bg_supernova));
		saveItems.add(itemContents(1, R.string.background_dark_sky, R.drawable.orrery_bg_backsky));
		saveItems.add(itemContents(2, R.string.background_scifi, R.drawable.orrery_bg_scifi));
		int selected = 0;
		int currentBg = configs.getInt(Configs.ConfigKey.background);
		for (ItemContents item: saveItems) {
			if (item.getImage() == currentBg){
				break;
			}
			selected ++;
		}

		final CustomAdapter adapter = new CustomAdapter(this, R.layout.radio_button_item, saveItems, selected);
		ListView listView = (ListView) findViewById(R.id.radioGroup);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.setSelectedIndex(position);  // set selected position and notify the adapter
				adapter.notifyDataSetChanged();

				configs.setInt(Configs.ConfigKey.background, saveItems.get(position).getImage());
			}
		});
	}

	private static ItemContents itemContents(int i, final int text, final int picture) {
		ItemContents itemContent = new ItemContents();
		itemContent.setId(i);
		itemContent.setText(text);
		itemContent.setImage(picture);
		return itemContent;

	}
}