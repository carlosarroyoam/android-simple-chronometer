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
import android.os.IBinder;
import android.util.Log;

import com.carlosarroyo.android.chronometer.chronometer.ChronometerEngine;
<<<<<<< HEAD
import com.carlosarroyo.android.chronometer.notifications.ChronometerNotificationBuilder;
=======
import com.carlosarroyo.android.chronometer.Notifications.ChronometerNotificationBuilder;
>>>>>>> origin/master

/**
 * @author Carlos Alberto Arroyo Martinez <carlosarroyoam@gmail.com>
 */
public class ChronometerService extends Service {

	private static final String TAG = ChronometerService.class.getSimpleName();
	private final IBinder mBinder = new LocalBinder();
	private ChronometerEngine mChronometerEngine;

	/**
	 *
	 */
	public class LocalBinder extends Binder {
		public ChronometerService getService() {
			return ChronometerService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mChronometerEngine = new ChronometerEngine();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand: ");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mChronometerEngine = null;
		Log.d(TAG, "onDestroy: ");
	}

	/* Client methods */
	public void playPauseChronometer() {
		mChronometerEngine.tooglePlayPause();
	}

	public void addLap() {
		mChronometerEngine.addLap();
	}

	public void resetChronometer() {
		mChronometerEngine.reset();
		stopForeground(STOP_FOREGROUND_REMOVE);
	}

	public String getFullTime() {
		return mChronometerEngine.getFullTime();
	}

	public String getMillisecondsTime() {
		return mChronometerEngine.getMillisecondsTime();
	}

	public boolean isRunning() {
		return mChronometerEngine.isRunning();
	}

	public void updateNotification() {
		if (mChronometerEngine.isStarted()) {
			startForeground(ChronometerNotificationBuilder.NOTIFICATION_ID, ChronometerNotificationBuilder.build(this, mChronometerEngine.getFullTime()));
		}
	}

}
