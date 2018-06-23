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
	  
	  private static final RestTemplate restTemplate = new RestTemplate();

	    @Autowired
	    PaypalServicesHelper servicesHelper;
	    
		@RequestMapping(path="/verify",method=RequestMethod.GET,
		produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
		public String verifyAddress(String address) {
	      String accessToken = servicesHelper.getCachedToken(PaypalServicesHelper.class,UniStoreServicesConstants.EXPIRES_IN_PAYPAL);
	      log.info(" accessToken returned from  Auth Service is "+ accessToken);
	      String invoicesList = callInvoiceService(accessToken);
	      log.info(" invoices List "+invoicesList);
	    //  String createPaymentResponse = createPayment(accessToken);
	      
	      String allPaymentsList = listAllPayments(accessToken);
	      log.info(" allPaymentsList  "+allPaymentsList);
		  return allPaymentsList;
		}

		
	    HttpHeaders createHeaders(){
	    	   return new HttpHeaders();
	    }
	    
	    /**
	     * 
	     * @param accessToken
	     * @return
	     */
		@RequestMapping(path="/createPayment",method=RequestMethod.GET,
		produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
	    private final String createPayment(String accessToken) {
		      log.info(" accessToken returned from  Auth Service is "+ accessToken);
		      String createPaymentURL = servicesHelper.getPaymentsURL();
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				log.info(" accessToken  "+ accessToken);
				httpHeaders.add("Authorization", "Bearer "+accessToken);
				//httpHeaders.add("Host", "api.pitneybowes.com");
				String jsonRequestBody = "{\n" + 
						"  \"intent\": \"sale\",\n" + 
						"  \"payer\": {\n" + 
						"    \"payment_method\": \"paypal\"\n" + 
						"  },\n" + 
						"  \"transactions\": [\n" + 
						"    {\n" + 
						"      \"amount\": {\n" + 
						"        \"total\": \"30.11\",\n" + 
						"        \"currency\": \"USD\",\n" + 
						"        \"details\": {\n" + 
						"          \"subtotal\": \"30.00\",\n" + 
						"          \"tax\": \"0.07\",\n" + 
						"          \"shipping\": \"0.03\",\n" + 
						"          \"handling_fee\": \"1.00\",\n" + 
						"          \"shipping_discount\": \"-1.00\",\n" + 
						"          \"insurance\": \"0.01\"\n" + 
						"        }\n" + 
						"      },\n" + 
						"      \"description\": \"The payment transaction description.\",\n" + 
						"      \"custom\": \"EBAY_EMS_90048630024435\",\n" + 
						"      \"invoice_number\": \"48787589673\",\n" + 
						"      \"payment_options\": {\n" + 
						"        \"allowed_payment_method\": \"INSTANT_FUNDING_SOURCE\"\n" + 
						"      },\n" + 
						"      \"soft_descriptor\": \"ECHI5786786\",\n" + 
						"      \"item_list\": {\n" + 
						"        \"items\": [\n" + 
						"          {\n" + 
						"            \"name\": \"hat\",\n" + 
						"            \"description\": \"Brown hat.\",\n" + 
						"            \"quantity\": \"5\",\n" + 
						"            \"price\": \"3\",\n" + 
						"            \"tax\": \"0.01\",\n" + 
						"            \"sku\": \"1\",\n" + 
						"            \"currency\": \"USD\"\n" + 
						"          },\n" + 
						"          {\n" + 
						"            \"name\": \"handbag\",\n" + 
						"            \"description\": \"Black handbag.\",\n" + 
						"            \"quantity\": \"1\",\n" + 
						"            \"price\": \"15\",\n" + 
						"            \"tax\": \"0.02\",\n" + 
						"            \"sku\": \"product34\",\n" + 
						"            \"currency\": \"USD\"\n" + 
						"          }\n" + 
						"        ],\n" + 
						"        \"shipping_address\": {\n" + 
						"          \"recipient_name\": \"Brian Robinson\",\n" + 
						"          \"line1\": \"4th Floor\",\n" + 
						"          \"line2\": \"Unit #34\",\n" + 
						"          \"city\": \"San Jose\",\n" + 
						"          \"country_code\": \"US\",\n" + 
						"          \"postal_code\": \"95131\",\n" + 
						"          \"phone\": \"011862212345678\",\n" + 
						"          \"state\": \"CA\"\n" + 
						"        }\n" + 
						"      }\n" + 
						"    }\n" + 
						"  ],\n" + 
						"  \"note_to_payer\": \"Contact us for any questions on your order.\",\n" + 
						"  \"redirect_urls\": {\n" + 
						"    \"return_url\": \"https://example.com/return\",\n" + 
						"    \"cancel_url\": \"https://example.com/cancel\"\n" + 
						"  }\n" + 
						"}";
				HttpEntity<?> httpEntity = new HttpEntity<>(jsonRequestBody, httpHeaders);
				log.info(" The jsonRequestBody "+ jsonRequestBody);
				ResponseEntity<Map> postResponse =  restTemplate.exchange
						(createPaymentURL, HttpMethod.POST, httpEntity, Map.class);
				log.info(" The Post Response is "+ postResponse.getBody());
				return new GsonBuilder().create().toJson(postResponse.getBody());
	    }
	    
	    
	    private final String listAllPayments(String accessToken) {
		      log.info(" accessToken returned from  Auth Service is "+ accessToken);
		      String allPaymentsURL = servicesHelper.getPaymentsURL()+"?count=10&start_index=0&sort_by=create_time&sort_order=desc";
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				log.info(" accessToken  "+ accessToken);
				httpHeaders.add("Authorization", "Bearer "+accessToken);
				//httpHeaders.add("Host", "api.pitneybowes.com");
				HttpEntity<?> httpEntity = new HttpEntity<>(null, httpHeaders);
				ResponseEntity<Map> postResponse =  restTemplate.exchange
						(allPaymentsURL, HttpMethod.GET, httpEntity, Map.class);
				log.info(" The Post Response is "+ postResponse.getBody());
				return new GsonBuilder().create().toJson(postResponse.getBody());
	    }
	    
	    
	    /**
	     * 
	     * @param accessToken
	     * @return
	     */
		public final String callInvoiceService(String accessToken) {
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
