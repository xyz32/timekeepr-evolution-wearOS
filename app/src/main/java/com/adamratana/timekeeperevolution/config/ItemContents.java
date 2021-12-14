package com.adamratana.timekeeperevolution.config;

import com.adamratana.timekeeperevolution.R;

public class ItemContents {
	int id = 0;
	int image = R.drawable.orrery_bg_backsky;
	int text = R.string.app_name;


	public void setId(int id) {
		this.id = id;
	}

	public void setText(int text) {
		this.text = text;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public int getText() {
		return text;
	}

	public int getImage() {
		return image;
	}
}
