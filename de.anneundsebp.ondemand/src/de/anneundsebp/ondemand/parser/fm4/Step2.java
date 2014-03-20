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
package de.anneundsebp.ondemand.parser.fm4;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.anneundsebp.ondemand.parser.Asset;
import de.anneundsebp.ondemand.parser.Category;
import de.anneundsebp.ondemand.parser.Step;
import de.anneundsebp.ondemand.parser.Util;

public class Step2 extends Step {
	
	Category category;

	@Override
	public void process(Map<String, String> context, Category category) {
		try {
			this.category = category;
			String page = Util.loadPage(category.url);
			parseCategories(page);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	List<Asset> parseCategories(String page) {
		this.assets = new ArrayList<Asset>();
		try {
			JSONArray array = new JSONArray(page);
			for (int i=0; i < array.length() ; i++) {
				JSONObject o = array.getJSONObject(i);
				String title = o.getString("title");
				String sub = this.category.url + "/" + o.getString("programKey");
				String subpage = Util.loadPage(sub);
				JSONObject program = new JSONObject(subpage);
				JSONArray streams = program.getJSONArray("streams");
				for (int streamCounter = 0; streamCounter < streams.length(); streamCounter++) {
					Asset a = new Asset();
					a.name = title;
					if (streams.length() > 1)
						a.name += " " + (streamCounter +1);
					a.url = "http://loopstream01.apa.at/?channel=fm4&id=" + streams.getJSONObject(streamCounter).getString("loopStreamId") + "&offset=0";
					assets.add(a);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return assets;
	}
}
