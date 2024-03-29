package com.adamratana.timekeeperevolution;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;

import com.adamratana.timekeeperevolution.config.Configs;
import com.adamratana.timekeeperevolution.engine.TimeKeeper;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn"t
 * shown. On devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient
 * mode. The watch face is drawn with less contrast in mute mode.
 * <p>
 * Important Note: Because watch face apps do not have a default Activity in
 * their project, you will need to set your Configurations to
 * "Do not launch Activity" for both the Wear and/or Application modules. If you
 * are unsure how to do this, please review the "Run Starter project" section
 * in the Google Watch Face Code Lab:
 * https://codelabs.developers.google.com/codelabs/watchface/index.html#0
 */
public class MyWatchFace extends CanvasWatchFaceService {

	/*
	 * Updates rate in milliseconds for interactive mode. We update once a second to advance the
	 * second hand.
	 */
	private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

	/**
	 * Handler message id for updating the time periodically in interactive mode.
	 */
	private static final int MSG_UPDATE_TIME = 0;

	@Override
	public Engine onCreateEngine() {
		return new Engine();
	}

	private static class EngineHandler extends Handler {
		private final WeakReference<MyWatchFace.Engine> mWeakReference;

		public EngineHandler(MyWatchFace.Engine reference) {
			mWeakReference = new WeakReference<>(reference);
		}

		@Override
		public void handleMessage(Message msg) {
			MyWatchFace.Engine engine = mWeakReference.get();
			if (engine != null) {
				switch (msg.what) {
					case MSG_UPDATE_TIME:
						engine.handleUpdateTimeMessage();
						break;
				}
			}
		}
	}

	public class Engine extends CanvasWatchFaceService.Engine {
		/* Handler to update the time once a second in interactive mode. */
		private final Handler mUpdateTimeHandler = new EngineHandler(this);
		public Calendar mCalendar;
		public Configs configs;
		private final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mCalendar.setTimeZone(TimeZone.getDefault());
				invalidate();
			}
		};
		private final TimeKeeper timeKeeper = new TimeKeeper(this);
		private boolean mRegisteredTimeZoneReceiver = false;
		private boolean mMuteMode;
		/* Colors for all hands (hour, minute, seconds, ticks) based on photo loaded. */
		public boolean mAmbient;
		private boolean mLowBitAmbient;
		private boolean mBurnInProtection;
		public Context context;

		@Override
		public void onCreate(SurfaceHolder holder) {
			super.onCreate(holder);

			setWatchFaceStyle(new WatchFaceStyle.Builder(MyWatchFace.this)
					.setAcceptsTapEvents(true)
					.build());

			this.context = getApplicationContext();

			configs = Configs.instance(context);

			mCalendar = Calendar.getInstance();

			timeKeeper.onCreate();
		}

		@Override
		public void onDestroy() {
			mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
			super.onDestroy();
		}

		@Override
		public void onPropertiesChanged(Bundle properties) {
			super.onPropertiesChanged(properties);
			mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
			mBurnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);
		}

		@Override
		public void onTimeTick() {
			super.onTimeTick();
			invalidate();
		}

		@Override
		public void onAmbientModeChanged(boolean inAmbientMode) {
			super.onAmbientModeChanged(inAmbientMode);
			mAmbient = inAmbientMode;

			timeKeeper.updateWatchHandStyle();

			/* Check and trigger whether or not timer should be running (only in active mode). */
			updateTimer();
		}

		@Override
		public void onInterruptionFilterChanged(int interruptionFilter) {
			super.onInterruptionFilterChanged(interruptionFilter);
			boolean inMuteMode = (interruptionFilter == WatchFaceService.INTERRUPTION_FILTER_NONE);

			/* Dim display in mute mode. */
			if (mMuteMode != inMuteMode) {
				mMuteMode = inMuteMode;
				timeKeeper.onMuteMode(mMuteMode);
				invalidate();
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			timeKeeper.onSurfaceChanged(holder, format, width, height);
		}

		/**
		 * Captures tap event (and tap type). The {@link WatchFaceService#TAP_TYPE_TAP} case can be
		 * used for implementing specific logic to handle the gesture.
		 */
		@Override
		public void onTapCommand(int tapType, int x, int y, long eventTime) {
			switch (tapType) {
				case TAP_TYPE_TOUCH:
					// The user has started touching the screen.
					break;
				case TAP_TYPE_TOUCH_CANCEL:
					// The user has started a different gesture or otherwise cancelled the tap.
					break;
				case TAP_TYPE_TAP:
					// The user has completed the tap gesture.
					break;
			}
			invalidate();
		}

		@Override
		public void onDraw(Canvas canvas, Rect bounds) {
			long now = System.currentTimeMillis();
			mCalendar.setTimeInMillis(now);

			drawBackground(canvas);
			drawWatchFace(canvas);
		}

		private void drawBackground(Canvas canvas) {
			if (mAmbient && (mLowBitAmbient || mBurnInProtection)) {
				canvas.drawColor(Color.BLACK);
			} else {
				timeKeeper.drawBackground(canvas, mAmbient);
			}
		}

		private void drawWatchFace(Canvas canvas) {
			timeKeeper.drawWatchFace(canvas);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);

			if (visible) {
				registerReceiver();
				/* Update time zone in case it changed while we weren't visible. */
				mCalendar.setTimeZone(TimeZone.getDefault());
				timeKeeper.invalidate();
				invalidate();
			} else {
				unregisterReceiver();
			}

			/* Check and trigger whether or not timer should be running (only in active mode). */
			updateTimer();
		}

		private void registerReceiver() {
			if (mRegisteredTimeZoneReceiver) {
				return;
			}
			mRegisteredTimeZoneReceiver = true;
			IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
			MyWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
		}

		private void unregisterReceiver() {
			if (!mRegisteredTimeZoneReceiver) {
				return;
			}
			mRegisteredTimeZoneReceiver = false;
			MyWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
		}

		/**
		 * Starts/stops the {@link #mUpdateTimeHandler} timer based on the state of the watch face.
		 */
		private void updateTimer() {
			mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
			if (shouldTimerBeRunning()) {
				mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
			}
		}

		/**
		 * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer
		 * should only run in active mode.
		 */
		private boolean shouldTimerBeRunning() {
			return isVisible() && !mAmbient;
		}

		/**
		 * Handle updating the time periodically in interactive mode.
		 */
		private void handleUpdateTimeMessage() {
			invalidate();
			if (shouldTimerBeRunning()) {
				long timeMs = System.currentTimeMillis();
				long delayMs = INTERACTIVE_UPDATE_RATE_MS
						- (timeMs % INTERACTIVE_UPDATE_RATE_MS);
				mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
			}
		}
	}
}