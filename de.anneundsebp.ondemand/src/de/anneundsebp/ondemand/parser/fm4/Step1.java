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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.anneundsebp.ondemand.parser.Category;
import de.anneundsebp.ondemand.parser.Step;
import de.anneundsebp.ondemand.parser.Util;

public class Step1 extends Step {

	static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);

	List<Category> getCategories() {
		try {
			String page = Util
					.loadPage("http://audioapi.orf.at/fm4/json/2.0/broadcasts");
			return parseCategories(page);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	List<Category> getPodcasts() {
		return null;
	}

	List<Category> parseCategories(String page) {
		this.categories = new ArrayList<Category>();
		try {
			JSONArray array = new JSONArray(page);
			for (int i = array.length() - 1; i >= 0; i--) {
				Category c = new Category();
				JSONObject o = array.getJSONObject(i);
				c.name = dateFormat.format(new Date(Long.parseLong(o
						.getString("date"))));
				c.url = "http://audioapi.orf.at/fm4/json/2.0/broadcasts/"
						+ o.getString("day");
				c.nextStep = Step2.class.getName();
				categories.add(c);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return categories;
	}

	boolean contains(List<Category> cats, String title) {
		for (Category cat : cats)
			if (cat.name.equals(title))
				return true;
		return false;
	}

	@Override
	public void process(Map<String, String> context, Category category) {
		if (categories == null) {
			if (category.url.equals("d"))
				getCategories();
			else if (category.url.equals("p"))
				getPodcasts();
		}
	}
}
