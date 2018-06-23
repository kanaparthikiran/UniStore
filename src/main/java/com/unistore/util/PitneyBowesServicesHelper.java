/**
 * 
 */
package com.unistore.util;

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
import org.springframework.web.client.RestTemplate;

import com.unistore.constants.UniStoreServicesConstants;
/**
 * @author Kiran Kanaparthi
 *
 */
@Component
public class PitneyBowesServicesHelper extends ApplicationServicesHelper {

    private static final Logger log = LoggerFactory.getLogger(PitneyBowesServicesHelper.class);
	

	@Autowired
	private Environment env;

    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	
    /**
     * 
     * @return
     */
    public  HttpHeaders createHeaders(){
    	   return new HttpHeaders() {/**
			 * 
			 */
			private static final long serialVersionUID = -3804363158474230702L;
		{
    	 		String encodedString = Base64.getEncoder().
    	 				encodeToString((env.getProperty("pitneybowes-apikey")+":"+
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
    
    public String getGlobalWatchListURL() {
    	String globalWatchListURL = env.getProperty("pitneybowes-global-watch-url");
    	return globalWatchListURL;
    }
    
    public String getGeo911URL() {
    	String geo911URL = env.getProperty("pitneybowes-geo-911");
    	return geo911URL;
    }

    
    public String getGeoCodeURL() {
    	String geoCodeURL = env.getProperty("pitneybowes-geo-code");
    	return geoCodeURL;
    }
    
    public String getGeoLocationURL() {
    	String geoLocationURL = env.getProperty("pitneybowes-geo-location");
    	return geoLocationURL;
    }
    
    public String getGeoTax() {
    	String geoTaxURL = env.getProperty("pitneybowes-geo-tax");
    	return geoTaxURL;
    }

    
    /**
     * 
     */
	public  final Map<String,Object> getAccessToken() {
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
		String accessToken = getProperty(postResponse, UniStoreServicesConstants.ACCESS_TOKEN);
		accessToken = jsonMap.get(UniStoreServicesConstants.ACCESS_TOKEN);
		long expiresInMinutes = Long.parseLong(jsonMap.get(UniStoreServicesConstants.EXPIRES_IN_PITNEY_BOWES));
		tokenCache.put(UniStoreServicesConstants.ACCESS_TOKEN,accessToken);
		
		tokenCache.put(UniStoreServicesConstants.EXPIRES_IN_PITNEY_BOWES, addTimeToCurrentTime((int)(expiresInMinutes)));
		return tokenCache;
	}
	
	

}
