package esg.idp.openid.web;


/**
 * Common parameters for OpenID authentication.
 */
public class OpenidPars {
	
	// Attributes used in openid exchange
	public final static String[] AX_FIRST_NAME = new String[] { "http://openid.net/schema/namePerson/first" };
	public final static String[] AX_LAST_NAME = new String[] { "http://openid.net/schema/namePerson/last" };
	public final static String[] AX_EMAIL = new String[] { "http://openid.net/schema/contact/internet/email",
		                                                   "http://schema.openid.net/contact/email",
		                                                   "http://axschema.org/contact/email" };
	
	// used
	public final static String PARAMETER_STATUS = "status";
	public final static String PARAMETER_STATUS_VALUE = "done";
	
	// parameters holding authentication state in HTTP session
	public final static String SESSION_ATTRIBUTE_OPENID = "esgf.idp.openid";
	public final static String SESSION_ATTRIBUTE_AUTHENTICATED = "esgf.idp.authenticated";
	public final static String SESSION_ATTRIBUTE_PARAMETERLIST = "esgf.idp.parameterlist";
	
	// possible openid modes
	public final static String OPENID_MODE = "openid.mode";
	public final static String OPENID_MODE_ASSOCIATE = "associate";
	public final static String OPENID_CHECKID_SETUP = "checkid_setup";
	public final static String OPENID_CHECKID_IMMEDIATE = "checkid_immediate";
	public final static String OPENID_CHECK_AUTHENTICATION = "check_authentication";

	
	// not used
	public final static String OPENID_IDENTIFIER = "openid_identifier";
	public final static String OPENID_COOKIE_NAME = "esgf.idp.cookie";
	public final static int OPENID_COOKIE_LIFETIME = 86400*365*10; // ten years
	

	
	
	
	public final static String PARAMETER_OPENID = "openid";
	public final static String PARAMETER_USERNAME = "j_username";
	public final static String PARAMETER_PASSWORD = "j_password";
	public final static String PARAMETER_REDIRECT = "redirectUrl";
	
	
	public final static String OPENID_CLAIMED_ID = "openid.claimed_id";
	public final static String OPENID_IDENTITY = "openid.identity";
	public final static String OPENID_REALM = "openid.realm";
	public final static String OPENID_ENDPOINT = "openid.op_endpoint";
		

	//public final static int UNLIMITED = 1000; // TODO: set to 0 when supported by Openid4Java

}
