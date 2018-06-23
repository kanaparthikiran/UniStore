package com.pitneybowes;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.pitneybowes.util.ServicesHelper;

@Controller
public class WelcomeController {

    private static final Logger log = LoggerFactory.getLogger(WelcomeController.class);

    @Autowired
    ServicesHelper authenticationUtil;
    
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
      ResponseEntity<String> jsonResponse = restTemplate.getForEntity
    		  ("http://gturnquist-quoters.cfapps.io/api/random", String.class);
      log.info("The Json Response from Service is "+ 
    		  jsonResponse.toString());
      
      String accessToken = authenticationUtil.getAccessToken();
      
      this.message = jsonResponse.toString();
		model.put("message", this.message);
		return "welcome";
	}

}