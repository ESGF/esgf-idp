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
            return "";
        }
        
        // extract Basic authentication information
        // example 'Authorization' header with Basic authentication:
        // Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
        // (username:password encoded as Base64)
        String header = request.getHeader("Authorization"); 
        if (!StringUtils.hasText(header)) {
            response.setHeader("WWW-Authenticate", "BASIC realm=\"ESGF\"");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return "";
        }
        assert header.substring(0, 6).equals("Basic ");
        // will contain "QWxhZGRpbjpvcGVuIHNlc2FtZQ=="
        String basicAuthEncoded = header.substring(6);
        // will contain "username:password"
        String basicAuthAsString = new String(new Base64().decode(basicAuthEncoded.getBytes()));
        // https://esg-datanode.jpl.nasa.gov/esgf-idp/openid/<username>:<password>
        String[] parts = basicAuthAsString.split(":");
        String username = parts[0]+":"+parts[1];
        String password = parts[2];
        // choose unique temporary filename
        String outputfile = "/tmp/"+System.currentTimeMillis()+".pem";
        
        // execute myproxy script
        String command = this.scriptpath + " " + username + " " + password + " " + outputfile;
        try {
            execute(command);
        } catch(Exception e) {
            LOG.warn(e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        
        // return certificate
        String cert = FileUtils.readFileToString(new File(outputfile));
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        return cert;        

    }
    
    private String execute(String command) throws Exception {
        
        // execute the command
        Runtime r = Runtime.getRuntime();
        Process p = r.exec(command);
        if (LOG.isInfoEnabled()) LOG.info("Executing command: "+command);
        
        InputStream in = p.getInputStream();
        BufferedInputStream buf = new BufferedInputStream(in);
        InputStreamReader inread = new InputStreamReader(buf);
        BufferedReader bufferedreader = new BufferedReader(inread);
        StringBuffer sb = new StringBuffer();
         
        // read the command output
        String line;
        while ((line = bufferedreader.readLine()) != null) {
                sb.append(line);
        }
        // check for error
        try {
            if (p.waitFor() != 0) {
                throw new Exception("Error: "+sb.toString());
            }
        } catch (InterruptedException e) {
            LOG.warn(e.getMessage());
        } finally {
            bufferedreader.close();
            inread.close();
            buf.close();
            in.close();
        }
        
        return sb.toString();
        
    }

}
