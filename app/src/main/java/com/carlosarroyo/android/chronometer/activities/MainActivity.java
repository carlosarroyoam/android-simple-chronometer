/*
 * Copyright 2020 Carlos Alberto Arroyo Martínez.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.carlosarroyo.android.chronometer.activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.carlosarroyo.android.chronometer.R;
import com.carlosarroyo.android.chronometer.services.ChronometerService;

/**
 * @author Carlos Alberto Arroyo Martinez <carlosarroyoam@gmail.com>
 */
public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private ChronometerService mChronometerService;
	private boolean mBound = false;
	private AlphaAnimation buttonClickAnimation = new AlphaAnimation(1F, 0.8F);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate: ");
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart: ");
		Intent intent = new Intent(this, ChronometerService.class);
		startService(intent);
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause: ");
		showNotification();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop: ");
		showNotification();
		unbindService(mServiceConnection);
		mBound = false;
	}

	public void onClickStart(View view) {
		if (mBound) {
			mChronometerService.playPauseChronometer();
			updateIU();
			toogleStartStopIcon();
		}
	}

	private void toogleStartStopIcon() {
		if (mBound) {
			final Button startStopButton = findViewById(R.id.btn_start_stop);
			final TextView startStopTextView = findViewById(R.id.txt_start_stop);

			if (mChronometerService.isRunning()) {
				startStopButton.setBackgroundResource(R.drawable.ic_pause);
				startStopTextView.setText(R.string.button_pause_text);
			} else {
				startStopButton.setBackgroundResource(R.drawable.ic_play);
				startStopTextView.setText(R.string.button_start_text);
			}
		}
	}

	public void onClickSplit(View view) {
		if (mBound) {
			mChronometerService.addLap();
			view.startAnimation(buttonClickAnimation);
		}
	}

	public void onClickReset(View view) {
		if (mBound) {
			mChronometerService.resetChronometer();
			view.startAnimation(buttonClickAnimation);
		}
	}

	private void updateIU() {
		final TextView timeEditText = findViewById(R.id.txt_seconds);
		final TextView millisEditText = findViewById(R.id.txt_millis);

		final Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				timeEditText.setText(mChronometerService.getFullTime());
				millisEditText.setText(mChronometerService.getMillisecondsTime());
				handler.postDelayed(this, 0);
			}
		});
	}

	private void showNotification() {
		if (mBound) {
			mChronometerService.updateNotification();
		}
	}

	/**
	 * Defines callbacks for service binding, passed to bindService()
	 */
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			ChronometerService.LocalBinder binder = (ChronometerService.LocalBinder) service;
			mChronometerService = binder.getService();
			mBound = true;
			mChronometerService.stopForeground(Service.STOP_FOREGROUND_REMOVE);
			updateIU();
			toogleStartStopIcon();
			Log.d(TAG, "onServiceConnected: ");
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
			Log.d(TAG, "onServiceDisconnected: ");
		}
	};
}
