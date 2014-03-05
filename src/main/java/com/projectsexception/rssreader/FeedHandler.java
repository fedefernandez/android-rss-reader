package com.projectsexception.rssreader;

import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.projectsexception.rssreader.exception.EndSAXException;
import com.projectsexception.rssreader.exception.UnknownFeedType;
import com.projectsexception.rssreader.model.Feed;
import com.projectsexception.rssreader.model.FeedEntry;
import com.projectsexception.rssreader.model.FeedType;
import com.projectsexception.rssreader.util.DateFormatter;

public class FeedHandler extends DefaultHandler {
    
    private Feed feed;
    private FeedEntry entry;
    private FeedType feedType;
    private Date lastReadDate;
    private boolean summary;
    
    private int currentstate;
    private boolean data;
    private StringBuilder buffer;
    private DateFormatter dateFormatter;
    
    private static final int STATE_INIT = 0;
    private static final int STATE_INFO = 1;
    private static final int STATE_ENTRIES = 2;
    private static final int STATE_ENTRY = 3;


    public FeedHandler() {
        this(null, false);
    }


    public FeedHandler(Date lastReadDate, boolean summary) {
        this.currentstate = 0;
        this.lastReadDate = lastReadDate;
        this.dateFormatter = new DateFormatter();
        this.summary = summary;
    }

    /*
     * getFeed - this returns our feed when all of the parsing is complete
     */
    public Feed getFeed() {
        return feed;
    }

    public void startDocument() throws SAXException {
        feed = new Feed();
        currentstate = STATE_INIT;
    }

    public void endDocument() throws SAXException {
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        String name = getElementName(localName, qName);
        
        if (currentstate == STATE_INIT) {
            if (name.equalsIgnoreCase(FeedType.RSS.getRootElement())) {
                feedType = FeedType.RSS;
            } else if (name.equalsIgnoreCase(FeedType.RDF.getRootElement())) {
                feedType = FeedType.RDF;
            } else if (name.equalsIgnoreCase(FeedType.ATOM.getRootElement())) {
                feedType = FeedType.ATOM;
            } else {
                throw new UnknownFeedType("Unknown Feed: <" + name + ">");
            }
            currentstate = STATE_INFO;
        } else if (currentstate == STATE_INFO) {
            if (name.equals(feedType.getRootTitleElement())) {
                data = true;
                buffer = new StringBuilder();
            } else  if (name.equals(feedType.getRootLinkElement())) {
                if (feedType == FeedType.ATOM) {
                    String relValue = atts.getValue("rel");
                    if ((relValue == null && feed.getWeb() == null) || "alternate".equals(relValue)) {
                        feed.setWeb(atts.getValue("href"));
                    }
                } else {
                    data = true;
                    buffer = new StringBuilder();
                }
            } else if (name.equals(feedType.getEntryElement())) {
                entry = new FeedEntry();
                currentstate = STATE_ENTRY;
            }
        } else if (currentstate == STATE_ENTRIES) {
            if (name.equals(feedType.getEntryElement())) {
                entry = new FeedEntry();
                currentstate = STATE_ENTRY;
            }
        } else if (currentstate == STATE_ENTRY) {
            if (name.equals(feedType.getEntryTitleElement())
                    || name.equals(feedType.getEntryUpdateElement())
                    || (summary && name.equals(feedType.getEntrySummaryElement()))) {
                data = true;
                buffer = new StringBuilder();
            } else if (name.equals(feedType.getEntryLinkElement())) {
                if (feedType == FeedType.ATOM) {
                    String relValue = atts.getValue("rel");
                    if ((relValue == null && entry.getLink() == null) || "alternate".equals(relValue)) {
                        entry.setLink(atts.getValue("href"));
                    }
                } else {
                    data = true;
                    buffer = new StringBuilder();
                }
            } else if (name.equals(feedType.getImageElement())) {
                if (feedType == FeedType.ATOM) {
                    entry.setImage(atts.getValue("url"));
                } else if (feedType == FeedType.RSS) {
                    String type = atts.getValue("type");
                    if (type != null && type.startsWith("image/")) {
                        entry.setImage(atts.getValue("url"));
                    }
                }
            }
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        String name = getElementName(localName, qName);
        
        if (currentstate == STATE_INFO) {
            if (name.equals(feedType.getRootTitleElement())) {
                feed.setTitle(cleanString(buffer.toString()));
            } else  if (name.equals(feedType.getRootLinkElement()) && feedType != FeedType.ATOM) {
                String cleanString = cleanString(buffer.toString());
                if (cleanString != null && cleanString.length() > 0) {
                    feed.setWeb(cleanString(buffer.toString()));
                }
            }
            data = false;
        } else if (currentstate == STATE_ENTRY) {
            if (name.equals(feedType.getEntryElement())) {
                feed.addEntry(entry);
                currentstate = STATE_ENTRIES;
            } else if (name.equals(feedType.getEntryTitleElement())) {
                entry.setTitle(cleanString(buffer.toString()));
                data = false;
            } else if (name.equals(feedType.getEntryLinkElement()) && feedType != FeedType.ATOM) {
                String cleanString = cleanString(buffer.toString());
                if (cleanString != null && cleanString.length() > 0) {
                    entry.setLink(cleanString(buffer.toString()));
                }
                data = false;
            } else if (name.equals(feedType.getEntryUpdateElement())) {
                Date entryDate = dateFormatter.parseDate(cleanString(buffer.toString()));
                if (lastReadDate != null) {
                    if (lastReadDate.compareTo(entryDate) >= 0) {
                        throw new EndSAXException();
                    }
                }
                entry.setPubdate(entryDate);
                data = false;
            } else if (summary && name.equals(feedType.getEntrySummaryElement())) {
                entry.setSummary(buffer.toString());
                data = false;
            }
        }
    }

    public void characters(char ch[], int start, int length) {
        if (data) {
            buffer.append(new String(ch, start, length));
        }
    }
    
    private String cleanString(String string) {
        if (string != null) {
            String tmp = string.replaceAll("\n", " ");
            tmp = tmp.replaceAll("\t", " ");
            tmp = tmp.replaceAll("\\s\\s+", " ");
            return tmp.trim();
        } else {
            return null;
        }
    }
    
    private String getElementName(String localName, String qName) {
        String name = localName;
        
        if (localName == null || localName.length() == 0) {
            int qual = qName.lastIndexOf(":");
            if (qual > 0) {
                name = qName.substring(qual + 1);
            } else {
                name = qName;
            }
        }
        return name;
    }
}
