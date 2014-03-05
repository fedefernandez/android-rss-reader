package com.projectsexception.rssreader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.projectsexception.rssreader.model.Feed;
import com.projectsexception.rssreader.model.FeedEntry;

public class SimpleFeedReaderTest {
    
    private SimpleFeedReader feedReader;

    @Before
    public void setUp() throws Exception {
        feedReader = new SimpleFeedReader();
    }

    @Test
    public void testGetFeedAndrocode() {
        URL url = getClass().getResource("/Androcode.xml");
        Feed feed = feedReader.getFeed(url, null);
        assertNotNull(feed);
        assertEquals("Androcode", feed.getTitle());
        assertEquals("http://androcode.es", feed.getWeb());
        assertNotNull(feed.getEntryList());
        assertEquals(10, feed.getEntryList().size());
        FeedEntry entry = feed.getEntryList().get(0);
        assertEquals("Cómo hacer más cómodo el trabajo con Eclipse", entry.getTitle());
        assertEquals("http://androcode.es/2013/02/como-hacer-mas-comodo-el-trabajo-con-eclipse/", entry.getLink());
        assertNotNull(entry.getPubdate());
        Date date = entry.getPubdate();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        //Thu, 14 Feb 2013 10:37:16 +0000
        assertEquals(2013, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, calendar.get(Calendar.MONTH));
        assertEquals(14, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(10, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(37, calendar.get(Calendar.MINUTE));
        
        calendar.set(Calendar.YEAR, 2013);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 20);
        feed = feedReader.getFeed(url, calendar.getTime());
        assertNotNull(feed);
        assertEquals("Androcode", feed.getTitle());
        assertEquals("http://androcode.es", feed.getWeb());
        assertNotNull(feed.getEntryList());
        assertEquals(3, feed.getEntryList().size());
    }

    @Test
    public void testGetFeedAndroidDevelopersBlog() {
        URL url = getClass().getResource("/AndroidDevelopersBlog.xml");
        Feed feed = feedReader.getFeed(url, null);
        assertNotNull(feed);
        assertEquals("Android Developers Blog", feed.getTitle());
        assertEquals("http://android-developers.blogspot.com/", feed.getWeb());
        assertNotNull(feed.getEntryList());
        assertEquals(25, feed.getEntryList().size());
        FeedEntry entry = feed.getEntryList().get(0);
        assertEquals("Security Enhancements in Jelly Bean", entry.getTitle());
        assertEquals("http://feedproxy.google.com/~r/blogspot/hsDu/~3/Q204r-VOkgo/security-enhancements-in-jelly-bean.html", entry.getLink());
        assertEquals("http://1.bp.blogspot.com/-ak-KpU1tBWY/URwL4H1K2QI/AAAAAAAAB8I/7sQBdBaQQG0/s72-c/adb-crop-new.png", entry.getImage());
        assertNotNull(entry.getPubdate());
        Date date = entry.getPubdate();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        //2013-02-14T20:59:49.254-08:00
        assertEquals(2013, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, calendar.get(Calendar.MONTH));
        assertEquals(14, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
    }

    @Test
    public void testGetFeedAndroidDevWeekly() {
        URL url = getClass().getResource("/AndroidDevWeekly.xml");
        Feed feed = feedReader.getFeed(url, null);
        assertNotNull(feed);
        assertEquals("#AndroidDev Weekly", feed.getTitle());
        assertEquals("http://androiddevweekly.com/", feed.getWeb());
        assertNotNull(feed.getEntryList());
        assertEquals(45, feed.getEntryList().size());
        FeedEntry entry = feed.getEntryList().get(0);
        assertEquals("Issue 45", entry.getTitle());
        assertEquals("http://feedproxy.google.com/~r/AndroidDevWeekly/~3/xTSfdzzDVpU/Issue-45.html", entry.getLink());
        assertNotNull(entry.getPubdate());
        Date date = entry.getPubdate();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        //2013-02-11T00:00:00-08:00
        assertEquals(2013, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, calendar.get(Calendar.MONTH));
        assertEquals(11, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(00, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(00, calendar.get(Calendar.MINUTE));
    }

}
