package esg.idp.openid.api;

/**
 * API for authenticating users. 
 * 
 * @author luca.cinquini
 *
 */
public interface IdentityProvider {
	
	/**
	 * Method to authenticate a user.
	 * @param openid
	 * @param password
	 * @return
	 */
	public boolean authenticate(final String openid, final String password);
	
	/**
	 * Method to retrieve the user's identity once the user has been authenticated,
	 * also used to verify the existence of a user with a given openid.
	 * 
	 * @param openid
	 * @return
	 */
	public Identity getIdentity(final String openid);

}
