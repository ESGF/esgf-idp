package esg.idp.server.impl;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import esg.idp.server.api.Identity;
import esg.idp.server.api.IdentityProvider;
import esg.node.security.UserInfo;
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
	private UserInfoDAO userInfoDAO = null;
	
	@Autowired
	public IdentityProviderDAOImpl(final @Qualifier("dbProperties") Properties props) {
		this.userInfoDAO = new UserInfoDAO(props);
	}

	@Override
	public boolean authenticate(String openid, String password) {
		final boolean fixme = userInfoDAO.checkPassword(openid, password);
		System.out.println("fixme="+fixme);
		return fixme;
		
	}

	@Override
	public Identity getIdentity(String openid) {
		
		final UserInfo user = userInfoDAO.getUserById(openid);
		if (user.isValid()) {
			return new IdentityImpl(openid, user.getUserName());
		} else {
			return null;
		}
		
	}

}
