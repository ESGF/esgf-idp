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
//@RequestMapping("/idp/login.html")
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
	
	private final static String LOGIN_COMMAND = "loginCommand";
	private final static String CUSTOM_BASIC_HTTP_AUTH_HEADER = "Agent-type";
	private final static String BASIC_HTTP_AUTH_HEADER_VALUE = "cl";
		
	private static final Log LOG = LogFactory.getLog(OpenidLoginController_ids.class);

	/**
	 * GET method is invoked as redirect from the {@link OpenidServer}.
	 * It simply returns the view since the openid information is kept in the HTTP session.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView doGet(final HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// instantiate new form backing object
		final OpenidLoginFormBean_ids command = new OpenidLoginFormBean_ids();
		
		// return to view
		final ModelAndView mav = new ModelAndView(view);
		mav.getModel().put(LOGIN_COMMAND, command);
		
		
		/* kltsa 08/08/2014 change for issue 23089 :Check origin of the request, if from command line 
		 *                                          then return basic http auth. 
		 */
		
		String agent_type = null;
		agent_type = request.getHeader(CUSTOM_BASIC_HTTP_AUTH_HEADER);
		if(agent_type != null) 		
		{	
		  if(agent_type.contains(BASIC_HTTP_AUTH_HEADER_VALUE))
		  {	 
		    response.setHeader("WWW-Authenticate", "Basic realm=\"ESGF\"");
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		  }
		}
		
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
	public ModelAndView doPost(@ModelAttribute(LOGIN_COMMAND) OpenidLoginFormBean_ids data, BindingResult errors, HttpServletRequest request) {
		
		final HttpSession session = request.getSession();
		final String username; 
		final String password; 
		String http_basic_auth_username = null; 
		String http_basic_auth_password = null;
		String http_basic_auth = null;
		StringBuilder openid = new StringBuilder();
		
		

		/* kltsa 08/08/2014 change for issue 23089 :Check if request contains http basic  auth header
		 *                                          and use this ,if exists, instead of the data from
		 *                                          login form. 
		 */
		http_basic_auth = request.getHeader("Authorization");
		if(http_basic_auth != null)
		{	
		  byte[] byteArray = null; 
		  String http_basic_auth_enc = http_basic_auth.replaceAll("Basic ", "");
		  		  
		  byteArray = Base64.decodeBase64(http_basic_auth_enc.getBytes());
		    
		  String http_basic_auth_dec =  new String(byteArray);
			
		  String[] parts = http_basic_auth_dec.split(":");
		  http_basic_auth_username = parts[0]; 
		  http_basic_auth_password = parts[1]; 			
		}
		
		if(http_basic_auth_username == null && http_basic_auth_password == null)
		{
		  // user openid is retrieved from form 
		  username =  data.getLogin_form_username(); /* a dict could be more useful ? */
		  password = data.getPassword();	         /* user password is bound to the form backing object */	
		}
		else
		{
		  username =  http_basic_auth_username; 
		  password = http_basic_auth_password;  
		}
		
				
		if (LOG.isDebugEnabled()) LOG.debug("Attempting authentication with user="+username+" password="+password);
		
		if (idp.authenticate_user(username, password, openid)) 
		{
		  /* kltsa 03/06/2014 changes for issue 23061 : Stores the openid found in database for this user. */
		  session.setAttribute(OpenidPars.IDENTIFIER_SELECT_STORED_USER_CLAIMED_ID, openid.toString());
						
			
		  // set session-scope authentication flag to TRUE
		  session.setAttribute(OpenidPars.SESSION_ATTRIBUTE_AUTHENTICATED, Boolean.TRUE);
		  if (LOG.isDebugEnabled()) LOG.debug("Authentication succeded");
			
	      // redirect to openid server fir further processing
	      final String redirect = serverUrl + "?" + OpenidPars.PARAMETER_STATUS+"="+OpenidPars.PARAMETER_STATUS_VALUE; 
	      return new ModelAndView( new RedirectView(redirect, true) );
        
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
