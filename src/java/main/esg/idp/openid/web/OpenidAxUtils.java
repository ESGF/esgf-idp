package esg.idp.openid.web;

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
