package com.adamratana.timekeeperevolution.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;

import com.adamratana.timekeeperevolution.MyWatchFace;

public abstract class TimeComponent {
	protected final MyWatchFace.Engine engine;
	protected Bitmap mBackgroundBitmap;
	private Bitmap mGrayBackgroundBitmap;

	protected Paint backgroundPaint;

	protected int x = 0;
	protected int y = 0;
	protected int width;
	protected int height;

	protected int scaledX = 0;
	protected int scaledY = 0;
	protected int scaledWidth;
	protected int scaledHeight;

	protected float scaleW = 1;
	protected float scaleH = 1;

	public abstract void initializeBackground();

	protected TimeComponent(MyWatchFace.Engine engine) {
		this.engine = engine;

		backgroundPaint = new Paint();
		backgroundPaint.setShadowLayer(6, 0, 0, Color.BLACK);
	}

	public void drawBackground(Canvas canvas, boolean grayScale) {
		RectF scaledRect = new RectF(scaledX, scaledY, scaledX + scaledWidth, scaledY + scaledHeight);
		if (grayScale) {
			canvas.drawBitmap(mGrayBackgroundBitmap, null, scaledRect, backgroundPaint);
		} else {
			canvas.drawBitmap(mBackgroundBitmap, null, scaledRect, backgroundPaint);
		}
	}

	protected Resources getResources() {
		return engine.getDisplayContext().getResources();
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void onScaleChange(float scaleW, float scaleH) {
		this.scaleW = scaleW;
		this.scaleH = scaleH;
		this.scaledX = (int) (x * scaleW);
		this.scaledY = (int) (y * scaleW);
		this.scaledWidth = (int) (width * scaleW);
		this.scaledHeight = (int) (height * scaleW);

		initializeBackground();

		mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap,
				(int) (mBackgroundBitmap.getWidth() * scaleW),
				(int) (mBackgroundBitmap.getHeight() * scaleH), true);

		mGrayBackgroundBitmap = convertToGray(mBackgroundBitmap);
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
}
