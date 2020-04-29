package com.delav.util;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class XmlUtil {
	public static final String UTF8_ENCODING = "UTF-8";
	  
	public static String parseNodeValueFromXml(String nodeStart, String nodeEnd, String src) {
	    int nodeStartLength = nodeStart.length();
	    int start = src.indexOf(nodeStart);
	    int end = src.indexOf(nodeEnd);
	    if ((start > -1) && (end > -1)) {
	      return src.substring(start + nodeStartLength, end);
	    }
	    return "";
	  }
	  
	public static String getNodeValueFromXml(String nodeStart, String nodeEnd, String src) {
	    String rtnStr = "";
	    int nodeStartLength = nodeStart.length();
	    int start = src.indexOf(nodeStart);
	    int end = src.indexOf(nodeEnd);
	    if ((start > -1) && (end > -1)) {
	      rtnStr = src.substring(start + nodeStartLength, end);
	    }
	    return rtnStr;
	}
	  
	public static String getNodeValueFromXml(String nodeName, String xmlStr) {
	    String nodeStart = "<" + nodeName + ">";
	    String nodeEnd = "</" + nodeName + ">";
	    return getNodeValueFromXml(nodeStart, nodeEnd, xmlStr);
	}
	  
	public static String relaceNodeContent(String nodeStart, String nodeEnd, String relacement, String xml) {
	    int nodeStartLength = nodeStart.length();
	    int start = xml.indexOf(nodeStart);
	    int end = xml.indexOf(nodeEnd);
	    if ((start > -1) && (end > -1))
	    {
	      String segStart = xml.substring(0, start + nodeStartLength);
	      String segEnd = xml.substring(end, xml.length());
	      return segStart + relacement + segEnd;
	    }
	    return xml;
	}
	  
	public static Document getDocumentFromXmlStr(String xmlStr) throws DocumentException {
	    Document doc = DocumentHelper.parseText(xmlStr);
	    return doc;
	}
	  
	public static String getNodeValueFromDocument(Document doc, String nodeName) {
	    return getNodeValueByXpath(doc, "//" + nodeName);
	}
	  
	public static String getNodeValueByXpath(Document doc, String xpathExpress) {
	    Node node = doc.selectSingleNode(xpathExpress);
	    return node.getText();
	}
	  
	public static String parseElement(String xmlStr, String fromSta, String fromEnd, String toSta, String toEnd) {
	    String rspStr = parseNodeValueFromXml(fromSta, fromEnd, xmlStr);
	    rspStr = fromSta + rspStr + fromEnd;
	    
	    String newXmlStr = relaceNodeContent(toSta, toEnd, rspStr, xmlStr);
	    return newXmlStr;
	}
	  
	public static String replaceBlank(String str) {
	    String dest = "";
	    if (str != null) {
	      Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	      Matcher m = p.matcher(str);
	      dest = m.replaceAll("");
	      dest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + dest.substring(36);
	    }
	    return dest;
	}
	  
	public static SortedMap<String, String> xmlStrToMap(String xmlStr) throws DocumentException {
	    SortedMap<String, String> map = new TreeMap<String, String>();
	    Document doc = DocumentHelper.parseText(xmlStr);
	    Element root = doc.getRootElement();
	    getNodes(root, map);
	    return map;
	}
	  
	private static void getNodes(Element node, Map<String, String> map) {
	    List<Element> list = node.elements();
	    if (list.size() > 0) {
	      for (Element element : list) {
	        getNodes(element, map);
	      }
	    } else {
	      map.put(node.getName(), node.getTextTrim());
	    }
	}
}
