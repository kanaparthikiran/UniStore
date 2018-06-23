package com.pitneybowes.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pitneybowes.constants.UniStoreServicesConstants;

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
    protected static Map<String,Object> tokenCache = new ConcurrentHashMap<>();

     protected  Map<String,Object> getAccessToken() {
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
	 * 
	 * @param expiresInMinutes
	 * @return
	 */
	protected static Calendar addTimeToCurrentTime(int expiresInMinutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, expiresInMinutes);
		return calendar;
	}
	
	/**
	 * @return
	 */
	private static boolean isTokenExpired(String expiryParamName) {
		return tokenCache.get(expiryParamName)!=null 
				&& Calendar.getInstance().before(tokenCache.get
						(expiryParamName));
	}
	

	
    /**
     * This method gets the Cached Auth Token
     * @return
     */
	public  String  getCachedToken(Object lock,String expiryParamName) {
		String authToken = null;
		if(tokenCache==null || tokenCache.isEmpty()) {
			log.info(" Getting the Token for the First Time " );
			return (String)getAccessToken().get(UniStoreServicesConstants.ACCESS_TOKEN);
		}
		if(isTokenExpired(expiryParamName)) {
			log.info(" The Token is Expired, Getting Again from the API Provider " );
			synchronized(lock) {
				if(isTokenExpired(expiryParamName)) {
					return (String)getAccessToken().get(UniStoreServicesConstants.ACCESS_TOKEN);
				}
			}
		} else {
			log.info(" The Token has Not Expired, Getting from the Cache " );
			authToken = (String)tokenCache.get(UniStoreServicesConstants.ACCESS_TOKEN);
		}
		return authToken;
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
