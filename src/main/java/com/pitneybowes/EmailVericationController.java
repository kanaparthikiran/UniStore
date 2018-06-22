/**
 * 
 */
package com.pitneybowes;

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

/**
 * @author Kiran Kanaparthi
 *
 */
@RestController
@RequestMapping(UniStoreServicesConstants.EMAIL_VERIFICATION_PATH)
public class EmailVericationController {

	  private static final Logger log = LoggerFactory.getLogger(EmailVericationController.class);

	    @Autowired
	    AuthenticationUtil authenticationUtil;
	    
		@RequestMapping(path="/verify",method=RequestMethod.GET,
		produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
		public String verifyEmail(String emailAddress) {
	      String accessToken = authenticationUtil.getAccessToken();
	      log.info(" accessToken returned from  Auth Service is "+ accessToken);
	      String avsResponse = callEmailService(accessToken,emailAddress);
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
		public final String callEmailService(String accessToken,String emailAddress) {
		    RestTemplate restTemplate = new RestTemplate();
		    String emailValidationURL = authenticationUtil.getEmailValidationURL();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			log.info(" accessToken  "+ accessToken);
			httpHeaders.add("Authorization", "Bearer "+accessToken);
			httpHeaders.add("Host", "api.pitneybowes.com");

			String emailServiceRequest = "{\n" + 
					"  \"options\": {     \n" + 
					"  },\n" + 
					"  \"Input\": {\n" + 
					"    \"Row\": [\n" + 
					"      {\n" + 
					"        \"rtc\": \"\",\n" + 
					"        \"bogus\": \"\",\n" + 
					"        \"role\": \"\",\n" + 
					"        \"emps\": \"\",\n" + 
					"        \"fccwireless\": \"\",\n" + 
					"        \"language\": \"\",\n" + 
					"	   \"complain\": \"\",\n" + 
					"        \"disposable\": \"\",\n" + 
					"        \"atc\": \"\",\n" + 
					"        \"emailAddress\": \""+emailAddress+"\",\n" + 
					"        \"rtc_timeout\": \"\",\n" + 
					"	    \"user_fields\":[\n" + 
					"	   	  {	\n" + 
					"				\"name\": \"User1\",\n" + 
					"				\"value\": \"Value1\"\n" + 
					"	   	  }	 \n" + 
					"	    ]\n" + 
					"      }\n" + 
					"\n" + 
					"    ]\n" + 
					"  }\n" + 
					"}";
			
			log.info(" The final Json to be passed to Email Service is "+emailServiceRequest);

			HttpEntity<?> httpEntity = new HttpEntity<>(emailServiceRequest, httpHeaders);
			ResponseEntity<Map> postResponse =  restTemplate.exchange
					(emailValidationURL, HttpMethod.POST, httpEntity, Map.class);
			log.info(" The Post Response is "+ postResponse.getBody());
			return new GsonBuilder().create().toJson(postResponse.getBody());
		}
}
