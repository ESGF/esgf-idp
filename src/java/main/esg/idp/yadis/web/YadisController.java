package esg.idp.yadis.web;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import esg.idp.yadis.api.YadisService;

/**
 * Controller for OpenID Yadis discovery:
 * this controller processes all requests sent to the Gateway Yadis servlet,
 * and returns the Gateway OpenID provider endpoint embedded within an XML document.
 *
 */
@Controller
public class YadisController {

	/**
	 * Service that generates the Yadis XML document.
	 */
	@Autowired
	private YadisService yadisService;
	
	@RequestMapping(method = { RequestMethod.GET } )
	public ModelAndView process(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		// extract userName from OpenID URL
		final String openid = request.getRequestURL().toString();
				
		try {
			
			final String xml = yadisService.discover(openid);
			
	        // write response to output stream
			response.setContentType("application/xrds+xml");
	        ServletOutputStream os = response.getOutputStream();
	        os.write(xml.getBytes());
	        os.close();   
			return null;
		
		} catch(IllegalArgumentException e) {
			
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid openid: "+openid);
			return null;
			
		}
	
	}

	
}
