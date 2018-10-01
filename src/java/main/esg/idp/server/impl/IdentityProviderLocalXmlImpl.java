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
package esg.idp.server.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import esg.idp.server.api.Identity;
import esg.idp.server.api.IdentityProvider;
import esg.security.utils.xml.Parser;

/**
 * Implementation of {@link IdentityProvider} backed up by a local XML configuration file.
 * 
 * WARNING: this class is meant to be used for testing purposes only, do NOT use in a production environment.
 * 
 * @author luca.cinquini
 *
 */
//@Service("identityProvider")
public class IdentityProviderLocalXmlImpl implements IdentityProvider {
	
	/**
	 * In-memory map of system users.
	 */
	final Map<String,String> users = new HashMap<String, String>();
		
	//@Autowired
	public IdentityProviderLocalXmlImpl(final @Value("${idp.xmlfilepath}") String xmlfilepath) throws Exception {
		
		final File file = new ClassPathResource(xmlfilepath).getFile();
		parseXml(file);
	}

	@Override
	public boolean authenticate(String openid, String password) {
		
		if (users.containsKey(openid) && users.get(openid).equals(password)) return true;
		else return false;
		
	}
	
	@Override
	public boolean authenticate_ids(String username, String password) {
	  return false;
	}
	
	
	@Override
	public String getOpenid(String username) {
	 return null;		
	}
	
	

	@Override
	public Identity getIdentity(String openid) {
		
		if (users.containsKey(openid)) return new IdentityImpl(openid);
		else return null;
		
	}
	
	// method to parse the XML file containing the system users.
	void parseXml(final File file) throws IOException, JDOMException {
		
		final Document doc = Parser.toJDOM(file.getAbsolutePath(), false);
		final Element root = doc.getRootElement();
		
		for (final Object user : root.getChildren("user")) {
			final Element _user = (Element)user;
			final String openid = _user.getAttributeValue("openid");
			final String password =  _user.getAttributeValue("password");
			users.put(openid, password);
		}
		
	}

}
