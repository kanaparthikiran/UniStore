/**
 * 
 */
package com.unistore.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kiran Kanaparthi
 * 
 *
 */
@Component
public class HttpUtils {

	  private static HttpServletRequest request;

	    @Autowired
	    public void setRequest(HttpServletRequest request) {
	        this.request = request;
	    }

	    public String getClientIp()  { // Returns the instance of InetAddress containing
	        // local host name and address
	        InetAddress localhost = null;
	        String systemipaddress = "";
			try {
				localhost = InetAddress.getLocalHost();
			
				System.out.println("System IP Address : " +
	                      (localhost.getHostAddress()).trim());
	        // Find public IP address
	       
	            URL url_name = new URL("http://bot.whatismyipaddress.com");
	 
	            BufferedReader sc =
	            new BufferedReader(new InputStreamReader(url_name.openStream()));
	 
	            // reads system IPAddress
	            systemipaddress = sc.readLine().trim();
	        }
	        catch (Exception e)
	        {
	            systemipaddress = "Cannot Execute Properly";
	        }
	        System.out.println("Public IP Address: " + systemipaddress +"\n");
	        return systemipaddress;
	    }
}
