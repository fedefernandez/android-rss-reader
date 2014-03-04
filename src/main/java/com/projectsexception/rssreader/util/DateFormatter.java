package com.projectsexception.rssreader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateFormatter {
    
    private static String[] patterns = {
        "[A-Z][a-z]{2}, [0-9][0-9] [A-Z][a-z]{2} [0-9]{4} [0-9]{2}:[0-9]{2}",
        "[A-Z][a-z]{2}, [0-9] [A-Z][a-z]{2} [0-9]{4} [0-9]{2}:[0-9]{2}",
        "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}",
        "[0-9]{4}-[0-9]{2}-[0-9]{2}",
        "[0-9]{2}-[0-9]{2}-[0-9]{4}"
    };
    
    private static String[] datePatterns = {
        "EEE, dd MMM yyyy HH:mm",
        "EEE, d MMM yyyy HH:mm",
        "yyyy-MM-dd'T'HH:mm",
        "yyyy-MM-dd",
        "dd-MM-yyyy"
    };
    
    private static Map<String, String> mapPatterns;
    
    static {
        mapPatterns = new HashMap<String, String>();
        for (int i = 0; i < patterns.length; i++) {
            mapPatterns.put(patterns[i], datePatterns[i]);            
        }
    }
    
    private SimpleDateFormat formatter;
    
    public DateFormatter() {
        formatter = new SimpleDateFormat("", Locale.ENGLISH);
    }
    
    public Date parseDate(String dateString) {        
        Pattern pattern;
        Matcher matcher;
        
        for (String patternString : mapPatterns.keySet()) {
            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(dateString);
            if (matcher.find()) {
                formatter.applyPattern(mapPatterns.get(patternString));
                try {
                    return formatter.parse(matcher.group());                    
                } catch (ParseException e) {
                }
            }
        }
        return null;
    }

}
