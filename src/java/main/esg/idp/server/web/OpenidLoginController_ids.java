package esg.idp.server.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.apache.commons.codec.binary.Base64;

import esg.idp.server.api.IdentityProvider;

@Controller
//@RequestMapping("/idp/login_ids.html")
public class OpenidLoginController_ids {
	
	@Autowired
	private IdentityProvider idp;
	
	/**
	 * URL of OpenidServer for redirection after successful authentication.
	 */
	private String serverUrl = "/idp/openidServer.htm";
	
	/**
	 * View name.
	 */
	private String view = "/idp/login_ids";
	private final static String LOGIN_COMMAND = "loginCommand_ids";
	private final static String CUSTOM_BASIC_HTTP_AUTH_HEADER = "Agent-type";
	private final static String BASIC_HTTP_AUTH_HEADER_VALUE = "cl";
	private static final Log LOG = LogFactory.getLog(OpenidLoginController_ids.class);

		
	/* kltsa 17/11/2014 changes for issue 23089: Signal idp service that user has been authenticated. */
	ModelAndView setPositiveSessionAuth(HttpSession session, String openid)
	{
	  /* kltsa 03/06/2014 : Stores the openid found in database for this user. */
	  session.setAttribute(OpenidPars.IDENTIFIER_SELECT_STORED_USER_CLAIMED_ID, openid);
					
	  // set session-scope authentication flag to TRUE
	  session.setAttribute(OpenidPars.SESSION_ATTRIBUTE_AUTHENTICATED, Boolean.TRUE);
	  if (LOG.isDebugEnabled()) LOG.debug("Authentication succeded");
			
	    // redirect to openid server for further processing
	  final String redirect = serverUrl + "?" + OpenidPars.PARAMETER_STATUS+"="+OpenidPars.PARAMETER_STATUS_VALUE; 
	  return new ModelAndView( new RedirectView(redirect, true) );
	}
	
	
	/* kltsa 08/08/2014 change for issue 23089 : Handles the initial get request from bash scripts. */
	private ModelAndView handleScriptGetReq(HttpServletResponse response, final String agent_type)
	{
	  if(agent_type.equals(BASIC_HTTP_AUTH_HEADER_VALUE))
	  { 	
	    response.setHeader("WWW-Authenticate", "Basic realm=\"ESGF\"");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	  }	  
	  return null; /* Do not return a view. */
	}
	
	
	/* Handles basic http auth header when sent. */
	private ModelAndView handleScriptBasicAuth(HttpServletRequest request, HttpServletResponse response)
	{
	  final HttpSession session = request.getSession();
	  String http_basic_auth = null, http_basic_auth_username = null, http_basic_auth_password = null, openid = null;
	  Boolean user_authenticated = false;
	  byte[] byteArray = null;
	  http_basic_auth = request.getHeader("Authorization");
	  
	  
	  String http_basic_auth_enc = http_basic_auth.replaceAll("Basic ", "");
	  byteArray = Base64.decodeBase64(http_basic_auth_enc.getBytes());
	  String http_basic_auth_dec = new String(byteArray);
	  String[] parts = http_basic_auth_dec.split(":");
	  http_basic_auth_username = parts[0];
	  http_basic_auth_password = parts[1];
	  
	  if (LOG.isDebugEnabled()) LOG.debug("Attempting authentication with user="+http_basic_auth_username+" password="+http_basic_auth_password);
		
	  user_authenticated = idp.authenticate_ids(http_basic_auth_username, http_basic_auth_password);
	  openid = idp.getOpenid(http_basic_auth_username);		
		
	  if((user_authenticated) && (openid != null)) 
	  {
		return setPositiveSessionAuth(session, openid);
	  } 
	  else
	  {
	    // set session-scope authentication flag to FALSE
		session.setAttribute(OpenidPars.SESSION_ATTRIBUTE_AUTHENTICATED, Boolean.FALSE);
		if (LOG.isDebugEnabled()) LOG.debug("Authentication error");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return null;
	  }
	}	
		
	
	/**
	 * GET method is invoked as redirect from the {@link OpenidServer}.
	 * It simply returns the view since the openid information is kept in the HTTP session.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView doGet(final HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
	  String http_basic_auth = null;
	  String agent_type = null;
	  
	  /* kltsa 08/08/2014 change for issue 23089 :Check if request contains http basic auth header
	   * and use this ,if exists,in order to authenticate the user.
	   */
	  agent_type = request.getHeader(CUSTOM_BASIC_HTTP_AUTH_HEADER);
	  http_basic_auth = request.getHeader("Authorization");
	  	  
	  if((agent_type != null) && (http_basic_auth == null))
	  {
		return handleScriptGetReq(response, agent_type);  
	  }
	  else if(http_basic_auth != null)
	  {	  
	    return handleScriptBasicAuth(request, response);
	  }	
	  else /* default request from html form. */
	  {
		// instantiate new form backing object
		final OpenidLoginFormBean_ids command = new OpenidLoginFormBean_ids();
					
		  // return to view
		final ModelAndView mav = new ModelAndView(view);
		mav.getModel().put(LOGIN_COMMAND, command);
					
		return mav;	
	  }  
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
	public ModelAndView doPost(@ModelAttribute(LOGIN_COMMAND) OpenidLoginFormBean_ids data, BindingResult errors, HttpServletRequest request) {
		
		final HttpSession session = request.getSession();
		final String username; 
		final String password; 
		String openid = null;
		Boolean user_authenticated = false;
		
				
		// user openid is retrieved from form 
		username =  data.getUsername(); /* a dict could be more useful ? */
		password =  data.getPassword();	/* user password is bound to the form backing object */	
		
						
		if (LOG.isDebugEnabled()) LOG.debug("Attempting authentication with user="+username+" password="+password);
		
		user_authenticated  = idp.authenticate_ids(username, password);
		openid = idp.getOpenid(username);		
		
		if((user_authenticated) && (openid != null)) 
		{
		  return setPositiveSessionAuth(session, openid);        
		} 
		else 
		{
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
