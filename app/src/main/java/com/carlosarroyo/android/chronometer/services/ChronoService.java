/*
 * Copyright 2020 Carlos Alberto Arroyo Martínez.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.carlosarroyo.android.chronometer.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Locale;

/**
 * @author Carlos Alberto Arroyo Martinez <carlosarroyoam@gmail.com>
 */
public class ChronoService extends Service {

	private static final String TAG = ChronoService.class.getSimpleName();
	private final IBinder binder = new LocalBinder();

	final Handler mHandler = new Handler();

	/* Chronos variables */
	private long mStartTime, mTimeMinusStart, mTimeWhenPaused, mCurrentMilliseconds;
	private int mHours, mMinutes, mSeconds, mMilliseconds;


	private String mFullTimeString, mMillisString;

	/**
	 *
	 */
	Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mTimeMinusStart = SystemClock.uptimeMillis() - mStartTime;
			mCurrentMilliseconds = mTimeWhenPaused + mTimeMinusStart;

			mMilliseconds = (int) ((mCurrentMilliseconds % 1000) / 10);
			mSeconds = (int) (mCurrentMilliseconds / 1000);
			mMinutes = mSeconds / 60;
			mHours = mMinutes / 60;
			mSeconds = mSeconds % 60;

			String hoursString = mHours > 0 ? String.format(Locale.US, "%d:", mHours) : "";
			String minutesString = mMinutes > 0 ? String.format(Locale.US, "%02d:", mMinutes) : "";
			String secondsString = String.format(Locale.US, "%02d", mSeconds);

			mFullTimeString = hoursString + minutesString + secondsString;
			mMillisString = String.format(Locale.US, "%02d", mMilliseconds);

			mHandler.postDelayed(this, 0);
		}
	};

	/**
	 *
	 */
	public class LocalBinder extends Binder {
		public ChronoService getService() {
			return ChronoService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand: ");
		return super.onStartCommand(intent, flags, startId);
	}

	/* Client methods */
	public void start() {
		mStartTime = SystemClock.uptimeMillis();
		mHandler.postDelayed(mRunnable, 0);
	}

	public void pause() {
		mTimeWhenPaused += mTimeMinusStart;
		mHandler.removeCallbacks(mRunnable);
	}

	public void reset() {
		mStartTime = 0;
		mTimeWhenPaused = 0;
		mTimeMinusStart = 0;
		mCurrentMilliseconds = 0;
		mHours = 0;
		mMinutes = 0;
		mSeconds = 0;
		mMilliseconds = 0;

		mFullTimeString = "00";
		mMillisString = "00";

		mHandler.removeCallbacks(mRunnable);
	}

	public String getFullTime() {
		return mFullTimeString;
	}

	public String getMillisecondsTime() {
		return mMillisString;
	}

}
