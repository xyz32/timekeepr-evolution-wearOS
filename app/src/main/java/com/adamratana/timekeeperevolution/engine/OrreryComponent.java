package com.adamratana.timekeeperevolution.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.adamratana.timekeeperevolution.MyWatchFace;
import com.adamratana.timekeeperevolution.R;

import java.util.Calendar;

public class OrreryComponent extends TimeComponent{

	private final float[] monthRingAngles = new float[] {0, -31, -62, -93, -123, -153, -182.5f, -212, -241.5f, -270.5f, -299.5f, -329.2f};
	private float monthAngle = 0;

	public OrreryComponent(MyWatchFace.Engine engine) {
		super(engine);
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
		tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.orrery_bg);
		canvas.drawBitmap(tmpBit, null, new RectF(topLeft, topLeft, topLeft + size, topLeft + size), backgroundPaint);

		tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.timekeeper_bg);
		canvas.drawBitmap(tmpBit, 0, 0, backgroundPaint);

		tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.inner_metal_frame_bg);
		canvas.drawBitmap(tmpBit, null, new RectF(topLeft, topLeft, topLeft + size, topLeft + size), backgroundPaint);

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
	}
}
