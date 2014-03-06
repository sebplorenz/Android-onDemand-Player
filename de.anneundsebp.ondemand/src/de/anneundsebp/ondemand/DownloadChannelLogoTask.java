package de.anneundsebp.ondemand;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

public class DownloadChannelLogoTask extends AsyncTask<String, Integer, Bitmap> {

	Activity context;

	public DownloadChannelLogoTask(Activity context) {
		this.context = context;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		try {
			URL url = new URL(params[0]);

			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setDoInput(true);
			c.connect();
			InputStream is = c.getInputStream();
			Bitmap img;
			img = BitmapFactory.decodeStream(is);
			return img;
		} catch (MalformedURLException e) {
			Log.d("RemoteImageHandler", "fetchImage passed invalid URL: "
					+ params[0]);
		} catch (IOException e) {
			Log.d("RemoteImageHandler", "fetchImage IO exception: " + e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			result = Bitmap.createScaledBitmap(result,
					(int) (result.getWidth() * 0.4),
					(int) (result.getHeight() * 0.4), true);
			((ImageButton) context.findViewById(R.id.imageButton_channelLogo))
					.setImageBitmap(result);
		}
	}

}
