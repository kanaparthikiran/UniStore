/**
 * 
 */
package com.pitneybowes;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kiran Kanaparthi
 *
 */
@Controller
@RequestMapping("/addressVerification/v1")
public class AddressVerificationServiceController {

    private static final Logger log = LoggerFactory.getLogger(AddressVerificationServiceController.class);

    @Autowired
    AuthenticationUtil authenticationUtil;
    
    
    @RequestMapping("/verify")
	public String welcome(Map<String, Object> model) {
		
      RestTemplate restTemplate = new RestTemplate();
      String accessToken = authenticationUtil.getAccessToken();

		return "welcome";
	}

}
