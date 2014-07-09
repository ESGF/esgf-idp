package esg.idp.server.web;

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import esg.security.attr.service.api.FederatedAttributeService;

@Controller
/**
 * Example controller to view a personal user account.
 */
public class AccountController {
    
    /**
     * View name.
     */
    private String view = "/idp/account";
    
    private final static String ATTRIBUTES = "attributes";
    
    private final FederatedAttributeService service;

    @Autowired
    public AccountController(FederatedAttributeService service) {    
        this.service = service;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest request) throws Exception {
        
        // user openid is retrieved from the session
        final String openid = (String)request.getSession().getAttribute(OpenidPars.SESSION_ATTRIBUTE_OPENID);
        if (!StringUtils.hasText(openid)) {
            throw new ServletException("User is not logged in.");
        }
        
        // retrieve federation-wide user attributes
        final Map<String, Set<String>> attributes = service.getAttributes(openid);
        
        // return to view
        final ModelAndView mav = new ModelAndView(view);
        mav.getModel().put(ATTRIBUTES, attributes);
        return mav;
        

    }

}
