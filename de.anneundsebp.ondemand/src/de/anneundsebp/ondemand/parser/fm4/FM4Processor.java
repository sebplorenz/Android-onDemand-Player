package de.anneundsebp.ondemand.parser.fm4;

import java.util.ArrayList;
import java.util.List;

import de.anneundsebp.ondemand.parser.Processor;
import de.anneundsebp.ondemand.parser.Step;

public class FM4Processor extends Processor {

	public FM4Processor() {
		this.name = "FM4";
		this.channelLogoUrl = "http://fm4.orf.at/v2static/images/fm4_logo.jpg";
		List<Step> steps = new ArrayList<Step>();
		steps.add(new Step1());
		steps.add(new Step2());
		this.steps = steps;
	}
}
