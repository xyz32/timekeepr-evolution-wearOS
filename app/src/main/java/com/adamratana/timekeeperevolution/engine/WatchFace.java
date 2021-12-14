package com.adamratana.timekeeperevolution.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.palette.graphics.Palette;

import com.adamratana.timekeeperevolution.MyWatchFace;

import java.util.Calendar;

public class WatchFace {
	private static final float HOUR_STROKE_WIDTH = 5f;
	private static final float MINUTE_STROKE_WIDTH = 3f;
	private static final float SECOND_TICK_STROKE_WIDTH = 2f;
	private static final float CENTER_GAP_AND_CIRCLE_RADIUS = 4f;
	private static final int SHADOW_RADIUS = 6;

	MyWatchFace.Engine engine;

	private Paint mHourPaint;
	private Paint mMinutePaint;
	private Paint mSecondPaint;
	private Paint mTickAndCirclePaint;
	private int mWatchHandColor;
	private int mWatchHandHighlightColor;
	private int mWatchHandShadowColor;

	public WatchFace(MyWatchFace.Engine engine) {
		this.engine = engine;
		mWatchHandColor = Color.BLACK;
		mWatchHandHighlightColor = Color.RED;
		mWatchHandShadowColor = Color.BLACK;

		mHourPaint = new Paint();
		mHourPaint.setColor(mWatchHandColor);
		mHourPaint.setStrokeWidth(HOUR_STROKE_WIDTH);
		mHourPaint.setAntiAlias(true);
		mHourPaint.setStrokeCap(Paint.Cap.ROUND);
		mHourPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, mWatchHandShadowColor);

		mMinutePaint = new Paint();
		mMinutePaint.setColor(mWatchHandColor);
		mMinutePaint.setStrokeWidth(MINUTE_STROKE_WIDTH);
		mMinutePaint.setAntiAlias(true);
		mMinutePaint.setStrokeCap(Paint.Cap.ROUND);
		mMinutePaint.setShadowLayer(SHADOW_RADIUS, 0, 0, mWatchHandShadowColor);

		mSecondPaint = new Paint();
		mSecondPaint.setColor(mWatchHandHighlightColor);
		mSecondPaint.setStrokeWidth(SECOND_TICK_STROKE_WIDTH);
		mSecondPaint.setAntiAlias(true);
		mSecondPaint.setStrokeCap(Paint.Cap.ROUND);
		mSecondPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, mWatchHandShadowColor);

		mTickAndCirclePaint = new Paint();
		mTickAndCirclePaint.setColor(mWatchHandColor);
		mTickAndCirclePaint.setStrokeWidth(SECOND_TICK_STROKE_WIDTH);
		mTickAndCirclePaint.setAntiAlias(true);
		mTickAndCirclePaint.setStyle(Paint.Style.STROKE);
		mTickAndCirclePaint.setShadowLayer(SHADOW_RADIUS, 0, 0, mWatchHandShadowColor);
	}

	public void updateWatchHandStyle() {
		if (engine.mAmbient) {
			mHourPaint.setColor(Color.WHITE);
			mMinutePaint.setColor(Color.WHITE);
			mSecondPaint.setColor(Color.WHITE);
			mTickAndCirclePaint.setColor(Color.WHITE);

			mHourPaint.setAntiAlias(false);
			mMinutePaint.setAntiAlias(false);
			mSecondPaint.setAntiAlias(false);
			mTickAndCirclePaint.setAntiAlias(false);

			mHourPaint.clearShadowLayer();
			mMinutePaint.clearShadowLayer();
			mSecondPaint.clearShadowLayer();
			mTickAndCirclePaint.clearShadowLayer();

		} else {
			mHourPaint.setColor(mWatchHandColor);
			mMinutePaint.setColor(mWatchHandColor);
			mSecondPaint.setColor(mWatchHandHighlightColor);
			mTickAndCirclePaint.setColor(mWatchHandColor);

			mHourPaint.setAntiAlias(true);
			mMinutePaint.setAntiAlias(true);
			mSecondPaint.setAntiAlias(true);
			mTickAndCirclePaint.setAntiAlias(true);

			mHourPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, mWatchHandShadowColor);
			mMinutePaint.setShadowLayer(SHADOW_RADIUS, 0, 0, mWatchHandShadowColor);
			mSecondPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, mWatchHandShadowColor);
			mTickAndCirclePaint.setShadowLayer(SHADOW_RADIUS, 0, 0, mWatchHandShadowColor);
		}
	}

	public void drawWatchFace(Canvas canvas) {
		/*
		 * These calculations reflect the rotation in degrees per unit of time, e.g.,
		 * 360 / 60 = 6 and 360 / 12 = 30.
		 */
		final float seconds =
				(engine.mCalendar.get(Calendar.SECOND) + engine.mCalendar.get(Calendar.MILLISECOND) / 1000f);
		final float secondsRotation = seconds * 6f;

		final float minutesRotation = engine.mCalendar.get(Calendar.MINUTE) * 6f;

		final float hourHandOffset = engine.mCalendar.get(Calendar.MINUTE) / 2f;
		final float hoursRotation = (engine.mCalendar.get(Calendar.HOUR) * 30) + hourHandOffset;

		int handX = canvas.getWidth() / 2;
		int handY = canvas.getHeight() / 2;

		float mSecondHandLength = (float) (handX * 0.75);
		float sMinuteHandLength = (float) (handX * 0.65);
		float sHourHandLength = (float) (handX * 0.45);

		/*
		 * Save the canvas state before we can begin to rotate it.
		 */

		canvas.save();
		/*
		 * Ensure the "seconds" hand is drawn only when we are in interactive mode.
		 * Otherwise, we only update the watch face once a minute.
		 */

		if (!engine.mAmbient) {
			canvas.rotate(secondsRotation, handX, handY);
			canvas.drawLine(
					handX,
					handY,
					handX,
					handY - mSecondHandLength,
					mSecondPaint);

		}

		/* Restore the canvas" original orientation. */
		canvas.restore();

		canvas.save();
		canvas.rotate(hoursRotation, handX, handY);
		canvas.drawLine(
				handX,
				handY - CENTER_GAP_AND_CIRCLE_RADIUS,
				handX,
				handY - sHourHandLength,
				mHourPaint);

		canvas.rotate(minutesRotation - hoursRotation, handX, handY);
		canvas.drawLine(
				handX,
				handY - CENTER_GAP_AND_CIRCLE_RADIUS,
				handX,
				handY - sMinuteHandLength,
				mMinutePaint);

		canvas.drawCircle(
				handX,
				handY,
				CENTER_GAP_AND_CIRCLE_RADIUS,
				mTickAndCirclePaint);
		canvas.restore();
	}

	public void setAlpha(int alpha) {
		mHourPaint.setAlpha(alpha);
		mMinutePaint.setAlpha(alpha);
		mSecondPaint.setAlpha(alpha);
	}

	public void setColour(int colour) {
		mWatchHandColor = colour;

		updateWatchHandStyle();
	}

	public void calculatePalette(Bitmap mBackgroundBitmap) {
		Palette.from(mBackgroundBitmap).generate(new Palette.PaletteAsyncListener() {
			@Override
			public void onGenerated(Palette palette) {
				if (palette != null) {
					mWatchHandHighlightColor = palette.getVibrantColor(Color.RED);
					mWatchHandColor = palette.getLightVibrantColor(Color.WHITE);
					mWatchHandShadowColor = palette.getDarkMutedColor(Color.BLACK);
					updateWatchHandStyle();
				}
			}
		});
	}
}
