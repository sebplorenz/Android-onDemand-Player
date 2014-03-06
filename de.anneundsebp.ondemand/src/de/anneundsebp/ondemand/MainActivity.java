package de.anneundsebp.ondemand;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import de.anneundsebp.ondemand.parser.Asset;
import de.anneundsebp.ondemand.parser.Category;
import de.anneundsebp.ondemand.parser.Processor;
import de.anneundsebp.ondemand.parser.Step;
import de.anneundsebp.ondemand.parser.fm4.FM4Processor;

public class MainActivity extends ListActivity implements MediaPlayerControl,
		OnClickListener, StepHandler, MediaListener, OnBufferingUpdateListener, OnCompletionListener {

	Processor p = new FM4Processor();
	List<Object> results;
	CatalogAssetArrayAdapter adapter;
	MediaController mcontroller;
	int bufferPercentage = 0;
	int playingPosition = -1;
	List<Object> currentlyPlayingResults = null;
	
	ProgressDialog mProgressDialog;
	private Handler handler = new Handler();

	@Override
	public void onBackPressed() {
		if (p.currentStep > 0) {
			StepBackwardTask backward = new StepBackwardTask(this);
			backward.execute(p);
		} else
			super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((ImageButton) findViewById(R.id.imageButton_channelLogo))
				.setContentDescription(p.name);
		DownloadChannelLogoTask t = new DownloadChannelLogoTask(this);
		t.execute(p.channelLogoUrl);
		StepForwardTask forward = new StepForwardTask(this);
		forward.execute(p);

		results = new ArrayList<Object>();
		adapter = new CatalogAssetArrayAdapter(this, results);
		this.setListAdapter(adapter);

		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Downloading...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		
		MediaPlayerService.mediaListener = this;
		
		if (MediaPlayerService.mp != null) {
			if (mcontroller == null) {
				mcontroller = new MediaController(this, false);
				mcontroller.setAnchorView(findViewById(R.id.layoutPlaying));
				mcontroller.setMediaPlayer(this);
				MediaPlayerService.mp.setOnBufferingUpdateListener(this);
				MediaPlayerService.mp.setOnCompletionListener(this);
			}
			((TextView)findViewById(R.id.textViewPlayingSub)).setText("Now Playing");
			((TextView)findViewById(R.id.textViewPlayingSuper)).setText(MediaPlayerService.asset.name);
		}
		
		findViewById(R.id.imageButton_channelLogo).setOnClickListener(this);
		findViewById(R.id.layoutPlaying).setOnClickListener(this);
		findViewById(R.id.textViewPlayingSuper).setOnClickListener(this);
		findViewById(R.id.textViewPlayingSub).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_exit: {
			stopService(new Intent(this, MediaPlayerService.class));
			finish();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
//		stopService(new Intent(this, MediaPlayerService.class));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object o = results.get(position);
		if (o instanceof Category) {
			adapter.clear();
			StepForwardTask forward = new StepForwardTask(this);
			forward.setNextCategory((Category) o);
			forward.execute(p);
		}
	}

	void play(Asset asset) {
		((TextView)findViewById(R.id.textViewPlayingSub)).setText("Connecting...");
		((TextView)findViewById(R.id.textViewPlayingSuper)).setText(asset.name);
		bufferPercentage = 0;
		if (mcontroller != null)
			mcontroller.setEnabled(false);
		MediaPlayerService.asset = asset;
		startService(new Intent(MediaPlayerService.ACTION_PLAY, null, this,
				MediaPlayerService.class));
	}

	void download(Asset asset) {
		final DownloadTask2 downloadTask = new DownloadTask2(this);
		mProgressDialog.setMessage("Downloading " + asset.name);
		downloadTask.mProgressDialog = mProgressDialog;
		downloadTask.execute(asset);

		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						downloadTask.cancel(true);
					}
				});
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getAudioSessionId() {
		return MediaPlayerService.mp.getAudioSessionId();
	}

	@Override
	public int getBufferPercentage() {
		return bufferPercentage;
	}

	@Override
	public int getCurrentPosition() {
		return MediaPlayerService.mp.getCurrentPosition();
	}

	@Override
	public int getDuration() {
		return MediaPlayerService.mp.getDuration();
	}

	@Override
	public boolean isPlaying() {
		return MediaPlayerService.mp.isPlaying();
	}

	@Override
	public void pause() {
		MediaPlayerService.mp.pause();
	}

	@Override
	public void seekTo(int pos) {
		MediaPlayerService.mp.seekTo(pos);
	}

	@Override
	public void start() {
		MediaPlayerService.mp.start();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.downloadButton)
			download((Asset) results.get(((Integer) v.getTag()).intValue()));
		else 
			if (v.getId() == R.id.playButton) {
			playingPosition = ((Integer) v.getTag()).intValue();
			currentlyPlayingResults = results;
			play((Asset) results.get(playingPosition));
		} 
//		else if (v.getId() == R.id.imageButton_channelLogo)
			// todo show web site
//			if (mcontroller != null)
//				mcontroller.show();
		else if (v.getId() == R.id.textViewPlayingSub || v.getId() == R.id.textViewPlayingSuper)
			if (mcontroller != null)
				mcontroller.show();
			
	}

	@Override
	public void stepUpdate(Step step) {
		adapter.clear();
		if (!step.categories.isEmpty())
			for (Category c : step.categories)
				adapter.add(c);
		if (!step.assets.isEmpty())
			for (Asset a : step.assets)
				adapter.add(a);
	}

	@Override
	public void startPlaying() {
		((TextView)findViewById(R.id.textViewPlayingSub)).setText("Now Playing");
		if (mcontroller == null) {
			mcontroller = new MediaController(this, false);
			mcontroller.setAnchorView(findViewById(R.id.layoutPlaying));
			mcontroller.setMediaPlayer(this);
			MediaPlayerService.mp.setOnBufferingUpdateListener(this);
			MediaPlayerService.mp.setOnCompletionListener(this);
		}
		handler.post(new Runnable() {
            public void run() {
                mcontroller.setEnabled(true);
                mcontroller.show();
            }
        });
	}
	
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		bufferPercentage = percent;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (currentlyPlayingResults != null && playingPosition < currentlyPlayingResults.size()-1) {
			Object o = currentlyPlayingResults.get(playingPosition+1);
			if (o instanceof Asset) {
				playingPosition++;
				play((Asset)o);
			}
		}
	}

}
