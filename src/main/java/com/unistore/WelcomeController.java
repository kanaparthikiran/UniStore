package com.unistore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.google.gson.GsonBuilder;
import com.unistore.constants.UniStoreServicesConstants;
import com.unistore.util.HttpUtils;
import com.unistore.util.PitneyBowesServicesHelper;

@Controller
public class WelcomeController {

    private static final Logger log = LoggerFactory.getLogger(WelcomeController.class);

    private static final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    PitneyBowesServicesHelper servicesHelper;
    

	  
    @Autowired
    HttpUtils httpUtils;
    
    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		
      RestTemplate restTemplate = new RestTemplate();
//      ResponseEntity<String> jsonResponse = restTemplate.getForEntity
//    		  ("http://gturnquist-quoters.cfapps.io/api/random", String.class);
//      log.info("The Json Response from Service is "+ 
//    		  jsonResponse.toString());
//      try {
//		log.info(" Client IP Address is "+httpUtils.getClientIp());
//	} catch (Exception e) {
//		e.printStackTrace();
	
      String accessToken = servicesHelper.getCachedToken
    		  (PitneyBowesServicesHelper.class,UniStoreServicesConstants.EXPIRES_IN_PITNEY_BOWES);
//      String accessToken = servicesHelper.getCachedToken
//    		  (PaypalServicesHelper.class,UniStoreServicesConstants.EXPIRES_IN_PAYPAL);
      log.info(" accessToken returned from  Auth Service is "+ accessToken);
      String ipAddress = httpUtils.getClientIp();
      String geoCodeList = callGeoCodeURL(accessToken,ipAddress);
     // this.message = jsonResponse.toString();
		//model.put("message", this.message);
      String response = " Customer City is Determined as "+geoCodeList;
      log.info(" response  "+response);
      model.put("message", response);
	  return "welcome";
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return
	 */
    private final String callGeoCodeURL(String accessToken,String ipAddress) {
    	// log.info(" accessToken returned from  Auth Service is "+ accessToken);
	      String geoLocationURL = servicesHelper.getGeoLocationURL()+"?ipAddress="+ipAddress;
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			//	log.info(" accessToken  "+ accessToken);
			httpHeaders.add("Authorization", "Bearer "+accessToken);
			//httpHeaders.add("Host", "api.pitneybowes.com");
			HttpEntity<?> httpEntity = new HttpEntity<>(null, httpHeaders);
			ResponseEntity<Map> postResponse =  restTemplate.exchange
					(geoLocationURL, HttpMethod.GET, httpEntity, Map.class);
			//	log.info(" The Post Response is "+ postResponse.getBody());
			Map<Object,Object> geoLocationMap = postResponse.getBody();
			//	log.info("  geoLocationMap  "+ geoLocationMap);
			Object geoLocationCoordinates = ((Map)geoLocationMap.get("geometry")).get("coordinates");
		//	log.info("  geoLocationCoordinates class "+ geoLocationCoordinates.getClass().getName());
			//		log.info("  (Map)geoLocationMap.get(\"geometry\")).get(\"coordinates\") "+ ((Map)geoLocationMap.get("geometry")).get("coordinates"));
			List latLongCoordinates = (ArrayList)geoLocationCoordinates;
			//	log.info("  latLongCoordinates.get(0)  "+ latLongCoordinates.get(0).getClass().getName());
			Double lattitude = (Double) latLongCoordinates.get(0);
			Double longitude = (Double) latLongCoordinates.get(1);

		    String getCodeURL = servicesHelper.getGeoCodeURL()+"?x="+lattitude+"&y="+longitude;

			ResponseEntity<Map> getCodeURLResponse =  restTemplate.exchange
						(getCodeURL, HttpMethod.GET, httpEntity, Map.class);

			Map<Object,Object> geoCodeMap = postResponse.getBody();
			//	log.info("  ((Map)((Map)(geoCodeMap.get(\"ipInfo\"))).get(\"place\")).get(\"city\")  "+ 
			//	((Map)((Map)(geoCodeMap.get("ipInfo"))).get("place")).get("city").getClass().getName());
			Map<Object,Object> placeMap = null;
					//((Map)((Map)(geoCodeMap.get("ipInfo"))).get("place")).get("city");
			//	log.info("  keyset  "+placeMap+"  Place instance is "+((Map)((Map)(geoCodeMap.get("ipInfo"))).get("place")).get("city"));
			String cityValue = (String) ((Map)((Map)((Map)(geoCodeMap.get("ipInfo"))).get("place")).get("city")).get("value");
			String stateValue = (String) ((Map)((Map)((Map)(geoCodeMap.get("ipInfo"))).get("place")).get("state")).get("value");
			String countryValue = (String) ((Map)((Map)((Map)(geoCodeMap.get("ipInfo"))).get("place")).get("country")).get("value");
			
			//log.info("  address  "+address+" geoCodeMap  "+geoCodeMap);
			return new GsonBuilder().create().toJson(cityValue);
    }

}