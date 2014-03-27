package de.anneundsebp.ondemand.parser.dradiowissen;

import java.util.ArrayList;

import de.anneundsebp.ondemand.parser.Processor;
import de.anneundsebp.ondemand.parser.Step;

public class DRadioWissenProcessor extends Processor {
	
	public DRadioWissenProcessor() {
		this.name = "DRadio Wissen";
		this.channelLogoUrl = "http://upload.wikimedia.org/wikipedia/commons/thumb/d/db/DRadio_Wissen.svg/500px-DRadio_Wissen.svg.png";
		this.steps = new ArrayList<Step>();
		this.firstStep = new Step1();
	}
}
