/*
 * Copyright 2019 Carlos Alberto Arroyo Martínez.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.carlosarroyo.android.chronometer;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.carlosarroyo.android.chronometer.services.ChronoService;

/**
 * @author Carlos Alberto Arroyo Martinez <carlosarroyoam@gmail.com>
 */
public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private ChronoService mChronoService;
	private boolean mBound = false;
	private AlphaAnimation buttonClickAnimation = new AlphaAnimation(1F, 0.6F);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = new Intent(this, ChronoService.class);
		startService(intent);
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(mServiceConnection);
		mBound = false;
	}

	public void onClickStart(View view) {
		if (mBound) {
			mChronoService.start();
			updateIU();
			view.startAnimation(buttonClickAnimation);
		}
	}

	public void onClickPause(View view) {
		if (mBound) {
			mChronoService.pause();
			view.startAnimation(buttonClickAnimation);
		}
	}

	public void onClickReset(View view) {
		if (mBound) {
			mChronoService.reset();
			view.startAnimation(buttonClickAnimation);
		}
	}

	public void updateIU() {
		final TextView timeEditText = findViewById(R.id.txt_seconds);
		final TextView millisEditText = findViewById(R.id.txt_millis);

		final Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				timeEditText.setText(mChronoService.getFullTime());
				millisEditText.setText(mChronoService.getMillisecondsTime());
				handler.postDelayed(this, 0);
			}
		});
	}

	/**
	 * Defines callbacks for service binding, passed to bindService()
	 */
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			ChronoService.LocalBinder binder = (ChronoService.LocalBinder) service;
			mChronoService = binder.getService();
			mBound = true;
			updateIU();
			Log.d(TAG, "onServiceConnected: ");
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
			Log.d(TAG, "onServiceDisconnected: ");
		}
	};
}
