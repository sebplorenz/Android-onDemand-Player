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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.anneundsebp.ondemand.parser.Category;
import de.anneundsebp.ondemand.parser.Step;
import de.anneundsebp.ondemand.parser.Util;


public class Step1 extends Step {

	List<Category> getCategories() {
		try {
			String page = Util
					.loadPage("http://fm4.orf.at/radio/stories/sendeschema");
			return parseCategories(page);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	String removeComments(String page) {
		int start;
		while ((start = page.indexOf("<!--")) != -1) {
			int end = page.indexOf("-->", start);
			String pre = page.substring(0, start);
			page = pre + page.substring(end + 3, page.length());
		}
		return page;
	}

	List<Category> parseCategories(String page) {
		page = removeComments(page);
		Pattern p = Pattern.compile("<area.*?\\/>");
		categories = new ArrayList<Category>();
		Matcher m = p.matcher(page);
		while (m.find()) {
			String match = m.group();
			int start = match.indexOf("href=\"");
			String url = match.substring(start + 6,
					match.indexOf("\"", start + 6));
			start = match.indexOf("alt=\"");
			String title = match.substring(start + 5,
					match.indexOf("\"", start + 5));
			if (!contains(categories, title)) {
				Category cat = new Category();
				cat.url = url;
				cat.name = title;
				categories.add(cat);
			}
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
		if (categories.isEmpty())
			getCategories();
	}
}
