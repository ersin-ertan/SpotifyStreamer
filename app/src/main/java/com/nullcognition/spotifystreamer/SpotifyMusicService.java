package com.nullcognition.spotifystreamer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

public class SpotifyMusicService extends Service{
	public SpotifyMusicService(){
	}

	private static final int ONGOING_NOTIFICATION_ID = 1;

	int mStartMode;       // indicates how to behave if the service is killed
	IBinder mBinder;      // interface for clients that bind
	boolean mAllowRebind; // indicates whether onRebind should be used


	@Override
	public int onStartCommand(Intent intent, int flags, int startId){

		buildNotification();
//		CharSequence charSequence1 = "notification event and message";
//
//		Notification notification = new Notification(android.R.drawable.sym_def_app_icon, charSequence1, System.currentTimeMillis());
//		Intent notificationIntent = new Intent(this, MediaPlayerActivity.class);
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//		CharSequence charSequence = "notification event and message";
//		notification.setLatestEventInfo(this, charSequence, charSequence, pendingIntent);
//		startForeground(ONGOING_NOTIFICATION_ID, notification);

		// The service is starting, due to a call to startService()
		return mStartMode;
	}
	private void buildNotification(){
		NotificationCompat.Builder mBuilder =
				(NotificationCompat.Builder) new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.abc_btn_radio_material)
						.setContentTitle("My notification")
						.setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
//		Intent resultIntent = new Intent(this, MediaPlayerActivity.class);
//
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//		stackBuilder.addParentStack(ArtistTop10Activity.class);
//
//		stackBuilder.addNextIntent(resultIntent);
//
//		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(ONGOING_NOTIFICATION_ID, mBuilder.build());
	}

	@Override
	public void onCreate(){ }

	@Override
	public void onDestroy(){ }
	@Nullable
	@Override
	public IBinder onBind(final Intent intent){
		return null;
	}
}
