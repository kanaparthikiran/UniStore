package com.pitneybowes.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author Kiran Kanaparthi
 * 
 * This class provides the Heler methods for all the
 * Application Services.
 *
 */
@Component
public class ApplicationServicesHelper {

    private static final Logger log = LoggerFactory.getLogger(ApplicationServicesHelper.class);

     protected  String getAccessToken() {
    	return null;
    }
     
     /**
      * 
      * @return
      */
     protected HttpHeaders createHeaders(){
    	 return null;
     }
     
	/**
	 * This method get the value for a Property from Json Object
	 * 
	 * @param jsonStr
	 * @param propertyName
	 * @return
	 */
	protected static String getProperty(Object jsonStr, String propertyName) {
		String propertyValue = null;
		log.debug("jsonStr: " + jsonStr);

		if (jsonStr != null && (jsonStr instanceof String) && !StringUtils.isEmpty((String) jsonStr)) {
			try {
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse((String) jsonStr);
				if(jsonElement.isJsonObject())
					propertyValue = ((JsonObject)jsonElement).get(propertyName).getAsString();
				log.debug("Property *" + propertyName + "* from JSON object is: " + propertyValue);
			} catch (Exception e) {
				log.error("Exception parsing JSON:"+jsonStr, e);
			}
		}
		return propertyValue;
	}
}
