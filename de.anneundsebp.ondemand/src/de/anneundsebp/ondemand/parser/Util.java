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
