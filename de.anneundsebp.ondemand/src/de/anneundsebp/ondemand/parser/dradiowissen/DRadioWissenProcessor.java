package de.anneundsebp.ondemand.parser.dradiowissen;

import java.util.ArrayList;
import java.util.List;

import de.anneundsebp.ondemand.parser.Processor;
import de.anneundsebp.ondemand.parser.Step;

public class DRadioWissenProcessor extends Processor {
	
	public DRadioWissenProcessor() {
		this.name = "DRadio Wissen";
		this.channelLogoUrl = "http://upload.wikimedia.org/wikipedia/commons/thumb/d/db/DRadio_Wissen.svg/500px-DRadio_Wissen.svg.png";
		List<Step> steps = new ArrayList<Step>();
		steps.add(new Step1());
		steps.add(new Step2());
		this.steps = steps;	
	}
}
