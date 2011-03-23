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
package esg.idp.yadis.impl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import esg.idp.server.api.IdentityProvider;
import esg.idp.yadis.api.YadisService;

/**
 * Implementation of {@link YadisService} backed up by an IdentityProvider 
 */
@Service("yadisService")
public class YadisServiceImpl implements YadisService {
	
	public final static String IDENTITY = "__IDENTITY__";
	
	//private final static Logger LOG = Logger.getLogger(YadisServiceImpl.class);
	
	// mandatory Identity Provider endpoint
	private URL idpProviderUrl = null;
	
	// optional ordered map of additional discovered endpoints
	private Map<String,String> endpoints = new LinkedHashMap<String, String>();
	
	// back-end IdenityProvider used to check existence of user with given openid
	final IdentityProvider idp;
	
	// XML document template with place-holder for the user's identity.
	private String xml;
	
	@Resource(name="yadisEndpoints")	
	public void setEndpoints(final Map<String,String> endpoints) {
		this.endpoints = endpoints;
	}
	
	@Autowired
	public YadisServiceImpl(final @Value("${idp.service.endpoint}") URL idpProviderUrl, final IdentityProvider idp) {
		this.idpProviderUrl = idpProviderUrl;
		this.idp = idp;
	}
	
	/**
	 * Method that builds a template XML using a place-holder for the user's identity.
	 * This method must be invoked after the constructor has been invoked,
	 * and the optional "endpoints" property has been set.
	 */
	@PostConstruct
	public void init() throws IOException, JDOMException {
		xml = buildXml(IDENTITY);
	}

	/**
	 * {@inheritDoc}
	 */
	public String discover(final String openid) throws IllegalArgumentException {
				
		// check user with given openid does exist
		if (idp.getIdentity(openid)==null) throw new IllegalArgumentException("Invalid openID: "+openid);
		
		// return Yadis document for existing user
		else return xml.replace(IDENTITY, openid);
		
	}
	
	/**
	 * Method to build the XML document template with the given user identity.
	 * @return
	 */
	String buildXml(final String openid) throws IOException, JDOMException {
		
		final StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		
		sb.append("<xrds:XRDS xmlns:xrds=\"xri://$xrds\" xmlns=\"xri://$xrd*($v*2.0)\">");
		sb.append("<XRD>");
		
		// insert mandatory IdP endpoint
		sb.append("<Service priority=\"0\">")
		  .append("<Type>http://specs.openid.net/auth/2.0/signon</Type>")
		  .append("<Type>http://openid.net/signon/1.0</Type>")
		  .append("<Type>http://openid.net/srv/ax/1.0</Type>")
		  .append("<URI>").append(idpProviderUrl.toString()).append("</URI>")
		  .append("<LocalID>").append(openid).append("</LocalID>")
		  .append("</Service>");
		
		// insert optional other service endpoints
		for (final String type : endpoints.keySet()) {
			sb.append("<Service priority=\"0\">")
			  .append("<Type>").append(type).append("</Type>")
			  .append("<URI>").append(endpoints.get(type)).append("</URI>")
			  .append("</Service>");
		}
		
		sb.append("</XRD>");
		sb.append("</xrds:XRDS>");
		
		// pretty-format the XML document
		return format(sb.toString());
		
	}
	
	/**
	 * Method to pretty-format the XML Yadis document
	 * @param xmlstring
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	private final static String format(String xmlstring) throws IOException, JDOMException {
		
		// get XML parser
		SAXBuilder builder = new SAXBuilder();
		//builder.setFeature("http://xml.org/sax/features/namespaces",true);
		
		// unformatted string > JDOM
	    final StringReader sr = new StringReader(xmlstring);
	    Document jdoc =  builder.build(sr);
	    
	    // JDOM > formatted string
	    Format format = Format.getPrettyFormat();
	  	XMLOutputter outputter = new XMLOutputter(format);
	     
	  	return outputter.outputString(jdoc);
	}

}
