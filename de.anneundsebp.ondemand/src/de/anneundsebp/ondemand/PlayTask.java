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
