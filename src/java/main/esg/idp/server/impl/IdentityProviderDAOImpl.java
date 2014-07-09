package esg.idp.server.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import esg.common.util.ESGFProperties;
import esg.idp.server.api.Identity;
import esg.idp.server.api.IdentityProvider;
import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;
import esg.node.security.UserInfoDAO;

/**
 * Implementation of {@link IdentityProvider} that is backed up by a relational database.
 * @author Luca Cinquini
 *
 */
@Service("identityProvider")
public class IdentityProviderDAOImpl implements IdentityProvider {
	
	/**
	 * Database access class
	 */
	private UserInfoCredentialedDAO userInfoDAO = null;
	
	private final Log LOG = LogFactory.getLog(this.getClass());
	
	@Autowired
	public IdentityProviderDAOImpl(final @Qualifier("esgfProperties") ESGFProperties props) {
	    userInfoDAO = new UserInfoCredentialedDAO("rootAdmin", props.getAdminPassword(), props);
	}

	@Override
	public boolean authenticate(String openid, String password) {
		
		return userInfoDAO.checkPassword(openid, password);
		
	}
	
	/*kltsa 04/06/2014 */
	@Override
	public boolean authenticate_user(String username, String password, StringBuilder openid) {
	 return userInfoDAO.check_user_Password(username, password, openid);
		
	}

	@Override
	public Identity getIdentity(String openid) {
		
		final UserInfo user = userInfoDAO.getUserById(openid);
		if (user.isValid()) {
		    
			IdentityImpl identity = new IdentityImpl(openid, user.getUserName());
			identity.setFirstName(user.getFirstName());
			identity.setLastName(user.getLastName());
			identity.setEmail(user.getEmail());
			return identity;
			
		} else {
			return null;
		}
		
	}

}
