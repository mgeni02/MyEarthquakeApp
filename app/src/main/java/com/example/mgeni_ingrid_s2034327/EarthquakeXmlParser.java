package com.example.mgeni_ingrid_s2034327;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeXmlParser {

    public static List<Earthquake> parse(String urlStr) {
        List<Earthquake> earthquakes = new ArrayList<>();

        try {
            // Create a new XmlPullParser object
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            // Set the input for the parser
            URL url = new URL(urlStr);
            InputStream inputStream = url.openConnection().getInputStream();
            parser.setInput(inputStream, null);

            // Find the first "item" element
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                    // Extract the earthquake information from the "item" element
                    String title = "";
                    String link = "";
                    String date = "";
                    String description = "";
                    double magnitude = 0.0;
                    while (eventType != XmlPullParser.END_TAG || !parser.getName().equals("item")) {
                        if (eventType == XmlPullParser.START_TAG && parser.getName().equals("title")) {
                            eventType = parser.next();
                            title = parser.getText();
                        } else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("link")) {
                            eventType = parser.next();
                            link = parser.getText();
                        } else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("pubDate")) {
                            eventType = parser.next();
                            date = parser.getText();
                        } else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("description")) {
                            eventType = parser.next();
                            description = parser.getText();
                            // Check if the description contains the "Magnitude" string and extract the value
                            int startIndex = description.indexOf("Magnitude:");
                            if (startIndex != -1) {
                                String magnitudeStr = description.substring(startIndex + 10);
                                magnitude = Double.parseDouble(magnitudeStr.trim());
                            }
                        }
                        eventType = parser.next();
                    }
                    // Create an Earthquake object with the extracted information
                    Earthquake earthquake = new Earthquake(title, link, date, description, magnitude);
                    earthquakes.add(earthquake);
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return earthquakes;
    }

}
