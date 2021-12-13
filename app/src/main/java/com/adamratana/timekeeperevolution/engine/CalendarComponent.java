package com.adamratana.timekeeperevolution.engine;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.adamratana.timekeeperevolution.MyWatchFace;
import com.adamratana.timekeeperevolution.R;

public class CalendarComponent extends TimeComponent {
	protected CalendarComponent(MyWatchFace.Engine engine) {
		super(engine);
	}

	@Override
	public void initializeBackground() {
		mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.calendar_bg);
	}
}
