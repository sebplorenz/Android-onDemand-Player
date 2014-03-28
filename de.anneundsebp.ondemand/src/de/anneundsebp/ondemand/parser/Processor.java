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
	public Step firstStep;

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
		// if (currentStep >= steps.size())
		// return null;

		currentStep++;
		currentCategory.push(category);
		// steps.get(currentStep).process(context, category);
		// return steps.get(currentStep);

		if (currentStep != 0) {
			if (category != null && category.next == null)
				try {
					category.next = (Step) Class.forName(category.nextStep)
							.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			steps.add(category.next);
			category.next.process(context, category);
			return category.next;
		}
	
		steps.add(this.firstStep);
		this.firstStep.process(context, category);
		return firstStep;
	}

	public Step backward() {
		if (currentStep <= 0)
			return null;

		currentStep--;
		steps.get(currentStep).process(context, currentCategory.pop());
		return steps.get(currentStep);
	}

	@Override
	public String toString() {
		return name;
	}
}
