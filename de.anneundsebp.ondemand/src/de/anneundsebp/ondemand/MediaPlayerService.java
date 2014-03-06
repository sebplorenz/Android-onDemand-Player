package de.anneundsebp.ondemand;

import java.io.IOException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.os.PowerManager;
import de.anneundsebp.ondemand.parser.Asset;

public class MediaPlayerService extends Service implements OnPreparedListener {

	public static final String ACTION_PLAY = "com.example.com.action.PLAY";
	public static final String ACTION_STOP = "com.example.com.action.STOP";

	static MediaPlayer mp = null;
	WifiLock wifiLock;
	
	public static Asset asset;
	
	public static MediaListener mediaListener;

	public void stopPlaying() {
		if (mp != null) {
			if (mp.isPlaying())
				mp.stop();
			mp.release();
			mp = null;
		}
		if (wifiLock != null)
			wifiLock.release();
		stopForeground(true);
	}

	public void startPlaying(Asset asset) {
		if (mp == null)
			mp = new MediaPlayer();
		else {
			try {
				if (mp.isPlaying())
					mp.stop();
			} catch (IllegalStateException e) {
				// do nothing
			}
			mp.reset();
		}
		try {
			mp.setDataSource(asset.url);
			mp.setOnPreparedListener(this);
			mp.prepareAsync();
			mp.setWakeMode(getApplicationContext(),
					PowerManager.PARTIAL_WAKE_LOCK);
			if (wifiLock == null) {
				wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
						.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
				wifiLock.acquire();
			}
			PendingIntent pi = PendingIntent.getActivity(
					getApplicationContext(), 0, new Intent(
							getApplicationContext(), MainActivity.class),
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			Notification notification = new Notification();
			notification.tickerText = "FM4 onDemand Player";
			notification.icon = R.drawable.launcher;
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			notification.setLatestEventInfo(getApplicationContext(),
					"FM4 onDemand Player", "Playing: " + asset.name, pi);
			
			startForeground(123, notification);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		if (mediaListener != null)
			mediaListener.startPlaying();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int i = super.onStartCommand(intent, flags, startId);
		if (intent.getAction().equals(ACTION_PLAY)) {
//			startPlaying(intent.getDataString());
			startPlaying(asset);
		}

		if (intent.getAction().equals(ACTION_STOP)) {
			stopPlaying();
		}

		return i;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopPlaying();
	}
	
}
