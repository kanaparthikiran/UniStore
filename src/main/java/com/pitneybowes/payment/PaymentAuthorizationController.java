/**
 * 
 */
package com.pitneybowes.payment;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.GsonBuilder;
import com.pitneybowes.constants.UniStoreServicesConstants;
import com.pitneybowes.util.AuthenticationUtil;

/**
 * @author Kiran Kanaparthi
 *
 */
@RestController
@RequestMapping(UniStoreServicesConstants.PAYMENT_VERIFICATION_PATH)
public class PaymentAuthorizationController {

	  private static final Logger log = LoggerFactory.getLogger(PaymentAuthorizationController.class);

	    @Autowired
	    AuthenticationUtil authenticationUtil;
	    
		@RequestMapping(path="/verify",method=RequestMethod.GET,
		produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
		public String verifyAddress(String address) {
	      String accessToken = authenticationUtil.getAccessToken();
	      log.info(" accessToken returned from  Auth Service is "+ accessToken);
	      String avsResponse = callAVSService(accessToken);
		  return avsResponse;
		}

		
	    HttpHeaders createHeaders(){
	    	   return new HttpHeaders();
	    }
	    
	    /**
	     * 
	     * @param accessToken
	     * @return
	     */
		public final String callAVSService(String accessToken) {
		    RestTemplate restTemplate = new RestTemplate();
		    String avsURL = authenticationUtil.getAVSURL();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			log.info(" accessToken  "+ accessToken);
			httpHeaders.add("Authorization", "Bearer "+accessToken);
			httpHeaders.add("Host", "api.pitneybowes.com");

			String avsRequest = "{ \"options\": { \"OutputCasing\": \"M\" },\n" + 
					"\"Input\": {\n" + 
					"\"Row\": [\n" + 
					"{\n" + 
					"\"AddressLine1\": \"65 Rio Robles East\",\n" + 
					"\"AddressLine2\": \"Unit 3228\",\n" + 
					"\"City\": \"San Jose\",\n" + 
					"\"Country\": \"US\",\n" + 
					"\"StateProvince\": \"CA\",\n" + 
					"\"PostalCode\": \"95134\",\n" + 
					"\"FirmName\": \"\"\n" + 
					"}\n" + 
					"]}}";

			HttpEntity<?> httpEntity = new HttpEntity<>(avsRequest, httpHeaders);
			ResponseEntity<Map> postResponse =  restTemplate.exchange
					(avsURL, HttpMethod.POST, httpEntity, Map.class);
			log.info(" The Post Response is "+ postResponse.getBody());
			return new GsonBuilder().create().toJson(postResponse.getBody());
		}
}
