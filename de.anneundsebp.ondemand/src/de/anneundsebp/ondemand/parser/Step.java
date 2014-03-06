package de.anneundsebp.ondemand.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Step {
	
	public List<Category> categories  = new ArrayList<Category>();
	public List<Asset> assets = new ArrayList<Asset>();
	
	public abstract void process(Map<String, String> context, Category category);
}
