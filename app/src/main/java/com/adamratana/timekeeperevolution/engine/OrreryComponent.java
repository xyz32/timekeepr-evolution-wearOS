package com.adamratana.timekeeperevolution.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import com.adamratana.timekeeperevolution.MyWatchFace;
import com.adamratana.timekeeperevolution.R;
import com.adamratana.timekeeperevolution.config.Configs;
import com.adamratana.timekeeperevolution.engine.utils.Planets;

import java.util.Calendar;

public class OrreryComponent extends TimeComponent{

	private final float[] monthRingAngles = new float[] {0, -31, -62, -93, -123, -153, -182.5f, -212, -241.5f, -270.5f, -299.5f, -329.2f};
	private final WatchFace watchFace;
	private float monthAngle = 0;

	public OrreryComponent(MyWatchFace.Engine engine) {
		super(engine);

		watchFace = new WatchFace(engine);
		watchFace.setColour(Color.WHITE);
	}

	public void initializeBackground() {
		Bitmap tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.timekeeper_bg);
		mBackgroundBitmap = Bitmap.createBitmap(
				tmpBit.getWidth(),
				tmpBit.getHeight(),
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(mBackgroundBitmap);

		int topLeft = 187;
		int size = 589;
		tmpBit = BitmapFactory.decodeResource(getResources(), engine.configs.getInt(Configs.ConfigKey.background));
		canvas.drawBitmap(tmpBit, null, new RectF(topLeft, topLeft, topLeft + size, topLeft + size), backgroundPaint);

		tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.timekeeper_bg);
		canvas.drawBitmap(tmpBit, 0, 0, backgroundPaint);

		tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.inner_metal_frame_bg);
		canvas.drawBitmap(tmpBit, null, new RectF(topLeft, topLeft, topLeft + size, topLeft + size), backgroundPaint);

		canvas.save();
		tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.boddy_sun_top);
		float scaleFactor = 0.2f;
		canvas.translate(mBackgroundBitmap.getWidth() / 2 - tmpBit.getWidth()*scaleFactor / 2, mBackgroundBitmap.getHeight() / 2 - tmpBit.getHeight()*scaleFactor / 2);
		canvas.scale(scaleFactor, scaleFactor);
		canvas.drawBitmap(tmpBit, 0, 0, backgroundPaint);
		canvas.restore();

		int centerX = mBackgroundBitmap.getWidth() / 2 + 3;
		int centerY = mBackgroundBitmap.getHeight() / 2 + 3;
		drawPlanet(canvas, R.drawable.boddy_sun_top, 0.47f, 0, 0, centerX, centerY);
		drawPlanet(canvas, R.drawable.boddy_mercury_top, 0.15f, (float) -Planets.getTrueAnomaly(0, engine.mCalendar), 70, centerX, centerY);
		drawPlanet(canvas, R.drawable.body_venus_top, 0.17f, (float) -Planets.getTrueAnomaly(1, engine.mCalendar), 107, centerX, centerY);
		drawPlanet(canvas, R.drawable.body_earth_top, 0.18f, (float) -Planets.getTrueAnomaly(2, engine.mCalendar), 145, centerX, centerY);
		drawPlanet(canvas, R.drawable.body_mars_top, 0.16f, (float) -Planets.getTrueAnomaly(3, engine.mCalendar), 177, centerX, centerY);
		drawPlanet(canvas, R.drawable.body_jupiter_top, 0.25f, (float) -Planets.getTrueAnomaly(4, engine.mCalendar), 207, centerX, centerY);
		drawPlanet(canvas, R.drawable.body_saturn_top, 0.43f, (float) -Planets.getTrueAnomaly(5, engine.mCalendar), 237, centerX, centerY);
		drawPlanet(canvas, R.drawable.body_uranus_top, 0.19f, (float) -Planets.getTrueAnomaly(6, engine.mCalendar), 267, centerX, centerY);
		drawPlanet(canvas, R.drawable.body_neptune_top, 0.19f, (float) -Planets.getTrueAnomaly(7, engine.mCalendar), 295, centerX, centerY);

		int top = 40;
		int left = 34;
		size = 890;

		int monthIndex = engine.mCalendar.get(Calendar.MONTH);
		int dayOfMonth = engine.mCalendar.get(Calendar.DAY_OF_MONTH) - 1;
		monthAngle =  monthRingAngles[monthIndex] - dayOfMonth;

		canvas.save();
		canvas.rotate(monthAngle, (float) (left + size/2), (float) (top + size/2));
		tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.month_ring);
		canvas.drawBitmap(tmpBit, null, new RectF(left, top, left + size, top + size), backgroundPaint);
		canvas.restore();
	}

	public void drawWatchFace(Canvas canvas) {
		int monthIndex = engine.mCalendar.get(Calendar.MONTH);
		int dayOfMonth = engine.mCalendar.get(Calendar.DAY_OF_MONTH) - 1;
		float newMonthAngle =  monthRingAngles[monthIndex] - dayOfMonth;

		if (monthAngle != newMonthAngle) {
			monthAngle = newMonthAngle;
			initializeBackground();
		}

		if (engine.configs.getInt(Configs.ConfigKey.clockStyle) == 1) {
			watchFace.drawWatchFace(canvas);
		}
	}

	private void drawPlanet(Canvas canvas, int resource, float scaleFactor, float rotation, int distance, int centerX, int centerY) {
		canvas.save();
		Bitmap tmpBit = BitmapFactory.decodeResource(getResources(), resource);
		canvas.rotate(rotation, centerX, centerY);
		canvas.translate(centerX - tmpBit.getWidth()*scaleFactor / 2, centerY - distance - tmpBit.getHeight()*scaleFactor / 2);
		canvas.scale(scaleFactor, scaleFactor);
		canvas.drawBitmap(tmpBit, 0, 0, backgroundPaint);
		canvas.restore();
	}
}
