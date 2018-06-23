/**
 * 
 */
package com.unistore.payment;

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
import com.unistore.constants.UniStoreServicesConstants;
import com.unistore.util.PaypalServicesHelper;

/**
 * @author Kiran Kanaparthi
 *
 */
@RestController
@RequestMapping(UniStoreServicesConstants.PAYMENT_VERIFICATION_PATH)
public class PaymentAuthorizationController {

	  private static final Logger log = LoggerFactory.getLogger(PaymentAuthorizationController.class);

	    @Autowired
	    PaypalServicesHelper servicesHelper;
	    
		@RequestMapping(path="/verify",method=RequestMethod.GET,
		produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
		public String verifyAddress(String address) {
	      String accessToken = servicesHelper.getCachedToken(PaypalServicesHelper.class,UniStoreServicesConstants.EXPIRES_IN_PAYPAL);
	      log.info(" accessToken returned from  Auth Service is "+ accessToken);
	      String invoicesList = callInvoiceService(accessToken);
		  return invoicesList;
		}

		
	    HttpHeaders createHeaders(){
	    	   return new HttpHeaders();
	    }
	    
	    /**
	     * 
	     * @param accessToken
	     * @return
	     */
		public final String callInvoiceService(String accessToken) {
		    RestTemplate restTemplate = new RestTemplate();
		    String avsURL = servicesHelper.getInvoiceURL()+"?page=3&page_size=4&total_count_required=true";
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			log.info(" accessToken  "+ accessToken);
			httpHeaders.add("Authorization", "Bearer "+accessToken);
			//httpHeaders.add("Host", "api.pitneybowes.com");
			HttpEntity<?> httpEntity = new HttpEntity<>(null, httpHeaders);
			ResponseEntity<Map> postResponse =  restTemplate.exchange
					(avsURL, HttpMethod.GET, httpEntity, Map.class);
			log.info(" The Post Response is "+ postResponse.getBody());
			return new GsonBuilder().create().toJson(postResponse.getBody());
		}
		
	
		
}
