package com.adamratana.timekeeperevolution.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.adamratana.timekeeperevolution.MyWatchFace;
import com.adamratana.timekeeperevolution.R;

public class TimeKeeper {
	private static int FACE_WIDTH = 956;
	private static int FACE_HEIGHT = 956;
	private final MyWatchFace.Engine engine;
	private final OrreryComponent orrery;
	private final ClockComponent clock;
	private final CalendarComponent calendar;
	private Bitmap mBackgroundBitmap;
	private Bitmap mGrayBackgroundBitmap;
	private Paint mBackgroundPaint;
	private Canvas canvas;
	private boolean mMuteMode = false;

	public TimeKeeper(MyWatchFace.Engine engine) {
		this.engine = engine;
		orrery = new OrreryComponent(engine);
		clock = new ClockComponent(engine);
		calendar = new CalendarComponent(engine);

		mBackgroundBitmap = Bitmap.createBitmap(
				FACE_WIDTH,
				FACE_HEIGHT,
				Bitmap.Config.ARGB_8888);

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(Color.BLACK);

		canvas = new Canvas(mBackgroundBitmap);
	}

	public void updateWatchHandStyle() {
		clock.updateWatchHandStyle();
	}

	public void initializeBackground() {
		orrery.initializeBackground();
		clock.initializeBackground();
		calendar.initializeBackground();

		orrery.setXY(0,0);
		orrery.setSize(FACE_WIDTH, FACE_HEIGHT);

		clock.setXY(90,90);
		clock.setSize(350, 350);
	}

	public void drawWatchFace(Canvas canvas) {
		clock.drawWatchFace(canvas);
	}

	public void initGrayBackgroundBitmap() {
		mGrayBackgroundBitmap = convertToGray(mBackgroundBitmap);
	}

	public void drawBackground(Canvas canvas, boolean grayScale) {
		if (grayScale) {
			canvas.drawBitmap(mGrayBackgroundBitmap, 0, 0, mBackgroundPaint);
		} else {
			canvas.drawBitmap(mBackgroundBitmap, 0, 0, mBackgroundPaint);
		}
	}

	private void generateBackground() {
		orrery.drawBackground(canvas);
		clock.drawBackground(canvas);
	}

	private Bitmap convertToGray(Bitmap toConvert) {
		Bitmap result = Bitmap.createBitmap(
				toConvert.getWidth(),
				toConvert.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		Paint grayPaint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
		grayPaint.setColorFilter(filter);
		canvas.drawBitmap(toConvert, 0, 0, grayPaint);
		return result;
	}

	public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		generateBackground();

		/* Scale loaded background image (more efficient) if surface dimensions change. */
		float scale = ((float) width) / (float) mBackgroundBitmap.getWidth();

		mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap,
				(int) (mBackgroundBitmap.getWidth() * scale),
				(int) (mBackgroundBitmap.getHeight() * scale), true);

		initGrayBackgroundBitmap();
	}

	public void onMuteMode(boolean inMuteMode) {
		if (mMuteMode != inMuteMode) {
			mMuteMode = inMuteMode;
			clock.setAlpha(inMuteMode ? 100 : 255);
		}
	}
}
