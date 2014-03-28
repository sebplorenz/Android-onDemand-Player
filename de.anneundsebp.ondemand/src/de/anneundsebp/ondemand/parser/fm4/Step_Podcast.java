package de.anneundsebp.ondemand.parser.fm4;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.anneundsebp.ondemand.parser.Category;
import de.anneundsebp.ondemand.parser.RssFeedStep;
import de.anneundsebp.ondemand.parser.Step;
import de.anneundsebp.ondemand.parser.Util;

public class Step_Podcast extends Step {

	@Override
	public void process(Map<String, String> context, Category category) {
		try {
			String page = Util.loadPage("http://fm4.orf.at/podcast");
			categories = new ArrayList<Category>();
			// Pattern p = Pattern.compile("http.*?xml");
			// Matcher m = p.matcher(page);
			// String s;
			//
			// while (m.find()) {
			// Category c = new Category();
			// c.url = m.group();
			// c.name = c.url;
			// categories.add(c);
			// }

			int i = 0;
			while ((i = page.indexOf("Listen to", i)) != -1) {
				Category c = new Category();
				i = page.indexOf("href", i);
				int j = page.indexOf("\"", i + 6);
				c.url = page.substring(i + 6, j);
				c.name = page.substring(j + 2, page.indexOf("</a", j+4)); 
				c.nextStep = RssFeedStep.class.getName();
				categories.add(c);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
