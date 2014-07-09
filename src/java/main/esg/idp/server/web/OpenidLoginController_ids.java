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
public class OpenidLoginController_ids {
	
	@Autowired
	private IdentityProvider idp;
	
	/**
	 * URL of OpenidServer for redirection after successfull authentication.
	 */
	private String serverUrl = "/idp/openidServer.htm";
	
	/**
	 * View name.
	 */
	private String view = "/idp/login_ids";
	
	private final static String LOGIN_COMMAND = "loginCommand";
		
	private static final Log LOG = LogFactory.getLog(OpenidLoginController.class);

	/**
	 * GET method is invoked as redirect from the {@link OpenidServer}.
	 * It simply returns the view since the openid information is kept in the HTTP session.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView doGet(final HttpServletRequest request) throws Exception {
		
		// instantiate new form backing object
		final OpenidLoginFormBean_ids command = new OpenidLoginFormBean_ids();
		
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
	public ModelAndView doPost(@ModelAttribute(LOGIN_COMMAND) OpenidLoginFormBean_ids data, BindingResult errors, HttpServletRequest request) {
		
		final HttpSession session = request.getSession();
		StringBuilder openid = new StringBuilder();
		
		// user openid is retrieved from form 
		/*request.getParameter(OpenidPars.IDENTIFIER_SELECT_LOGIN_FORM_USERNAME_TEXTFIELD_ID); */
		final String username =  data.getLogin_form_username(); /* a dict could be more useful ? */
			
		// user password is bound to the form backing object
		final String password = data.getPassword();		
		if (LOG.isDebugEnabled()) LOG.debug("Attempting authentication with user="+username+" password="+password);
		
		if (idp.authenticate_user(username, password, openid)) 
		{
		  /* kltsa 03/06/2014 changes for issue 23061 : Store the openid found in database for this user. */
		  request.getSession().setAttribute(OpenidPars.IDENTIFIER_SELECT_STORED_USER_CLAIMED_ID, openid.toString());
						
			
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
