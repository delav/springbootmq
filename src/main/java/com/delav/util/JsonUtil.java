package com.delav.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.StringWriter;

public class JsonUtil {

	private static ThreadLocal<ObjectMapper> local = new ThreadLocal<ObjectMapper>() {
		
		protected ObjectMapper initialValue() {
			return new ObjectMapper();
		}
  	};
  
  	public static String object2JsonStr(Object obj) throws JsonProcessingException {
  		ObjectMapper mapper = (ObjectMapper)local.get();
  		String jsonStr;
  		try {
  			jsonStr = mapper.writeValueAsString(obj);
  		} catch (JsonProcessingException e) {
  			throw e;
  		}
  		return jsonStr;
  	}
  
  	public static <T> T jsonStr2Obj(String json, Class<T> clazz) throws IOException {
	    ObjectMapper mapper = (ObjectMapper)local.get();
	    T obj;
	    try {
	    	obj = mapper.readValue(json, clazz);
	    } catch (JsonParseException|JsonMappingException e) {
	      throw e;
	    } catch (IOException e) {
	      throw e;
	    }
	    return obj;
	}

	public static <T> T get(String json, String fieldName, Class<T> clazz) throws IOException {
	    ObjectMapper mapper = (ObjectMapper)local.get();
	    T obj;
	    try {
	    	ObjectNode objectNode = (ObjectNode)mapper.readTree(json);
	    	obj = mapper.readValue(objectNode.get(fieldName).toString(), clazz);
	    } catch (JsonParseException|JsonMappingException e) {
	      throw e;
	    } catch (IOException e) {
	      throw e;
	    }
	    return obj;
	}
  
	public static <T> T get(String json, String fieldName, TypeReference<?> valueTypeRef) throws IOException {
	    ObjectMapper mapper = (ObjectMapper)local.get();
	    T obj;
	    try {
	      ObjectNode objectNode = (ObjectNode)mapper.readTree(json);
	      obj = mapper.readValue(objectNode.get(fieldName).toString(), valueTypeRef);
	    } catch (JsonParseException|JsonMappingException e) {
	    	throw e;
	    } catch (IOException e) {
	      throw e;
	    }
	    return obj;
	}
  
	public static String updateJsonStr(String json, String needToUpdate, String fieldName) throws IOException {
	    ObjectMapper mapper = (ObjectMapper)local.get();
	    ObjectNode objectNode = null;
	    try {
	      JsonNode needToUpdateNode = null;
	      objectNode = (ObjectNode)mapper.readTree(json);
	      needToUpdateNode = mapper.readTree(needToUpdate);
	      objectNode.set(fieldName, needToUpdateNode);
	      return objectNode.toString();
	    } catch (IOException e) {
	      throw e;
	    }
	}
  
	public static <T> T jsonStr2GenericObj(String json, TypeReference<?> type) throws IOException {
	    ObjectMapper mapper = (ObjectMapper)local.get();
	    T genericObj;
	    try {
	      genericObj = mapper.readValue(json, type);
	    } catch (JsonParseException|JsonMappingException e) {
	      throw e;
	    } catch (IOException e) {
	      throw e;
	    }
	    return genericObj;
	}
  
	public static <T> String genericObj2JsonStr(T obj, TypeReference<?> type) throws IOException {
	    ObjectMapper mapper = (ObjectMapper)local.get();
	    ObjectWriter writer = mapper.writerFor(type);
	    StringWriter w = new StringWriter();
	    try {
	      writer.writeValue(w, obj);
	    } catch (JsonGenerationException|JsonMappingException e) {
	      throw e;
	    } catch (IOException e) {
	      throw e;
	    }
	    return w.toString();
	}
  
	public static String get(String jsonStr, String fieldName) throws IOException {
	    String rtnStr = null;
	    try {
	      JsonNode node = ((ObjectMapper)local.get()).readTree(jsonStr);
	      if (node.get(fieldName) == null) {
	        return null;
	      }
	      rtnStr = node.get(fieldName).toString();
	      rtnStr = rtnStr.startsWith("\"") ? rtnStr.substring(1, rtnStr.length()) : rtnStr;
	      rtnStr = rtnStr.endsWith("\"") ? rtnStr.substring(0, rtnStr.length() - 1) : rtnStr;
	    } catch (IOException e) {
	      throw e;
	    }
	    return rtnStr;
	}
}

