/**
 * 
 */
package com.unistore.payment;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kiran Kanaparthi
 *
 */
@RestController
public class UIMappingController {

	@RequestMapping(path="/address",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String address(String address) {
			  return "This is Address JSP";
	}
	
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
	
	@RequestMapping(path="/welcome",method=RequestMethod.GET,
			produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
			public String welcome(String address) {
			  return "This is Welcome JSP";
	}
	
	
}
