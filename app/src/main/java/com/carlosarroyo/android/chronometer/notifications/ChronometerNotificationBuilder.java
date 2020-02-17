/*
 * Copyright 2020 Carlos Alberto Arroyo Martínez.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.carlosarroyo.android.chronometer.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.carlosarroyo.android.chronometer.App;
import com.carlosarroyo.android.chronometer.activities.MainActivity;
import com.carlosarroyo.android.chronometer.R;

/**
 * @author Carlos Alberto Arroyo Martinez <carlosarroyoam@gmail.com>
 */
public class ChronometerNotificationBuilder {

	public static final int NOTIFICATION_ID = 12887952;
	private static final String ACTION_PLAY_PAUSE = "com.carlosarroyoam.android.chronometer.ACTION.PLAY_PAUSE";
	private static final String ACTION_RESET = "com.carlosarroyoam.android.chronometer.ACTION.RESET";

	public static Notification build(Context context, String notificationBody) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		Intent playOrPauseIntent = new Intent();
		playOrPauseIntent.setAction(ACTION_PLAY_PAUSE);
		PendingIntent playOrPausePendingIntent = PendingIntent.getBroadcast(context, 0, playOrPauseIntent, 0);

		Intent resetIntent = new Intent();
		resetIntent.setAction(ACTION_RESET);
		PendingIntent resetPendingIntent = PendingIntent.getBroadcast(context, 0, resetIntent, 0);

		return new Notification.Builder(context, App.CHRONOMETER_CHANNEL_ID)
//				.setContentTitle(getText(R.string.chronometer_notification_title))
				.setContentText(notificationBody)
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setColor(context.getResources().getColor(R.color.colorPrimary, null))
				.setContentIntent(pendingIntent)
				.build();
	}
}
