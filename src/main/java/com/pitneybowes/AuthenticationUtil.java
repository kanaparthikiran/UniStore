/**
 * 
 */
package com.pitneybowes;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
/**
 * @author Kiran Kanaparthi
 *
 */
@Component
public class AuthenticationUtil {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationUtil.class);

	@Autowired
	private Environment env;

    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
    
    HttpHeaders createHeaders(){
    	   return new HttpHeaders() {{
//    	         String auth = username + ":" + password;
    	 		String encodedString = Base64.getEncoder().encodeToString((env.getProperty("pitneybowes-apikey")+":"+
    					env.getProperty("pitneybowes-apisecret")).getBytes());
    	         String authHeader = "Basic " + encodedString;
    	         set( "Authorization", authHeader );
    	      }};
    	}
    
    public String getAVSURL() {
    	String avsURL = env.getProperty("pitneybowes-avs-url");
    	return avsURL;
    }
    
    public String getEmailValidationURL() {
    	String emailValidationURL = env.getProperty("pitneybowes-email-url");
    	return emailValidationURL;
    }
    
	public final String getAccessToken() {
//		String encodedString = Base64.getEncoder().encodeToString((env.getProperty("pitneybowes-apikey")+":"+
//					env.getProperty("pitneybowes-apisecret")).getBytes());
		log.info(" env.getProperty(\"pitneybowes-apisecret\")  "+env.getProperty("pitneybowes-apisecret")
			+" env.getProperty(\"pitneybowes-apikey\")  "+env.getProperty("pitneybowes-apikey"));
		
	      RestTemplate restTemplate = new RestTemplate();
//	      ResponseEntity<String> jsonResponse = restTemplate.getForEntity("http://gturnquist-quoters.cfapps.io/api/random", String.class);
//	      log.info("The Json Response from Service is "+ 
//	    		  jsonResponse.toString());

		String tokenURI = env.getProperty("pitneybowes-token-uri");
		
		log.info(" The Token URI is "+tokenURI);
		//Map<String,String> requestData = new ConcurrentHashMap<>();
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();     
		body.put("grant_type", Arrays.asList("client_credentials"));
		//requestData.put("grant_type", "client_credentials");
		HttpHeaders httpHeaders = createHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body, httpHeaders);
		
		ResponseEntity<Map> postResponse =  restTemplate.exchange(tokenURI, HttpMethod.POST, httpEntity, Map.class);
		
		//ResponseEntity<String> postResponse = restTemplate.postForEntity(tokenURI, "{\"grant_type\":\"client_credentials\"}", String.class);
		log.info(" The Post Response is "+ postResponse.getBody());
		Map<String,String> jsonMap = postResponse.getBody();
		String accessToken = getProperty(postResponse, "access_token");
		accessToken = jsonMap.get("access_token");
		return accessToken;
	}
	
	
	/**
	 * This method get the value for a Property from Json Object
	 * 
	 * @param jsonStr
	 * @param propertyName
	 * @return
	 */
	public static String getProperty(Object jsonStr, String propertyName) {
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
