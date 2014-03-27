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
		steps.add(new Step0());
		steps.add(new Step1());
		steps.add(new Step2());
		this.steps = steps;
	}
}
