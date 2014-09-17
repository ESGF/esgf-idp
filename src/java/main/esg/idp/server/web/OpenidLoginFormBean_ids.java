package esg.idp.server.web;

/**
 * Bean holding data for form-based openid authentication.
 */
public class OpenidLoginFormBean_ids {
	
	
	private String username;
	/**
	 * Password entered in form.
	 */
	private String password;	
	
	
	/** Flag to remember user's openid in cookie */
	private boolean rememberOpenid = false;

	public String getUsername() {
		return username;
	}

	public void setUsername(String login_form_username) {
		this.username = login_form_username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberOpenid() {
		return rememberOpenid;
	}

	public void setRememberOpenid(boolean rememberOpenid) {
		this.rememberOpenid = rememberOpenid;
	}

}
