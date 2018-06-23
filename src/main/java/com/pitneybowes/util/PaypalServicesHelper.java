/**
 * 
 */
package com.pitneybowes.util;

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

/**
 * @author Kiran Kanaparthi
 * 
 * This class provides the Helper methods for PayPal Services
 *
 */
@Component
public class PaypalServicesHelper  extends ApplicationServicesHelper {

    private static final Logger log = LoggerFactory.getLogger(PaypalServicesHelper.class);

	@Autowired
	private Environment env;

    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
    
//    paypal-client-id=Ae1_qYulhFDcFzirTOXE9qX8LKK6bts-1QqLUcnI8M9jdHJLHTyj9RJasr3p6A7SvgQlaTgX-_lVJYOQ
//    		paypal-auth-token=EO4iicUmHvaZ_NMxow1YEwwbW3ygI0pQF63N4o-waoO10yAekUiatNmFFMBgxhjEBEQDZ1VwpsjdpmkA
    HttpHeaders createHeaders(){
 	   return new HttpHeaders() {{
// 	         String auth = username + ":" + password;
 	 		String encodedString = Base64.getEncoder().encodeToString((env.getProperty("paypal-client-id")+":"+
 					env.getProperty("paypal-auth-token")).getBytes());
 	         String authHeader = "Basic " + encodedString;
 	         log.info(" authHeader "+authHeader);
 	         set( "Authorization", authHeader );
 	      }};
 	}
    public String getPaymentURL() {
//    	String avsURL = env.getProperty("pitneybowes-avs-url");
//    	return avsURL;
    	return null;
    }
//    
//    public String getEmailValidationURL() {
//    	String emailValidationURL = env.getProperty("pitneybowes-email-url");
//    	return emailValidationURL;
//    }
//    
//    public String getGlobalWatchListURL() {
//    	String globalWatchListURL = env.getProperty("pitneybowes-global-watch-url");
//    	return globalWatchListURL;
//    }
    
	public final String getAccessToken() {
	      RestTemplate restTemplate = new RestTemplate();
		String tokenURI = env.getProperty("paypal-oath-service");
		log.info(" The Token URI is "+tokenURI);
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();     
		body.put("grant_type", Arrays.asList("client_credentials"));
		HttpHeaders httpHeaders = createHeaders();
		
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.set("Accept-Language", "en_US");

		HttpEntity<?> httpEntity = new HttpEntity<Object>(body, httpHeaders);
		
		ResponseEntity<Map> postResponse =  restTemplate.exchange(tokenURI, HttpMethod.POST, httpEntity, Map.class);
		log.info(" The Post Response is "+ postResponse.getBody());
		Map<String,String> jsonMap = postResponse.getBody();
		String accessToken = getProperty(postResponse, "access_token");
		accessToken = jsonMap.get("access_token");
		return accessToken;
	}
	
}