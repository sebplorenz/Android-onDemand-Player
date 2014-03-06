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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import de.anneundsebp.ondemand.parser.Asset;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

public class DownloadTask extends AsyncTask<Asset, Integer, Object>{
	
	DownloadManager manager;
	
	public DownloadTask(DownloadManager manager) {
		this.manager = manager;
	}

	@Override
	protected Object doInBackground(Asset... params) {
		Asset asset = params[0];

		URL url;
		int length = -1;
		try {
			url = new URL(asset.url);
			HttpURLConnection urlConnection = null;
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			length = urlConnection.getContentLength();
			Map<String, List<String>> headers = urlConnection.getHeaderFields();
			System.out.println(headers);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(asset.url));
		request.setDescription("");
		request.setTitle(asset.name);
//		request.setMimeType(media);
		// in order for this if to run, you must use the android 3.2 to compile your app
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//		    request.allowScanningByMediaScanner();
//		    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//		}
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "test");

		// get download service and enqueue file
		
		request.addRequestHeader("Range", "bytes=" + Integer.toString(length));
		manager.enqueue(request);
		return null;
	}

}
