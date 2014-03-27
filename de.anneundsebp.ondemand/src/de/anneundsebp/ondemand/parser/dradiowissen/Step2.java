package de.anneundsebp.ondemand.parser.dradiowissen;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;

import de.anneundsebp.ondemand.parser.Asset;
import de.anneundsebp.ondemand.parser.Category;
import de.anneundsebp.ondemand.parser.Step;
import de.anneundsebp.ondemand.parser.Util;

public class Step2 extends Step {

	@Override
	public void process(Map<String, String> context, Category category) {
		if (category != null && this.categories == null) {
			try {
				this.assets = new ArrayList<Asset>();
				String nextPage = category.url;
				int pageCount = 2;
				String page;
				do {
					page = Util.loadPage(nextPage);
					int button = 0;
					while ((button = page.indexOf("<button data-title", button + 1)) != -1) {
						Asset c = new Asset();
						c.name = page.substring(button+20, page.indexOf("\"", button+21));
						int data = page.indexOf("data-mp3=", button);
						c.url = page.substring(data+10,
								page.indexOf("\"", data + 12));
						this.assets.add(c);
					}
					nextPage = category.url + "/p" + pageCount++;
				} while (page.contains(nextPage));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
