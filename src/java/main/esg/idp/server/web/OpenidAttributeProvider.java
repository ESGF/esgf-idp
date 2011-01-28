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

import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.Attribute;
import org.openid4java.util.AttributeProvider;
import org.openid4java.util.AttributeProviderException;
import org.openid4java.util.ConfigException;
import org.openid4java.util.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of @see AttributeProvider that retrieves the user information from a {@link UserService} collaborator.
 * The encoding of attributes for OpenID attributes exchange is delegated to the utility class {@link OpenidAxUtils}.
 */
@Service
public class OpenidAttributeProvider implements AttributeProvider {
	
	/**
	 * Collaborator used to retrieve user information.
	 */
	private final UserService userService;
	
	//private static final Log LOG = LogFactory.getLog(OpenidAttributeProvider.class);
	
	@Autowired	
	public OpenidAttributeProvider(final UserService userService) {
		this.userService = userService;
	}

	public Attribute[] getAttributes(final String openid, final ParameterList parameterList) throws AttributeProviderException, ConfigException {
				
		final User user = userService.getUserByOpenid(openid);
		if (user==null) throw new AttributeProviderException("User with openid="+openid+" not found");
		return OpenidAxUtils.getAttributes(user, parameterList);
			
	}

	public void initialize(NameValuePair[] arg0) throws ConfigException {
		// no initialization necessary
	}

}
