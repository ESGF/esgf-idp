package esg.idp.openid.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import esg.idp.openid.api.Identity;
import esg.idp.openid.api.IdentityProvider;
import esg.security.utils.xml.Parser;

/**
 * Implementation of {@link IdentityProvider} backed up by a local XML configuration file.
 * 
 * WARNING: this class is meant to be used for testing purposes only, do NOT use in a production environment.
 * 
 * @author luca.cinquini
 *
 */
@Service("identityProvider")
public class IdentityProviderLocalXmlImpl implements IdentityProvider {
	
	/**
	 * In-memory map of system users.
	 */
	final Map<String,String> users = new HashMap<String, String>();
		
	@Autowired
	public IdentityProviderLocalXmlImpl(final @Value("${idp.xmlfilepath}") String xmlfilepath) throws Exception {
		
		final File file = new ClassPathResource(xmlfilepath).getFile();
		parseXml(file);
	}

	@Override
	public boolean authenticate(String openid, String password) {
		
		if (users.containsKey(openid) && users.get(openid).equals(password)) return true;
		else return false;
		
	}

	@Override
	public Identity getIdentity(String openid) {
		
		if (users.containsKey(openid)) return new IdentityImpl(openid);
		else return null;
		
	}
	
	// method to parse the XML file containing the system users.
	void parseXml(final File file) throws IOException, JDOMException {
		
		final Document doc = Parser.toJDOM(file.getAbsolutePath(), false);
		final Element root = doc.getRootElement();
		
		for (final Object user : root.getChildren("user")) {
			final Element _user = (Element)user;
			final String openid = _user.getAttributeValue("openid");
			final String password =  _user.getAttributeValue("password");
			users.put(openid, password);
		}
		
	}

}
