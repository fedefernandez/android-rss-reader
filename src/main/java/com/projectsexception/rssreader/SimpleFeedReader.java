package com.projectsexception.rssreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.projectsexception.rssreader.exception.EndSAXException;
import com.projectsexception.rssreader.model.Feed;

/**
 * Library main class. You need to create a new instance to operate with 
 * @author ffernandez
 *
 */
public class SimpleFeedReader {
    
    private XMLReader xmlReader;
    
    /**
     * Constructor. It initializes the {@code XMLReader}
     */
    public SimpleFeedReader() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
            xmlReader = parser.getXMLReader();
        } catch (ParserConfigurationException e) {
            System.err.println(e.getMessage());
        } catch (SAXException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Parse feed
     * @param feedURLObject the feed url
     * @return the feed without entries summary
     * @see #getFeed(URL, Date, boolean)
     */
    public Feed getFeed(URL feedURLObject) {
        return getFeed(feedURLObject, null, false);
    }
    
    /**
     * Parse feed and return only those entries with a publication date before the specified date
     * @param feedURLObject the feed url
     * @param lastUpdatedDate
     * @return the feed without entries summary
     * @see #getFeed(URL, Date, boolean)
     */
    public Feed getFeed(URL feedURLObject, Date lastUpdatedDate) {
        return getFeed(feedURLObject, lastUpdatedDate, false);
    }
    
    /**
     * Parse feed with the entries summary if the boolean parameter is true 
     * @param feedURLObject the feed url
     * @param summary
     * @return the feed
     * @see #getFeed(URL, Date, boolean)
     */
    public Feed getFeed(URL feedURLObject, boolean summary) {
        return getFeed(feedURLObject, null, false);
    }
    
    /**
     * Parse feed with the entries summary if the boolean parameter is true. 
     * Return only those entries with a publication date before the specified date  
     * @param feedURLObject the feed url
     * @param lastUpdatedDate
     * @param summary
     * @return the feed
     * @see #getFeed(URL, Date, boolean)
     */
    public Feed getFeed(URL feedURLObject, Date lastUpdatedDate, boolean summary) {
        InputStream is = null;        
        FeedHandler feedHandler = new FeedHandler(lastUpdatedDate, summary);
        if (xmlReader != null && feedURLObject != null) {
            try {
                String encoding = detectEncoding(feedURLObject);
                
                is = feedURLObject.openStream();
                InputSource inputSource = new InputSource(is);
                if (encoding != null) {
                    inputSource.setEncoding(encoding);
                }
                
                xmlReader.setContentHandler(feedHandler);
                xmlReader.parse(inputSource);
                
            } catch (EndSAXException e) {
                // No problem
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (SAXException e) {
                System.err.println(e.getMessage());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
        return feedHandler.getFeed();
    }
    
    /**
     * Parse feed
     * @param feedURL the feed url
     * @return the feed without entries summary
     * @see #getFeed(String, Date, boolean)
     */
    public Feed getFeed(String feedURL) {
        return getFeed(feedURL, null, false);
    }
    
    /**
     * Parse feed and return only those entries with a publication date before the specified date
     * @param feedURL the feed url
     * @param lastUpdatedDate
     * @return the feed without entries summary
     * @see #getFeed(String, Date, boolean)
     */
    public Feed getFeed(String feedURL, Date lastUpdatedDate) {
        return getFeed(feedURL, lastUpdatedDate, false);
    }
    
    /**
     * Parse feed with the entries summary if the boolean parameter is true 
     * @param feedURL the feed url
     * @param summary
     * @return the feed
     * @see #getFeed(String, Date, boolean)
     */
    public Feed getFeed(String feedURL, boolean summary) {
        return getFeed(feedURL, null, summary);
    }
    
    /**
     * Parse feed with the entries summary if the boolean parameter is true. 
     * Return only those entries with a publication date before the specified date  
     * @param feedURL the feed url
     * @param lastUpdatedDate
     * @param summary
     * @return the feed
     * @see #getFeed(URL, Date, boolean)
     */
    public Feed getFeed(String feedURL, Date lastUpdatedDate, boolean summary) {
        URL feedURLObject = null;
        try {
            feedURLObject = new URL(feedURL);
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
        return getFeed(feedURLObject, lastUpdatedDate, summary);
    }
    
    private static String getFirstLine(InputStream is) {
        if (is != null) {
            String line = null;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is), 1024);
                line = reader.readLine();
            } catch (IOException e) {
                // 
            }
            return line;
        } else {        
            return "";
        }
    }
    
    private static String detectEncoding(URL urlObj) {
        String encoding = null;
        try {
            URLConnection connection = urlObj.openConnection();
            encoding = connection.getContentEncoding();
            if (encoding == null || encoding.equalsIgnoreCase("none")) {
                InputStream is = connection.getInputStream();
                String line = getFirstLine(is).toLowerCase();
                Pattern pattern = Pattern.compile("encoding=[\"']([\\w\\-_]+)[\"']");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    encoding = matcher.group(1);
                }
                is.reset();
            }
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            // 
        }
        return encoding;
    }


}
