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
	
//	@RequestMapping("/")
//	public String welcome(Map<String, Object> model) {
//		
//      RestTemplate restTemplate = new RestTemplate();
//      ResponseEntity<String> jsonResponse = restTemplate.getForEntity
//    		  ("http://gturnquist-quoters.cfapps.io/api/random", String.class);
//      log.info("The Json Response from Service is "+ 
//    		  jsonResponse.toString());
//      
//      String accessToken = authenticationUtil.getCachedToken(PitneyBowesServicesHelper.class,UniStoreServicesConstants.EXPIRES_IN_PITNEY_BOWES);
//      
//      this.message = jsonResponse.toString();
//		model.put("message", this.message);
//		return "welcome";
//	}
	
	@RequestMapping(path="/my-account",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String myAccount(String address) {
			  return "This is My Account JSP";
	}
	
	@RequestMapping(path="/order-history",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String orderHistory(String address) {
			  return "This is Order History JSP";
	}
	
	@RequestMapping(path="/payment",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String payment(String address) {
			  return "This is Payment JSP";
	}
	
	@RequestMapping(path="/product",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String productPage(String address) {
			  return "This is Product Page JSP";
	}
	
	@RequestMapping(path="/products",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String products(String address) {
			  return "This is Products JSP";
	}
	
	@RequestMapping(path="/shopping-cart",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String shoppingCart(String address) {
			  return "This is Shopping Cart JSP";
	}
	
	@RequestMapping(path="/thank-you",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String thankYou(String address) {
			  return "This is Thank You JSP";
	}
	
//	@RequestMapping(path="/welcome",method=RequestMethod.GET,
//			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
//			public String welcome(String address) {
//			  return "This is Welcome JSP";
//	}
	
	
}
