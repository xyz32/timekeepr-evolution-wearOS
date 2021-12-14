package com.adamratana.timekeeperevolution.engine;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.adamratana.timekeeperevolution.MyWatchFace;
import com.adamratana.timekeeperevolution.config.Configs;

public class TimeKeeper {
	private static int ABSOLUTE_WIDTH = 956;
	private static int ABSOLUTE_HEIGHT = 956;
	private static int CLOCK_WIDTH = 350;
	private static int CLOCK_HEIGHT = 350;
	private static int CALENDAR_WIDTH = 370;
	private static double CALENDAR_ASPECT_RATE = 0.614583;
	private static int CALENDAR_HEIGHT = (int) (CALENDAR_WIDTH * CALENDAR_ASPECT_RATE);

	private final MyWatchFace.Engine engine;
	private OrreryComponent orrery;
	private ClockComponent clock;
	private CalendarComponent calendar;
	private boolean mMuteMode = false;

	public TimeKeeper(MyWatchFace.Engine engine) {
		this.engine = engine;
	}

	public void invalidate() {
		orrery.initializeBackground();
		if (engine.configs.getBoolean(Configs.ConfigKey.showClock)) {
			clock.initializeBackground();
		}
		if (engine.configs.getBoolean(Configs.ConfigKey.showCalendar)) {
			calendar.initializeBackground();
		}
	}

	public void updateWatchHandStyle() {
		if (engine.configs.getBoolean(Configs.ConfigKey.showClock)) {
			clock.updateWatchHandStyle();
		}
	}

	public void onCreate() {
		orrery = new OrreryComponent(engine);
		orrery.setXY(0,0);
		orrery.setSize(ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT);

		clock = new ClockComponent(engine);
		if (engine.configs.getInt(Configs.ConfigKey.clockStyle) == 0) {
			clock.setXY(90, 90);
			clock.setSize(CLOCK_WIDTH, CLOCK_HEIGHT);
		} else {
			clock.setXY(0, 0);
			clock.setSize(ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT);
		}

		calendar = new CalendarComponent(engine);
		calendar.setXY(ABSOLUTE_WIDTH - CALENDAR_WIDTH,380);
		calendar.setSize(CALENDAR_WIDTH, CALENDAR_HEIGHT);
	}

	public void drawWatchFace(Canvas canvas) {
		orrery.drawWatchFace(canvas);
		if (engine.configs.getBoolean(Configs.ConfigKey.showClock)) {
			clock.drawWatchFace(canvas);
		}
	}

	public void drawBackground(Canvas canvas, boolean grayScale) {
		orrery.drawBackground(canvas, grayScale);
		if (engine.configs.getBoolean(Configs.ConfigKey.showClock)
			&& (engine.configs.getInt(Configs.ConfigKey.clockStyle) == 0)) {
			clock.drawBackground(canvas, grayScale);
		}
		if (engine.configs.getBoolean(Configs.ConfigKey.showCalendar)) {
			calendar.drawBackground(canvas, grayScale);
		}
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
