package esg.idp.openid.web;

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
