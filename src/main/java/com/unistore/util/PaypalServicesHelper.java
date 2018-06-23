/**
 * 
 */
package com.unistore.util;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    
    @Override
    protected HttpHeaders createHeaders(){
 	   return new HttpHeaders() {
		private static final long serialVersionUID = 5428739747745980088L;
		{
 	 		String encodedString = Base64.getEncoder().encodeToString
 	 				((env.getProperty("paypal-client-id")+":"+
 					env.getProperty("paypal-auth-token")).getBytes());
 	         String authHeader = "Basic " + encodedString;
 	         log.info(" authHeader "+authHeader);
 	         set( "Authorization", authHeader );
 	      }};
 	}

    public String getInvoiceURL() {
    	String invoiceURL = env.getProperty("paypal-invoices-service");
    	return invoiceURL;
    }
    
    public String getPaymentsURL() {
    	String allPaymentsURL = env.getProperty("paypal-all-payments-service");
    	return allPaymentsURL;
    }
    
    public String getCreateOrderURL() {
    	String createOrderURL = env.getProperty("paypal-create-order-service");
    	return createOrderURL;
    }

    /**
     * This method gets the Access Token, and Expiry Time 
     * from the Auth Service
     * 
     */
    @Override
	public Map<String,Object> getAccessToken() {
	      RestTemplate restTemplate = new RestTemplate();
		String tokenURI = env.getProperty("paypal-oath-service");
		log.info(" The Token URI is "+tokenURI);
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();     
		body.put("grant_type", Arrays.asList("client_credentials"));
		HttpHeaders httpHeaders = createHeaders();
		
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.set("Accept-Language", "en_US");

		HttpEntity<?> httpEntity = new HttpEntity<Object>(body, httpHeaders);
		
		ResponseEntity<Map> postResponse =  restTemplate.exchange
				(tokenURI, HttpMethod.POST, httpEntity, Map.class);
		log.info(" The Post Response is "+ postResponse.getBody());
		Map<Object,Object> jsonMap = postResponse.getBody();
		String accessToken = null;
		Integer expiresIn = null;
		accessToken = (String)jsonMap.get(UniStoreServicesConstants.ACCESS_TOKEN);
		log.info(" acceess Token "+accessToken);
		log.info((" expires In "+jsonMap.get(UniStoreServicesConstants.EXPIRES_IN_PAYPAL)));
		expiresIn = (Integer)jsonMap.get(UniStoreServicesConstants.EXPIRES_IN_PAYPAL);
		Map<String,Object> accessTokenMap = new ConcurrentHashMap<>();
		accessTokenMap.put(UniStoreServicesConstants.ACCESS_TOKEN, accessToken);
		accessTokenMap.put(UniStoreServicesConstants.EXPIRES_IN_PAYPAL, expiresIn);
		return accessTokenMap;
	}
	
}
