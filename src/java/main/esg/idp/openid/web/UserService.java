package esg.idp.openid.web;

/**
 * API for retrieving basic user information, transmitted in OpenID attributes exchange.
 * 
 * @author luca.cinquini
 *
 */
public interface UserService {
	
	User getUserByOpenid(String openid);

}
