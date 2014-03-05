package com.projectsexception.rssreader.model;

import java.util.Date;


public class FeedEntry {
    
    private String title;
	private String link;
	private String summary;
	private Date pubdate;
	private String image;

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Entry [title=" + title + ", pubdate=" + pubdate + ", link=" + link + "]";
    }	

}
