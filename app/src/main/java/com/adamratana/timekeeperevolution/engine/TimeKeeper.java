package com.adamratana.timekeeperevolution.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.adamratana.timekeeperevolution.MyWatchFace;

public class TimeKeeper {
	private static int ABSOLUTE_WIDTH = 956;
	private static int ABSOLUTE_HEIGHT = 956;
	private static int CLOCK_WIDTH = 350;
	private static int CLOCK_HEIGHT = 350;
	private static int CALENDAR_WIDTH = 350;
	private static double CALENDAR_ASPECT_RATE = 0.614583;
	private static int CALENDAR_HEIGHT = (int) (CALENDAR_WIDTH * CALENDAR_ASPECT_RATE);

	private final MyWatchFace.Engine engine;
	private final OrreryComponent orrery;
	private final ClockComponent clock;
	private final CalendarComponent calendar;
	private boolean mMuteMode = false;

	public TimeKeeper(MyWatchFace.Engine engine) {
		this.engine = engine;

		orrery = new OrreryComponent(engine);
		clock = new ClockComponent(engine);
		calendar = new CalendarComponent(engine);
	}

	public void updateWatchHandStyle() {
		clock.updateWatchHandStyle();
	}

	public void onCreate() {
		orrery.setXY(0,0);
		orrery.setSize(ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT);

		clock.setXY(90,90);
		clock.setSize(CLOCK_WIDTH, CLOCK_HEIGHT);

		calendar.setXY(ABSOLUTE_WIDTH - CALENDAR_WIDTH,380);
		calendar.setSize(CALENDAR_WIDTH, CALENDAR_HEIGHT);
	}

	public void drawWatchFace(Canvas canvas) {
		orrery.drawWatchFace(canvas);
		clock.drawWatchFace(canvas);
	}

	public void drawBackground(Canvas canvas, boolean grayScale) {
		orrery.drawBackground(canvas, grayScale);
		clock.drawBackground(canvas, grayScale);
		calendar.drawBackground(canvas, grayScale);
	}

	public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		/* Scale loaded background image (more efficient) if surface dimensions change. */
		float scaleW = ((float) width) / (float) ABSOLUTE_WIDTH;
		float scaleH = ((float) width) / (float) ABSOLUTE_HEIGHT;

		orrery.onScaleChange(scaleW, scaleH);
		clock.onScaleChange(scaleW, scaleH);
		calendar.onScaleChange(scaleW, scaleH);
	}

	public void onMuteMode(boolean inMuteMode) {
		if (mMuteMode != inMuteMode) {
			mMuteMode = inMuteMode;
			clock.setAlpha(inMuteMode ? 100 : 255);
		}
	}
}
