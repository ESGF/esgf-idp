package esg.idp.openid.impl;

/**
 * Java bean implementation of Identity interface.
 * This implementation sets the username to be the ending part of the user's openid.
 */
import esg.idp.openid.api.Identity;

public class IdentityImpl implements Identity {
	
	private final String openid;
	
	private final String username;
	
	public IdentityImpl(final String openid) {
		this.openid = openid;
		this.username = openid.substring(openid.lastIndexOf("/")+1);
	}

	@Override
	public String getOpenid() {
		return openid;
	}

	@Override
	public String getUsername() {
		return username;
	}



}
