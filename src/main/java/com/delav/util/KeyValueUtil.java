package com.delav.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;

public class KeyValueUtil {
	private static int STATUS_KEY = 1;
	private static int STATUS_SIMPLEVALUE = 2;
	private static int STATUS_COMPLEXVALUE = 4;
	  
	public static SortedMap<String, String> keyValueStringToMap(String keyValueString) {
	    if (StringUtils.isBlank(keyValueString)) {
	      return null;
	    }
	    StringBuilder sb = new StringBuilder(keyValueString);
	    if (sb.charAt(0) == '{') {
	      sb.deleteCharAt(0);
	    }
	    if (sb.charAt(sb.length() - 1) == '}') {
	      sb.deleteCharAt(sb.length() - 1);
	    }
	    SortedMap<String, String> map = new TreeMap<String, String>();
	    
	    int currentIndex = 0;
	    String key = null;
	    String value = null;
	    
	    int status = STATUS_KEY;
	    for (int i = 0; i < sb.length(); i++) {
	      char c = sb.charAt(i);
	      if ((status == STATUS_KEY) && (c == '=')) {
	        status = STATUS_SIMPLEVALUE;
	        key = sb.substring(currentIndex, i);
	        currentIndex = i + 1;
	      } else if ((status == STATUS_SIMPLEVALUE) && (c == '&')) {
	        status = STATUS_KEY;
	        value = sb.substring(currentIndex, i);
	        map.put(key, value);
	        currentIndex = i + 1;
	      } else if ((status == STATUS_SIMPLEVALUE) && (c == '{')) {
	        status = STATUS_COMPLEXVALUE;
	      } else if ((status == STATUS_COMPLEXVALUE) && (c == '}')) {
	        status = STATUS_SIMPLEVALUE;
	      }
	    }
	    value = sb.substring(currentIndex, sb.length());
	    map.put(key, value);
	    
	    return map;
	}
	  
	public static String mapToString(Map<String, String> map) {
	    SortedMap<String, String> sortedMap = new TreeMap<String, String>(map);
	    
	    StringBuilder sb = new StringBuilder();
	    for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
	      if (!StringUtils.isBlank((CharSequence)entry.getValue())) {
	        sb.append((String)entry.getKey()).append('=').append((String)entry.getValue()).append('&');
	      }
	    }
	    sb.deleteCharAt(sb.length() - 1);
	    
	    return sb.length() == 0 ? "" : sb.toString();
	}
}

