package esg.idp.openid.web;

import org.springframework.stereotype.Service;

/**
 * Demo implementation of {@link UserService} that returns mock information based on the user's openid.
 * 
 * Warning: this class must not be used in production.
 * 
 * @author luca.cinquini
 *
 */
@Service
public class UserServiceImpl implements UserService {
	
	public User getUserByOpenid(String openid) {
		
		final UserBean user = new UserBean();
		final String userName = openid.substring(openid.lastIndexOf("/")+1);
		user.setUserName(userName);
		user.setFirstName(userName + " First Name");
		user.setLastName(userName + " Last Name");
		user.setEmail(userName + " Email");
		return user;
		
	}

}
