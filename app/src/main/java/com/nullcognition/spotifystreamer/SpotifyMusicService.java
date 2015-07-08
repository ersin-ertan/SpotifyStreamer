package com.nullcognition.spotifystreamer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class SpotifyMusicService extends Service{
	private NotificationManager mNM;

	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private int NOTIFICATION = R.string.local_service_started;

	/**
	 Class for clients to access.  Because we know this service always
	 runs in the same process as its clients, we don't need to deal with
	 IPC.
	 */
	public class LocalBinder extends Binder{
		SpotifyMusicService getService(){
			return SpotifyMusicService.this;
		}
	}

	@Override
	public void onCreate(){
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		showNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i("SpotifyMusicService", "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy(){
		// Cancel the persistent notification.
		mNM.cancel(NOTIFICATION);

		Toast.makeText(this, "service stopped", Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent){
		return mBinder;
	}

	// This is the object that receives interactions from clients.  See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder();

	/**
	 Show a notification while this service is running.
	 */
	private void showNotification(){
		// In this sample, we'll use the same text for the ticker and the expanded notification
		CharSequence text = getText(R.string.local_service_started);

		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MediaPlayerActivity.class), 0);

		// Set the icon, scrolling text and timestamp
		Notification notification = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha)
				.setContentTitle("ContentTitle")
				.setContentIntent(contentIntent)
				.setContentInfo("contentInfo")
				.setTicker("ticker")
				.build();


		// Send the notification.
		mNM.notify(NOTIFICATION, notification);
	}
}
