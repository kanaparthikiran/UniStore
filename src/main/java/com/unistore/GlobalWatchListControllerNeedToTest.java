/**
 * 
 */
package com.unistore;

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
import com.unistore.util.PitneyBowesServicesHelper;

/**
 * @author Kiran Kanaparthi
 *
 */
@RestController
@RequestMapping(UniStoreServicesConstants.GLOBAL_WATCH_LIST_PATH)
public class GlobalWatchListControllerNeedToTest {

	  private static final Logger log = LoggerFactory.getLogger(GlobalWatchListControllerNeedToTest.class);

	    @Autowired
	    PitneyBowesServicesHelper authenticationUtil;
	    
		@RequestMapping(path="/verify",method=RequestMethod.GET,
		produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
		public String verifyWatchList(String emailAddress,String firstName,String lastName) {
	      String accessToken = authenticationUtil.getCachedToken(PitneyBowesServicesHelper.class,UniStoreServicesConstants.EXPIRES_IN_PITNEY_BOWES);
	      log.info(" accessToken returned from  Auth Service is "+ accessToken);
	      String avsResponse = callWatchListService(accessToken,firstName,lastName);
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
		public final String callWatchListService(String accessToken,String firstName,String lastName) {
		    RestTemplate restTemplate = new RestTemplate();
		    String emailValidationURL = authenticationUtil.getEmailValidationURL();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			log.info(" accessToken  "+ accessToken);
			httpHeaders.add("Authorization", "Bearer "+accessToken);
			httpHeaders.add("Host", "api.pitneybowes.com");

			String watchListRequest = "{\n" + 
					"	\"Input\": {\n" + 
					"		\"Row\": [{\n" + 
					"			\"FirstName\": \""+firstName+"\",\n" + 
					"			\"LastName\": \""+lastName+"\" \n" + 
					"		}]\n" + 
					"	}\n" + 
					"}";
			
//			//String emailServiceRequest = "{\n" + 
//					"  \"options\": {     \n" + 
//					"  },\n" + 
//					"  \"Input\": {\n" + 
//					"    \"Row\": [\n" + 
//					"      {\n" + 
//					"        \"rtc\": \"\",\n" + 
//					"        \"bogus\": \"\",\n" + 
//					"        \"role\": \"\",\n" + 
//					"        \"emps\": \"\",\n" + 
//					"        \"fccwireless\": \"\",\n" + 
//					"        \"language\": \"\",\n" + 
//					"	   \"complain\": \"\",\n" + 
//					"        \"disposable\": \"\",\n" + 
//					"        \"atc\": \"\",\n" + 
//					"        \"emailAddress\": \""+emailAddress+"\",\n" + 
//					"        \"rtc_timeout\": \"\",\n" + 
//					"	    \"user_fields\":[\n" + 
//					"	   	  {	\n" + 
//					"				\"name\": \"User1\",\n" + 
//					"				\"value\": \"Value1\"\n" + 
//					"	   	  }	 \n" + 
//					"	    ]\n" + 
//					"      }\n" + 
//					"\n" + 
//					"    ]\n" + 
//					"  }\n" + 
//					"}";
			log.info(" The final Json to be passed to Watch List Service is "+watchListRequest);

			HttpEntity<?> httpEntity = new HttpEntity<>(watchListRequest, httpHeaders);
			ResponseEntity<Map> postResponse =  restTemplate.exchange
					(emailValidationURL, HttpMethod.POST, httpEntity, Map.class);
			log.info(" The Post Response is "+ postResponse.getBody());
			return new GsonBuilder().create().toJson(postResponse.getBody());
		}

}
