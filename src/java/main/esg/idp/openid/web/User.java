package esg.idp.openid.web;

/**
 * Interface representing information about a system user.
 * 
 * @author luca.cinquini
 *
 */
public interface User {
	
	public String getOpenid();
	
	public String getFirstName();
	
	public String getLastName();
	
	public String getEmail();
	
	public String getUserName();

}
