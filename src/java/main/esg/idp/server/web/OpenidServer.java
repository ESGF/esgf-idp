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

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.DirectError;
import org.openid4java.message.Message;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.server.InMemoryServerAssociationStore;
import org.openid4java.server.ServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Web controller executing OpenID server functionality.
 * This controller must be able to process GET/POST HTTP requests.
 */
@Controller("openidServer")
//@RequestMapping("/idp/openidServer.htm")
public class OpenidServer {
	
	/**
	 * URL of OpenID server (this controller), needed by the ServerManager.
	 */
	private String openidServerUrl;
	
	/** URL of OpenID provider login view */
	private String loginUrl = "/idp/login.htm";
	
	/** Openid back-end service. */
	private ServerManager manager;
	
	private static final Log LOG = LogFactory.getLog(OpenidServer.class);
	
	@Autowired
	public OpenidServer(final ServerManager manager) {
		this.manager = manager;	
	}
	
	@Autowired
	public void setOpenidServerUrl(final @Value("${idp.service.endpoint}") String openidServerUrl) {
		this.openidServerUrl = openidServerUrl;
	}
	
	/**
	 * Method that configures the {@link ServerManager}.
	 */
	@PostConstruct
	public void init() {
		
		manager.setOPEndpointUrl(openidServerUrl);
		manager.setSharedAssociations(new InMemoryServerAssociationStore());
		manager.setPrivateAssociations(new InMemoryServerAssociationStore());
		if (LOG.isDebugEnabled()) LOG.debug("Configuring ServerManager with OpenID Server URL: "+openidServerUrl);
		
		// fields signed by the Openid Server
		manager.setSignFields("return_to,assoc_handle,claimed_id,identity,ns.ext1"); // OpenID 1.x
		manager.setSignExtensions(new String[]{AxMessage.OPENID_NS_AX});
		
	}

	/** 
	 * Method to process GET/POST requests. 
	 * This method delegates to other request handling modes depending on the HTTP parameter "openid.mode". 
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD } )
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		if (LOG.isDebugEnabled()) LOG.debug("OpenidServer: session="+session.getId());

		// retrieve HTTP parameters from request or session
        final ParameterList parameterList = getParameterList(request);
      
        final String mode = parameterList.getParameterValue(OpenidPars.OPENID_MODE);
    	        
        // switch on openid.mode parameter
        if (LOG.isDebugEnabled()) LOG.debug("OpenidServer: request mode="+mode);
        
        // openid.mode=associate
        if (OpenidPars.OPENID_MODE_ASSOCIATE.equals(mode)) {
        	return handleAssociate(request, response);
        	            
        // openid.mode=checkid_setup, checkid_immediate
        } else if (OpenidPars.OPENID_CHECKID_SETUP.equals(mode) || OpenidPars.OPENID_CHECKID_IMMEDIATE.equals(mode)) {
        	return handleCheckId(request, response);
            
        // openid.mode=check_authentication.
        } else if (OpenidPars.OPENID_CHECK_AUTHENTICATION.equals(mode)) {
        	return handleCheckAuthentication(request, response);
            
        // openid.mode=?
        } else {
            return handleError(request, response);
            
        }
		
	}
	
	
	/**
	 * openid.mode=associate.
	 * This method processes an association request to establish shared keys. 
	 * @param request
	 * @param parameterList
	 * @return
	 */
	private ModelAndView handleAssociate(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		
		final ParameterList parameterList = getParameterList(request);
		final Message message = manager.associationResponse(parameterList);
		return sendResponse(response, message.keyValueFormEncoding());
		
	}
	
