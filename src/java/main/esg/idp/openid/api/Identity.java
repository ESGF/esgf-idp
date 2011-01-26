package esg.idp.openid.api;

/**
 * Interface representing a user's identity.
 * 
 * @author luca.cinquini
 *
 */
public interface Identity {
	
	/**
	 * The openid that uniquely identifies users throughout the federation.
	 * @return
	 */
	public String getOpenid();
	
	/**
	 * The user nickname, not guaranteed to be unique across the federation.
	 * @return
	 */
	public String getUsername();
	
}
