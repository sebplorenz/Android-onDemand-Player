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
