package com.adamratana.timekeeperevolution.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.adamratana.timekeeperevolution.MyWatchFace;
import com.adamratana.timekeeperevolution.R;
import com.adamratana.timekeeperevolution.config.Configs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ClockComponent extends TimeComponent {
	private static final float HOUR_STROKE_WIDTH = 5f;
	private static final float MINUTE_STROKE_WIDTH = 3f;
	private static final float SECOND_TICK_STROKE_WIDTH = 2f;

	private static final float CENTER_GAP_AND_CIRCLE_RADIUS = 4f;

	private static final int SHADOW_RADIUS = 8;

	private final Paint mHourPaint;
	private final Paint mMinutePaint;
	private final Paint mSecondPaint;
	private final Paint mTickAndCirclePaint;
	private final Paint mTextPaint;
	private final int mWatchHandColor;
	private final int mWatchHandHighlightColor;
	private final int mWatchHandShadowColor;
	private int weekDay = 0;

	public ClockComponent(MyWatchFace.Engine engine) {
		super(engine);

		mWatchHandColor = Color.WHITE;
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

		mTextPaint = new Paint();
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(30);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
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

	@Override
	public void initializeBackground() {
		Bitmap tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.clock_bg);

		mBackgroundBitmap = Bitmap.createBitmap(
				tmpBit.getWidth(),
				tmpBit.getHeight(),
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(mBackgroundBitmap);

		canvas.drawBitmap(tmpBit, 0, 0, backgroundPaint);

		String weekDay;
		SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());

		Calendar calendar = Calendar.getInstance();
		weekDay = dayFormat.format(calendar.getTime());

		canvas.drawText(weekDay.toUpperCase(), 166, 236, mTextPaint);

		/* Extracts colors from background image to improve watchface style. */
//		Palette.from(mBackgroundBitmap).generate(new Palette.PaletteAsyncListener() {
//			@Override
//			public void onGenerated(Palette palette) {
//				if (palette != null) {
//					mWatchHandHighlightColor = palette.getVibrantColor(Color.RED);
//					mWatchHandColor = palette.getLightVibrantColor(Color.WHITE);
//					mWatchHandShadowColor = palette.getDarkMutedColor(Color.BLACK);
//					updateWatchHandStyle();
//				}
//			}
//		});
	}

	public void drawWatchFace(Canvas canvas) {
		if (weekDay != engine.mCalendar.get(Calendar.DAY_OF_WEEK)) {
			weekDay = engine.mCalendar.get(Calendar.DAY_OF_WEEK);
			initializeBackground();
		}

		if (engine.configs.getInt(Configs.ConfigKey.clockStyle) != TimeKeeper.CLOCK_STATUS_SMALL) {
			return;
		}

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

		/*
		 * Save the canvas state before we can begin to rotate it.
		 */

		canvas.save();
		/*
		 * Ensure the "seconds" hand is drawn only when we are in interactive mode.
		 * Otherwise, we only update the watch face once a minute.
		 */

		float mSecondHandX = 178;
		int handX = (int) (scaledX + mSecondHandX * scaleW);
		float mSecondHandY = 114;
		int handY = (int) (scaledY + mSecondHandY * scaleH);

		if (!engine.mAmbient) {
			canvas.rotate(secondsRotation, handX, handY);
			float mSecondHandLength = 32;
			canvas.drawLine(
					handX,
					handY,
					handX,
					handY - mSecondHandLength * scaleH,
					mSecondPaint);

		}

		/* Restore the canvas" original orientation. */
		canvas.restore();

		canvas.save();
		float sMinuteHandCenterX = 178;
		handX = (int) (scaledX + sMinuteHandCenterX * scaleW);
		float sMinuteHandCenterY = 176;
		handY = (int) (scaledY + sMinuteHandCenterY * scaleH);

		canvas.rotate(hoursRotation, handX, handY);
		float sHourHandLength = 70;
		canvas.drawLine(
				handX,
				handY - CENTER_GAP_AND_CIRCLE_RADIUS,
				handX,
				handY - sHourHandLength * scaleH,
				mHourPaint);

		canvas.rotate(minutesRotation - hoursRotation, handX, handY);
		float sMinuteHandLength = 100;
		canvas.drawLine(
				handX,
				handY - CENTER_GAP_AND_CIRCLE_RADIUS,
				handX,
				handY - sMinuteHandLength * scaleH,
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
}
