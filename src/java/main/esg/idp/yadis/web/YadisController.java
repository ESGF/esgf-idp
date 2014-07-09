/*******************************************************************************
 * Copyright (c) 2011 Earth System Grid Federation
 * ALL RIGHTS RESERVED. 
 * U.S. Government sponsorship acknowledged.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package esg.idp.yadis.web;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import esg.idp.yadis.api.YadisService;

/**
 * Controller for OpenID Yadis discovery:
 * this controller processes all requests sent to the Gateway Yadis servlet,
 * and returns the Gateway OpenID provider endpoint embedded within an XML document.
 *
 */
@Controller
public class YadisController {

	/**
	 * Service that generates the Yadis XML document.
	 */
	@Autowired
	private YadisService yadisService;
	
	@RequestMapping(method = { RequestMethod.GET } )
	public ModelAndView process(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		// extract userName from OpenID URL
		final String openid = request.getRequestURL().toString();
				
		try {
			
			final String xml = yadisService.discover(openid);
						
	        // write response to output stream
			response.setContentType("application/xrds+xml");
						
	        ServletOutputStream os = response.getOutputStream();
	        os.write(xml.getBytes());
	        os.close();   
			return null;
		
		} catch(IllegalArgumentException e) {
			
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid openid: "+openid);
			return null;
			
		}
	
	}
	
}
