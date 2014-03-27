package de.anneundsebp.ondemand.parser.dradiowissen;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.anneundsebp.ondemand.parser.Category;
import de.anneundsebp.ondemand.parser.Step;
import de.anneundsebp.ondemand.parser.Util;

public class Step1 extends Step {

	@Override
	public void process(Map<String, String> context, Category category) {
		if (this.categories == null) {
			try {
				this.categories = new ArrayList<Category>();
				String page = Util.loadPage("http://dradiowissen.de/programm");
				int h2 =0;
				while ((h2 = page.indexOf("<h2 class=\"h3\">", h2+1)) != -1) {
					int href = page.indexOf("href=", h2);
					Category c = new Category();
					c.url = page.substring(href + 6, page.indexOf("\"", href+7));
					c.name = page.substring(page.indexOf(">", href)+1, page.indexOf("<", href));
					this.categories.add(c);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
