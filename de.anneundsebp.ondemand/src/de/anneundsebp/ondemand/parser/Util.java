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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Util {
	public static String loadPage(String url) throws MalformedURLException {
		URL urlURL = new URL(url);
		StringBuffer buffer = new StringBuffer();
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(urlURL.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				buffer.append(inputLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