	/**
	 * openid.mode=check_authentication
	 * This method processes an openid verification request
	 * @param request
	 * @param parameterList
	 * @return
	 */
	private ModelAndView handleCheckAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		
		final ParameterList parameterList = getParameterList(request);
		final Message message = manager.verify(parameterList);
		return sendResponse(response, message.keyValueFormEncoding());
        		
	}
	
	/**
	 * openid.mode=checkid_setup, checkid_immediate
	 * @param request
	 * @param parameterList
	 * @return
	 */
	private ModelAndView handleCheckId(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		
		final ParameterList parameterList = getParameterList(request);
        final String openid = parameterList.getParameterValue(OpenidPars.OPENID_IDENTITY);
        final String userClaimedId = parameterList.getParameterValue(OpenidPars.OPENID_CLAIMED_ID);
        HttpSession session = request.getSession();

		// user is authenticated
        final boolean loggedin = isUserLoggedIn(session, openid);
        if (LOG.isDebugEnabled()) LOG.debug("User authenticated status="+loggedin);
		if (loggedin) {
						                        
            // process the authentication request
            final Message message = manager.authResponse(parameterList, openid, userClaimedId, true);
            
            // purge parameter list from session so user can log in with another browser/openid
            session.removeAttribute(OpenidPars.SESSION_ATTRIBUTE_PARAMETERLIST);
            
            // send external redirect response
            return new ModelAndView( new RedirectView(((AuthSuccess)message).getDestinationUrl(true), false) );
			
		// user is not authenticated
		} else {
						
            session.setAttribute(OpenidPars.SESSION_ATTRIBUTE_PARAMETERLIST, parameterList);
            session.setAttribute(OpenidPars.SESSION_ATTRIBUTE_OPENID, openid);
                		
    		// send internal redirect response to login controller
    		return new ModelAndView( new RedirectView(loginUrl,true) );
			
		}
		
	}
	
	/**
	 * openid.mode=?
	 * This method process a request with unknown openid mode.
	 * @return
	 */
	private ModelAndView handleError(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final Message message = DirectError.createDirectError("Unknown openid.mode parameter in request");
        return sendResponse(response, message.keyValueFormEncoding());
	}
	
	/**
	 * Method to check if the user has been previously authenticated
	 * @param session
	 * @param openid
	 * @return
	 */
	private boolean isUserLoggedIn(final HttpSession session, final String openid) {
        if (   (session.getAttribute(OpenidPars.SESSION_ATTRIBUTE_AUTHENTICATED) != null) 
        	&& ( (Boolean)session.getAttribute(OpenidPars.SESSION_ATTRIBUTE_AUTHENTICATED) == Boolean.TRUE) 
            && (  ((String)session.getAttribute(OpenidPars.SESSION_ATTRIBUTE_OPENID)).equals(openid) ) ) return true;
        else return false;
	}
	
	/**
	 * Utility method to extract the openid parameters either from the request, or from the session
	 * (depending on whether the additional parameter "j_action" is found in the request.
	 * @param request
	 */
	private ParameterList getParameterList(final HttpServletRequest request) throws Exception {
		
        ParameterList parameterList = null;
        final String status = request.getParameter(OpenidPars.PARAMETER_STATUS);
        
        // request redirected from the openid controller with flag j_status=done
        if (StringUtils.hasText(status) && status.equals(OpenidPars.PARAMETER_STATUS_VALUE)) { 
        	
        	// retrieve openid parameters from HTTP session
    		final HttpSession session = request.getSession();
    		parameterList = (ParameterList)session.getAttribute(OpenidPars.SESSION_ATTRIBUTE_PARAMETERLIST);
        	
        // request sent from openid client
        } else {
        	
        	// retrieve openid parameters from HTTP request
        	parameterList = new ParameterList(request.getParameterMap());
        	
        }  
        if (parameterList==null) throw new Exception("Could not retrieve openid parameters from request/session");
        
        return parameterList;
		
	}
	
	/**
	 * Method to send an HTTP response with the given text
	 * @param response
	 * @param text
	 */
	private ModelAndView sendResponse(final HttpServletResponse response, final String text) throws Exception {
        final ServletOutputStream os = response.getOutputStream();
        os.write(text.getBytes());
        os.close();
        return null; // response object already completed
	}
	
	/**
	 * Setter method provided to change the login page URL, if needed
	 * @param loginUrl
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
}
