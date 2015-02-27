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
package esg.idp.server.web;


/**
 * Common parameters for OpenID authentication.
 */
public class OpenidPars {
	
	// Attributes used in openid exchange (in the order they are supported)
	public final static String[] AX_FIRST_NAME = new String[] { "http://axschema.org/namePerson/first",
	                                                            "http://openid.net/schema/namePerson/first" };
	
	public final static String[] AX_LAST_NAME = new String[] { "http://axschema.org/namePerson/last",
	                                                           "http://openid.net/schema/namePerson/last" };
	
	public final static String[] AX_EMAIL = new String[] { "http://axschema.org/contact/email",
	                                                       "http://openid.net/schema/contact/internet/email",
		                                                   "http://schema.openid.net/contact/email" };
	
	public final static String[] AX_NICK_NAME = new String[] { "http://axschema.org/namePerson/friendly",
                                                               "http://schema.openid.net/namePerson/friendly"};
	
	// HTTP parameters for this application
	public final static String PARAMETER_STATUS = "status";
	public final static String PARAMETER_STATUS_VALUE = "done";
	public final static String PARAMETER_OPENID = "openid_identifier";
	public final static String PARAMETER_REMEMBERME = "remember_openid";
	
	// HTTP parameters specified by OpenID protocol
	public final static String OPENID_CLAIMED_ID = "openid.claimed_id";
	public final static String OPENID_IDENTITY = "openid.identity";
	public final static String OPENID_REALM = "openid.realm";
	public final static String OPENID_ENDPOINT = "openid.op_endpoint";
	
	// HTTP cookies
	public final static String OPENID_COOKIE_NAME = "esgf.idp.cookie";
	public final static int OPENID_COOKIE_LIFETIME = 86400*365*10; // ten years
	
	// HTTP session attributes
	public final static String SESSION_ATTRIBUTE_OPENID = "esgf.idp.openid";
	public final static String SESSION_ATTRIBUTE_AUTHENTICATED = "esgf.idp.authenticated";
	public final static String SESSION_ATTRIBUTE_PARAMETERLIST = "esgf.idp.parameterlist";
	
	// possible openid modes
	public final static String OPENID_MODE = "openid.mode";
	public final static String OPENID_MODE_ASSOCIATE = "associate";
	public final static String OPENID_CHECKID_SETUP = "checkid_setup";
	public final static String OPENID_CHECKID_IMMEDIATE = "checkid_immediate";
	public final static String OPENID_CHECK_AUTHENTICATION = "check_authentication";
	
	/* kltsa 03/06/2014  : General form of OP identifier, claimed id for user in identifier_select mode
	 *                     and name of session attribute containing the openid of user found in  
	 *                     database.
	 */
	public final static String ESGF_OP_DEFAULT_IDENTIFIER_URL = "esgf-idp/openid/";
	public final static String ESGF_OP_DEFAULT_IDENTIFIER_URL_WS = "esgf-idp/openid";
	public final static String DEFAULT_IDENTIFIER_SELECT_CLAIMED_ID = "identifier_select";
	public final static String IDENTIFIER_SELECT_STORED_USER_CLAIMED_ID = "user_database_openid";
	/*public final static String IDENTIFIER_SELECT_LOGIN_FORM_USERNAME_TEXTFIELD_ID = "login_form_username";*/

}
