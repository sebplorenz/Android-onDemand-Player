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
			String page = Util.loadPage("http://fm4.orf.at/" + category.url);
			getProgrammeStreamURL(page);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	List<Asset> getProgrammeStreamURL(String page) {
		int pos;
		int start = 0;
		assets = new ArrayList<Asset>();
		while ((pos = page.indexOf("data-id", start)) != -1) {
			start = pos + 1;
			String dataId = page
					.substring(pos + 9, page.indexOf("\"", pos + 9));
			String apiurl = "http://audioapi.orf.at/fm4/json/2.0/playlist/"
					+ dataId;
			try {
				String json = Util.loadPage(apiurl);
				int jstart = 0;
				while ((pos = json.indexOf("loopStreamId\":", jstart)) != -1) {
					jstart = pos +1;
					Asset a = new Asset();
					int end = json.indexOf("\"", pos+15);
					int num = json.indexOf("_", pos+25);
					num = json.indexOf("_", num+1);
					num = json.indexOf("_", num+1);
					num = json.indexOf("_", num+1);
					num = json.indexOf("_", num+1);
					a.name = category.name + "_" + json.substring(pos + 15, pos + 26)
							+ json.substring(num-1, num) 
							+ json.substring(end-4, end);
//							json.indexOf("\"", pos + 15));
					a.url = "http://loopstream01.apa.at/?channel=fm4&ua=flash&id="
							+ json.substring(pos + 15, json.indexOf("\"", pos + 15));
					assets.add(a);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return assets;
	}
}
