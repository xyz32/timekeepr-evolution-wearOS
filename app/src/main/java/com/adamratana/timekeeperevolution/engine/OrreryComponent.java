package com.adamratana.timekeeperevolution.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.adamratana.timekeeperevolution.MyWatchFace;
import com.adamratana.timekeeperevolution.R;

public class OrreryComponent extends TimeComponent{

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

		topLeft = 32;
		size = 890;
		tmpBit = BitmapFactory.decodeResource(getResources(), R.drawable.month_ring);
		canvas.drawBitmap(tmpBit, null, new RectF(topLeft, topLeft, topLeft + size, topLeft + size), backgroundPaint);
	}
}
