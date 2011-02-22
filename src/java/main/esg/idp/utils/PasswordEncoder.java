package esg.idp.utils;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Utility class to encode a clear text password with MD5.
 * @author Luca Cinquini
 *
 */
public class PasswordEncoder {
	
	public final static void main(String[] args) {
		final String clearTextPassword = args[0];
		final String encryptedPassword = md5Hex(clearTextPassword);
		System.out.println("Encrypted Password="+encryptedPassword);
	}

}
