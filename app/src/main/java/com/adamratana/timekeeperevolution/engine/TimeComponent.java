package com.adamratana.timekeeperevolution.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.adamratana.timekeeperevolution.MyWatchFace;

public abstract class TimeComponent {
	protected final MyWatchFace.Engine engine;
	protected Paint mBackgroundPaint;
	protected Bitmap mBackgroundBitmap;

	protected Paint tmpPaint = new Paint();

	protected int x = 0;
	protected int y = 0;
	protected int width;
	protected int height;

	public abstract void initializeBackground();

	protected TimeComponent(MyWatchFace.Engine engine) {
		this.engine = engine;
	}

	public void drawBackground(Canvas canvas) {
		canvas.drawBitmap(mBackgroundBitmap, null, new RectF(x, y, x + width, y + height), tmpPaint);
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
}
