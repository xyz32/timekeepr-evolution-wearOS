package com.adamratana.timekeeperevolution.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.adamratana.timekeeperevolution.MyWatchFace;
import com.adamratana.timekeeperevolution.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarComponent extends TimeComponent {
	private final Paint mTextPaint;

	protected CalendarComponent(MyWatchFace.Engine engine) {
		super(engine);

		mTextPaint = new Paint();
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(40);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
	}

	@Override
	public void initializeBackground() {
		Bitmap tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.calendar_bg);

		mBackgroundBitmap = Bitmap.createBitmap(
				tmpBit.getWidth(),
				tmpBit.getHeight(),
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(mBackgroundBitmap);

		canvas.drawBitmap(tmpBit, 0, 0, backgroundPaint);

		String[] dateStrings;
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

		Calendar calendar = Calendar.getInstance();
		dateStrings = dayFormat.format(calendar.getTime()).split("-");

		canvas.drawText(dateStrings[0], 224, 65, mTextPaint);
		mTextPaint.setTextSize(32);
		canvas.drawText(dateStrings[1].toUpperCase(), 120, 124, mTextPaint);
		mTextPaint.setTextSize(40);
		canvas.drawText(dateStrings[2], 195, 190, mTextPaint);
	}
}
