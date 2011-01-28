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
/**
 * 
 */
package esg.idp.server.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import esg.idp.server.api.IdentityProvider;

@Controller
//@RequestMapping("/idp/login.html")
public class OpenidLoginController {
	
	@Autowired
	private IdentityProvider idp;
	
	/**
	 * URL of OpenidServer for redirection after successfull authentication.
	 */
	private String serverUrl = "/idp/openidServer.htm";
	
	/**
	 * View name.
	 */
	private String view = "/idp/loginForm";
	
	private final static String LOGIN_COMMAND = "loginCommand";
		
	private static final Log LOG = LogFactory.getLog(OpenidLoginController.class);

	/**
	 * GET method is invoked as redirect from the {@link OpenidServer}.
	 * It simply returns the view since the openid information is kept in the HTTP session.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView doGet(final HttpServletRequest request) throws Exception {
		
		// instantiate new form backing object
		final OpenidLoginFormBean command = new OpenidLoginFormBean();
		
		// return to view
		final ModelAndView mav = new ModelAndView(view);
		mav.getModel().put(LOGIN_COMMAND, command);
		return mav;
		
	}


	/**
	 * POST method uses the password from the form and the openid from the session to authenticate the user.
	 * 
	 * @param data
	 * @param errors
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView doPost(@ModelAttribute(LOGIN_COMMAND) OpenidLoginFormBean data, BindingResult errors, HttpServletRequest request) {
		
		final HttpSession session = request.getSession();
		
		// user openid is retrieved from the session
		final String openid = (String)request.getSession().getAttribute(OpenidPars.SESSION_ATTRIBUTE_OPENID);
		// user password is bound to the form backing object
		final String password = data.getPassword();		
		if (LOG.isDebugEnabled()) LOG.debug("Attempting authentication with openid="+openid+" password="+password);
		
		if (idp.authenticate(openid, password)) {
			
			// set session-scope authentication flag to TRUE
			session.setAttribute(OpenidPars.SESSION_ATTRIBUTE_AUTHENTICATED, Boolean.TRUE);
			if (LOG.isDebugEnabled()) LOG.debug("Authentication succeded");
			
	        // redirect to openid server fir further processing
	        final String redirect = serverUrl + "?" + OpenidPars.PARAMETER_STATUS+"="+OpenidPars.PARAMETER_STATUS_VALUE; 
	        return new ModelAndView( new RedirectView(redirect, true) );
        
		} else {
			
			// set session-scope authentication flag to FALSE
			session.setAttribute(OpenidPars.SESSION_ATTRIBUTE_AUTHENTICATED, Boolean.FALSE);
			if (LOG.isDebugEnabled()) LOG.debug("Authentication error");
			
			errors.reject("error.invalid", new Object[] {}, "Invalid OpenID and/or Password combination");
			return new ModelAndView(view);
		
		}
	
	} 
	
	/**
	 * Setter method provided to change the openid server URL, if needed
	 * @param loginUrl
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	/**
	 * Setter method provided to change the view name, if needed.
	 * @param view
	 */
	public void setView(final String view) {
		this.view = view;
	}
	
}
