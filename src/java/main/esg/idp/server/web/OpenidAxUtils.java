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

import java.util.ArrayList;
import java.util.List;

import org.openid4java.message.Parameter;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.Attribute;
import org.springframework.util.StringUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Class containing utilities for OpenID attributes exchange.
 * 
 * @author luca.cinquini
 *
 */
public class OpenidAxUtils {
	
	/**
	 * Method to encode the user information as openid exchange attributes.
	 * @param user
	 * @param parameterList
	 * @return
	 */
    public static Attribute[] getAttributes(final User user, final ParameterList parameterList) {
			
    	// list of attributes to be returned
		final List<Attribute> attributes = new ArrayList<Attribute>();
		
		// loop over attributes in parameters list
		for (final Object obj : parameterList.getParameters()) {
			final Parameter parameter = (Parameter)obj;
			
			addAttribute(attributes, parameter, OpenidPars.AX_FIRST_NAME, "firstname", user.getFirstName());
			addAttribute(attributes, parameter, OpenidPars.AX_LAST_NAME, "lastname", user.getLastName());
			addAttribute(attributes, parameter, OpenidPars.AX_EMAIL, "email", user.getEmail());
		
		}
		
		// cast list into typed array
		return attributes.toArray(new Attribute[0]);
		
	}


    /**
     * Method to add an attribute to the list.
     * 
     * @param attributes : the list of attributes to add to
     * @param par : the requested parameter
     * @param types : the possible matching types
     * @param alias : an alias for the attribute, if found
     * @param value : the attribute value to set
     */
	private static void addAttribute(final List<Attribute> attributes, final Parameter par, final String[] types, final String alias, final String value) {
		
		// loop over possible matching types
		for (final String type : types) {
			// match to requested attribute type
			if (par.getValue().equals(type)) {
				// do not send empty values
				if (StringUtils.hasText(value)) {
			         attributes.add( new Attribute(alias, type, Arrays.asList(new String[] { value } )) );
				}
			}
		}
		
    }

}
