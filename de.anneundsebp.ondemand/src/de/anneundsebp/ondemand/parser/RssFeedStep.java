package de.anneundsebp.ondemand.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class RssFeedStep extends Step {

	@Override
	public void process(Map<String, String> context, Category category) {
		try {
			// String page = Util.loadPage(category.url);
			this.assets = new ArrayList<Asset>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(new URL(category.url).openStream(), null);
			parser.nextTag();
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();
				// Starts by looking for the entry tag
				if (name.equals("item")) {
					Asset a = new Asset();
					while (parser.next() != XmlPullParser.END_TAG) {
						if (parser.getEventType() != XmlPullParser.START_TAG) {
							continue;
						}
						name = parser.getName();
						
						if (name.equals("title")) {
							a.name = readTitle(parser);
//						} else if (name.equals("summary")) {
//							summary = readSummary(parser);
						} else if (name.equals("link")) {
							a.url = readLink(parser);
						} 
						else {
							skip(parser);
						}
						
					}
					this.assets.add(a);
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String ns = null;

	// Processes title tags in the feed.
	private String readTitle(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = parser.nextText();
//		parser.require(XmlPullParser.END_TAG, ns, "title");
		parser.next();
		return title;
	}

	// Processes link tags in the feed.
	private String readLink(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String link = "";
		parser.require(XmlPullParser.START_TAG, ns, "link");
		String tag = parser.getName();
		String relType = parser.getAttributeValue(null, "rel");
		if (tag.equals("link")) {
			if (relType != null && relType.equals("alternate")) {
				link = parser.getAttributeValue(null, "href");
				parser.nextTag();
			} else {
				link = parser.nextText();
				parser.next();
			}
		}
//		parser.require(XmlPullParser.END_TAG, ns, "link");
		return link;
	}

//	// Processes summary tags in the feed.
//	private String readSummary(XmlPullParser parser) throws IOException,
//			XmlPullParserException {
//		parser.require(XmlPullParser.START_TAG, ns, "summary");
//		String summary = parser.getText();
//		parser.require(XmlPullParser.END_TAG, ns, "summary");
//		return summary;
//	}
	
	void skip(XmlPullParser parser) {
		try {
			while (parser.next() != XmlPullParser.END_TAG);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
