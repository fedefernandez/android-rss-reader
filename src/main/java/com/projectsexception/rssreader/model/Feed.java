package com.projectsexception.rssreader.model;

import java.util.ArrayList;
import java.util.List;

public class Feed {
    
    private String title;
	private String web;
	private List<FeedEntry> entryList;
	
	public Feed(){
		entryList = new ArrayList<FeedEntry>();
	}

    public void addEntry(FeedEntry item){
		entryList.add(item);
	}

    public List<FeedEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<FeedEntry> entryList) {
        this.entryList = entryList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Override
    public String toString() {
        return "RSSFeed [web=" + web + ", title=" + title + "]";
    }

}
