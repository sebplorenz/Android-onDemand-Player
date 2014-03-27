package de.anneundsebp.ondemand.parser.fm4;

import java.util.ArrayList;
import java.util.Map;

import de.anneundsebp.ondemand.parser.Asset;
import de.anneundsebp.ondemand.parser.Category;
import de.anneundsebp.ondemand.parser.Step;

public class Step0 extends Step {
	
	@Override
	public void process(Map<String, String> context, Category category) {
		if (this.assets == null) {
			// livestream
			this.assets = new ArrayList<Asset>(1);
			Asset asset = new Asset();
			asset.name = "FM4 Livestream";
			asset.url = "http://mp3stream1.apasf.apa.at:8000";
			this.assets.add(asset);
			
			this.categories = new ArrayList<Category>(2);
			Category c = new Category();
			c.name = "FM4 onDemand";
			c.url = "d";
			c.nextStep = Step1.class.getName();
			this.categories.add(c);
			
//			c = new Category();
//			c.name = "FM4 Podcasts";
//			c.url = "p";
//			this.categories.add(c);
		}
	}
}
