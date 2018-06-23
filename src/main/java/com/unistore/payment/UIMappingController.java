/**
 * 
 */
package com.unistore.payment;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kiran Kanaparthi
 *
 */
@Controller
public class UIMappingController {

	  private static final Logger log = LoggerFactory.getLogger(UIMappingController.class);

	@RequestMapping(path="/address",method=RequestMethod.GET)
	//,produces= MediaType.APPLICATION_JSON_UTF8_VALUE
			public String address(Map<String, Object> model) {
			model.put("message", "hello test");
			log.info(" Started UIMappingController->address  ");
			// return "address";
		      RestTemplate restTemplate = new RestTemplate();
		      ResponseEntity<String> jsonResponse = restTemplate.getForEntity
		    		  ("http://gturnquist-quoters.cfapps.io/api/random", String.class);
		      log.info("The Json Response from Service is "+ 
		    		  jsonResponse.toString());
		      
				model.put("message", "test");
				return "address";
	}
	

	
	@RequestMapping(path="/my-account",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String myAccount(String address) {
			  return "my-account";
	}
	
	@RequestMapping(path="/order-history",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String orderHistory(String address) {
			  return "order-history";
	}
	
	@RequestMapping(path="/payment",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String payment(String address) {
			  return "payment";
	}
	
	@RequestMapping(path="/product",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String productPage(String address) {
			  return "product";
	}
	
	@RequestMapping(path="/products",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String products(String address) {
			  return "products";
	}
	
	@RequestMapping(path="/shopping-cart",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String shoppingCart(String address) {
			  return "shopping-cart";
	}
	
	@RequestMapping(path="/thank-you",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String thankYou(String address) {
			  return "thank-you";
	}
	
	@RequestMapping(path="/welcome",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String welcome(String address) {
			  return "welcome";
	}
	
	
}
