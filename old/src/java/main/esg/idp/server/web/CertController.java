package esg.idp.server.web;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to retrieve user certificate through HTTPS basic authentication.
 * 
 * @author Luca Cinquini
 *
 */
public class CertController {
    
    final String scriptpath;
    
    private final Log LOG = LogFactory.getLog(this.getClass());
    
    /**
     * @param scriptpath: file system path of myproxy-logon script
     */
    public CertController(String scriptpath) {
        this.scriptpath = scriptpath;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String doGet(final HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        // verify SSL
        // Note: the upstream servlet configuraton redirects insecure requests BEFORE this code is invoked
        if (!request.isSecure()) {
            String url = request.getRequestURL().toString()
                        .replace("http:", "https:")
                        .replace("8080", "8443");
            LOG.warn("Insecure request, redirecting to: "+url);
            response.sendRedirect(url);
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            return "URL must be invoked via 'https' protocol.";
        }
        
        // verify script exists
        if (!(new File(scriptpath).exists())) {
            LOG.warn("Script : "+scriptpath+" does NOT exist, aborting.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return("MyProxy script NOT found, please contact the site administrator.");
        }
        
        // extract Basic authentication information
        // example 'Authorization' header with Basic authentication:
        // Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
        // (username:password encoded as Base64)
        String header = request.getHeader("Authorization"); 
        if (!StringUtils.hasText(header)) {
            response.setHeader("WWW-Authenticate", "BASIC realm=\"ESGF\"");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return "Basic authentication header not found.";
        }
        assert header.substring(0, 6).equals("Basic ");
        // will contain "QWxhZGRpbjpvcGVuIHNlc2FtZQ=="
        String basicAuthEncoded = header.substring(6);
        // will contain "username:password"
        String basicAuthAsString = new String(new Base64().decode(basicAuthEncoded.getBytes()));
        // https://esg-datanode.jpl.nasa.gov/esgf-idp/openid/<username>:<password>
        // or:
        // <username>:<password>
        String[] parts = basicAuthAsString.split(":");
        String username = null;
        String password = null;
        if (basicAuthAsString.startsWith("https:")) {
            username = parts[0]+":"+parts[1];
            password = parts[2];
        } else {
           username = parts[0];
           password = parts[1];
        } 
        // choose unique temporary filename
        String outputfile = "/tmp/"+System.currentTimeMillis()+".pem";
        
        // execute myproxy script
        String command = this.scriptpath + " " + username + " " + password + " " + outputfile;
        try {
            
            execute(command);
            
            // return certificate
            String cert = FileUtils.readFileToString(new File(outputfile));
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            return cert;  
            
        } catch(Exception e) {
            LOG.warn(e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return e.getMessage();
        }    

    }
    
    private String execute(String command) throws Exception {
        
        // execute the command
        //if (LOG.isInfoEnabled()) LOG.info("Executing command: "+command);
        String[] args = command.split("\\s+");
        
        ProcessBuilder builder = new ProcessBuilder(args);
        builder.redirectErrorStream(true); // redirect standard error to standard output
        Process process = builder.start();
        
        String line;
        InputStream in = process.getInputStream();
        BufferedInputStream buf = new BufferedInputStream(in);
        InputStreamReader inread = new InputStreamReader(buf);
        BufferedReader bufferedreader = new BufferedReader(inread);
        StringBuffer sb = new StringBuffer();
        while ((line = bufferedreader.readLine ()) != null) {
            sb.append(line);
        }
        
        // check for error
        try {
            if (process.waitFor() != 0) {
                LOG.warn("Error : "+sb.toString());
                throw new Exception("Error: "+sb.toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOG.warn("An error occurred: "+sb.toString());
        } finally {
            bufferedreader.close();
            inread.close();
            buf.close();
            in.close();
        }
        
        return sb.toString();
        
    }

}
