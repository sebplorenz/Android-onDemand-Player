package de.anneundsebp.ondemand.parser;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Processor {
	public String name;
	public List<Step> steps = new ArrayList<Step>();
	public Map<String, String> context = new Hashtable<String, String>();
	public int currentStep = -1;
	public Stack<Category> currentCategory = new Stack<Category>();
	
	public String channelLogoUrl;

	public String getChannelLogoUrl() {
		return channelLogoUrl;
	}

	public void setChannelLogoUrl(String channelLogoUrl) {
		this.channelLogoUrl = channelLogoUrl;
	}

	public int getStepCount() {
		return steps.size();
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public Step forward(Category category) {
		if (currentStep >= steps.size())
			return null;

		currentStep++;
		currentCategory.push(category);
		steps.get(currentStep).process(context, category);
		return steps.get(currentStep);
	}

	public Step backward() {
		if (currentStep <= 0)
			return null;

		currentStep--;
		steps.get(currentStep).process(context, currentCategory.pop());
		return steps.get(currentStep);
	}
}
