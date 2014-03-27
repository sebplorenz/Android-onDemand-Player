/**
 * onDemand Player
 * Copyright (C) 2014 Sebastian Lorenz
 *
 * onDemand Player is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * onDemand Player is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import de.anneundsebp.ondemand.parser.dradiowissen.DRadioWissenProcessor;
import de.anneundsebp.ondemand.parser.fm4.FM4Processor;

public class MainActivity extends ListActivity implements MediaPlayerControl,
		OnClickListener, StepHandler, MediaListener, OnBufferingUpdateListener,
		OnCompletionListener {

	Processor currentProcessor;
	List<Object> results;
	CatalogAssetArrayAdapter adapter;
	MediaController mcontroller;
	int bufferPercentage = 0;
	int playingPosition = -1;
	List<Object> currentlyPlayingResults = null;

	ProgressDialog mProgressDialog;
	private Handler handler = new Handler();

	static Processor[] processors;

	static {
		processors = new Processor[] { new FM4Processor(), new DRadioWissenProcessor() };
	}

	@Override
	public void onBackPressed() {
		if (currentProcessor != null && currentProcessor.currentStep > 0)
			new StepBackwardTask(this).execute(currentProcessor);
		else if (currentProcessor != null && currentProcessor.currentStep == 0) {
			currentProcessor.currentStep--;
			currentProcessor = null;
			stepUpdate(null);
//			((ImageButton) findViewById(R.id.imageButton_channelLogo))... channel logo setzen
		} else
			super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		results = new ArrayList<Object>();
		adapter = new CatalogAssetArrayAdapter(this, results);
		this.setListAdapter(adapter);
		
		stepUpdate(null);

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
			((TextView) findViewById(R.id.textViewPlayingSub))
					.setText("Now Playing");
			((TextView) findViewById(R.id.textViewPlayingSuper))
					.setText(MediaPlayerService.asset.name);
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
		// stopService(new Intent(this, MediaPlayerService.class));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object o = results.get(position);
		if (o instanceof Processor) {
			currentProcessor = (Processor) o;
			showProcessDialog("Loading " + currentProcessor.name);
			((ImageButton) findViewById(R.id.imageButton_channelLogo))
					.setContentDescription(currentProcessor.name);
			DownloadChannelLogoTask t = new DownloadChannelLogoTask(this);
			t.execute(currentProcessor.channelLogoUrl);
			StepForwardTask forward = new StepForwardTask(this);
			forward.execute(currentProcessor);
		} else if (o instanceof Category) {
			Category c = (Category) o;
			showProcessDialog("Loading " + c.name);
			adapter.clear();
			StepForwardTask forward = new StepForwardTask(this);
			forward.setNextCategory(c);
			forward.execute(currentProcessor);
		}
	}
	
	void showProcessDialog(String message) {
		mProgressDialog = new ProgressDialog(MainActivity.this);
		mProgressDialog.setMessage(message);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
	}

	void play(Asset asset) {
		((TextView) findViewById(R.id.textViewPlayingSub))
				.setText("Connecting...");
		((TextView) findViewById(R.id.textViewPlayingSuper))
				.setText(asset.name);
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
		// if (v.getId() == R.id.downloadButton)
		// download((Asset) results.get(((Integer) v.getTag()).intValue()));
		// else
		if (v.getId() == R.id.playButton) {
			playingPosition = ((Integer) v.getTag()).intValue();
			currentlyPlayingResults = results;
			play((Asset) results.get(playingPosition));
		}
		// else if (v.getId() == R.id.imageButton_channelLogo)
		// todo show web site
		// if (mcontroller != null)
		// mcontroller.show();
		else if (v.getId() == R.id.textViewPlayingSub
				|| v.getId() == R.id.textViewPlayingSuper)
			if (mcontroller != null)
				mcontroller.show();

	}

	@Override
	public void stepUpdate(Step step) {
		if (this.mProgressDialog != null && this.mProgressDialog.isShowing())
			this.mProgressDialog.dismiss();
		adapter.clear();
		if (step == null)
			for (Processor processor : processors)
				adapter.add(processor);
		else {
			if (!(step.categories == null || step.categories.isEmpty()))
				for (Category category : step.categories)
					adapter.add(category);
			if (!(step.assets == null || step.assets.isEmpty()))
				for (Asset asset : step.assets)
					adapter.add(asset);
		}
	}

	@Override
	public void startPlaying() {
		((TextView) findViewById(R.id.textViewPlayingSub))
				.setText("Now Playing");
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
		if (currentlyPlayingResults != null
				&& playingPosition < currentlyPlayingResults.size() - 1) {
			Object o = currentlyPlayingResults.get(playingPosition + 1);
			if (o instanceof Asset) {
				playingPosition++;
				play((Asset) o);
			}
		}
	}
	
	@Override
	protected void onPause() {
		if (this.mProgressDialog != null && this.mProgressDialog.isShowing())
			this.mProgressDialog.dismiss();
		super.onPause();
	}

}
