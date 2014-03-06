package de.anneundsebp.ondemand;

import de.anneundsebp.ondemand.parser.Asset;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

public class PlayTask extends AsyncTask<Asset, Integer, String> {

	@Override
	protected String doInBackground(Asset... params) {
		new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(params[0].url), "audio/*");
		return null;
	}

}
