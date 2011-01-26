package esg.idp.openid.web;

/**
 * Bean holding data for form-based openid authentication.
 */
public class OpenidLoginFormBean {
	
	/**
	 * Password entered in form.
	 */
	private String password;			
	
	/** Flag to remember user's openid in cookie */
	private boolean rememberOpenid = false;

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