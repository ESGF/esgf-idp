/*
 * 
 * kltsa 24/02/2014 no issue :
 * Changes in the original contrail filter so that is it can used with
 * the esgf user table.
 * Note: jpa is used in order to access the database.
 * 
 */

/* Required java libraries */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

/* Required class for accessing the User table in postgres database. */
import test1.User;


/* imports from the contrail repository. */
/*********************************************************************/
import org.ow2.contrail.federation.federationdb.utils.PersistenceUtils;
import eu.contrail.security.SecurityCommons;
/*********************************************************************/

/*
 * Class that implements the filter,
 * that checks the validity of user requests.
 */
public class BasicAuthFilter_esgf  implements Filter 
{
  /*Attributes used for storing database state. */
  private EntityManagerFactory emf;
  private EntityManager em = null;

  /* Attributes for storing security, log, context data.*/
  /* private String message; */
  private static final boolean debug = true;
  private static ServletContext ctx;
  private SecurityCommons sc = new SecurityCommons();
  private FilterConfig filterConfig = null;
 
  /***************************************************/
  /*             Utility functions.                  */ 
  /**************************************************/
 
  /*
   * Returns a String representation of this object.
   * Input:
   * Output:
   */
  @Override
  public String toString() 
  {
    if (filterConfig == null) 
    {
      return ("BasicAuthFilter()");
    }
    StringBuffer sb = new StringBuffer("BasicAuthFilter(");
    sb.append(filterConfig);
    sb.append(")");
    return (sb.toString());
  }
 
  /*
   * Input:
   * Output: the frame stack.
   */
  public static String getStackTrace(Throwable t) 
  {
    String stackTrace = null;
    try 
    {
	  StringWriter sw = new StringWriter();
	  PrintWriter pw = new PrintWriter(sw);
	  t.printStackTrace(pw);
	  pw.close();
	  sw.close();
	  stackTrace = sw.getBuffer().toString();
	} 
    catch (Exception ex) 
    {
	}
	
    return stackTrace;
  }
  
  /*
   * Input: The frame stack.
   * Output: formated frame stack.
   */
  private void sendProcessingError(Throwable t, ServletResponse response) 
  {
    String stackTrace = getStackTrace(t);    
	    
    if (stackTrace != null && !stackTrace.equals("")) 
    {
	  try 
	  {
	    response.setContentType("text/html");
	    PrintStream ps = new PrintStream(response.getOutputStream());
	    PrintWriter pw = new PrintWriter(ps);        
	    pw.print("<html>%n<head>%n<title>Error</title>%n</head>%n<body>%n"); //NOI18N

	    // PENDING! Localize this for next official release
	    pw.print("<h1>The resource did not process correctly</h1>%n<pre>%n");        
	    pw.print(stackTrace);        
	    pw.print("</pre></body>%n</html>"); //NOI18N
	    pw.close();
	    ps.close();
	    response.getOutputStream().close();
	  } 
	  catch (Exception ex) 
	  {
	 
	  }
	} 
    else 
    {
	  try 
	  {
	    PrintStream ps = new PrintStream(response.getOutputStream());
	    t.printStackTrace(ps);
	    ps.close();
	    response.getOutputStream().close();
	  } 
	  catch (Exception ex) 
	  {
	  }
	}
  }
 
  /*
   * Input: String to log.
   * Output: 
   */
  public void log(String msg) 
  {
    ctx.log(msg);    
  }
  
 
  /* Methods from contrail that checks if this is a valid request.
   * if this request is valid then it is forwarded to servlet that 
   * signs the certificate.
   *
   * Input:
   * Output: boolean value, true for valid requests. 
   */
  private boolean doBeforeProcessing(ServletRequest request, ServletResponse response)
		    throws IOException, ServletException 
  {
    if (debug)
    {
	  log("BasicAuthFilter:DoBeforeProcessing");
    }

    // Write code here to process the request and/or response before
    // the rest of the filter chain is invoked. 
   
    // For example, a logging filter might log items on the request object,
    // such as the parameters.

    HttpServletRequest httpRequest = (HttpServletRequest) request;
	HttpServletResponse httpResponse = (HttpServletResponse) response;
		    
	if ("GET".equalsIgnoreCase(httpRequest.getMethod())) 
	{
	  if (debug) 
	  {
		log("Rejecting GET method");
	  }
	  
	  httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	  return false;  
	  
	  // This signals calling code - doFilter - to not carry on
		                      // and the servlet isn't called
	 	      
	  /*
	   * Ideally, want to just return response to client
	   * Rather than call next entry in filter chain (i.e. the servlet)
	   * 
	   */
	}

	final String remoteAddr = httpRequest.getRemoteAddr();

	if (remoteAddr != null) 
	{
	  if (debug) 
	  {
		log("\nBasicAuthFilter: Request from IP " + remoteAddr);
	  }
	}
		    
	final String authHeader = httpRequest.getHeader("Authorization");
		    
	if (authHeader == null || authHeader.length() == 0 || "".equals(authHeader)) 
	{
	  httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"my-contrail-onlineca-realm\"");
	  httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		      
	  if (debug) 
	  {
		log("Sending BasicAuth challenge");
	  }
	  return false;
	}
		    
	final String[] usernameAndPassword = sc.getBasicAuthUsernamePassword(authHeader);
	String username = null; //NOPMD
	String password = null; //NOPMD

		      
	if (usernameAndPassword == null || usernameAndPassword.length != 2) 
	{
	  final String msg = "Cannot retrieve username and password from Authorization header";
     
	  if (debug) 
	  {
		ctx.log(msg);
		       
	  }

	  System.err.println(msg);

      //httpResponse.setHeader("WWW-Authenticate", "BASIC realm=\"Contrail\"");
	  httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg); // , "Cannot retrieve username and password from Authorization header");
	  return false;
    }
		    
	username = usernameAndPassword[0];
	password = usernameAndPassword[1];
		    
	//password = BCrypt.hashpw(password, BCrypt.gensalt(12));
	
	if (debug) 
	{
	  ctx.log(String.format("BasciAuthFilter: Username=%s.", username));
	}

	User user = null;
	
	/*
	 * EntityManager em = pu.getEntityManager(); 
     */

	if (em == null) 
	{
      if (debug) 
      {
        ctx.log("Couldn't create EntityManager");
      }

	  httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	  return false;
    }

    String queryString;
		    
    //queryString = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
		    
	queryString = "SELECT u FROM User u WHERE u.username = :username";
		    
	Query query = em.createQuery(queryString);

	if (query == null) 
	{
      if (debug) 
      {
        ctx.log("Couldn't create Query");
      }

	  httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	  return false;
    }
		    
	query.setParameter("username", username);
    //query.setParameter("password", password);

	boolean badMatch = false;
	try 
	{
      user = (User) query.getSingleResult();
      if (user == null) 
      {
        if (debug) 
        {
          ctx.log("User object from query.getSingleResult is NULL");

		}

		httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return false;
      }
		      
	  badMatch = !BCrypt.checkpw(password, user.getPassword());
		      
	  if (badMatch)
	  {
		if (debug) 
		{
		  ctx.log(String.format("BCrypt.checkpw returns false"));
		}
	  }
    } 
	catch (NoResultException ex) 
	{
      if (debug) 
      {
		ctx.log(String.format("No entry in database for %s.", username));
	  }
	  badMatch = true;

	} 
	catch (NonUniqueResultException ex) 
	{
      if (debug) 
      {
		ctx.log(String.format("Multiple entries in database for %s.", username));
	  }
	  badMatch = true;
	}

    //		      if ("validuser".equals(username) && "validpassword".equals(password)) {
    //		        
    //		        if (debug) {
    //		          log("BasicAuthFilter:Valid user");
    //		        }
    //		      
    //		        httpRequest.setAttribute("username", username);
    //		      
    //		      } else {

	if (badMatch) 
	{
      ctx.log(String.format("Username and password do not match"));
		      
	  httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	  return false;
     } 
	else 
	{
      httpRequest.setAttribute("user", user);
    }

	return true;
  }
 
  /* Process the request after has been checked for errors. 
   * Input:
   * Output: Logs the end of processing.
   */
  private void doAfterProcessing(ServletRequest request, ServletResponse response)
  throws IOException, ServletException 
  {
    if (debug) 
    {
	 log("BasicAuthFilter:DoAfterProcessing");
    }

    // Write code here to process the request and/or response after
    // the rest of the filter chain is invoked.

    // For example, a logging filter might log the attributes on the
    // request object after the request has been processed. 
    /*
	 * for (Enumeration en = request.getAttributeNames(); en.hasMoreElements();
	 * ) { String name = (String)en.nextElement(); Object value =
	 * request.getAttribute(name); log("attribute: " + name + "=" +
	 * value.toString());
	 *
	 * }
	 */

	 // For example, a filter might append something to the response.
	 /*
	  * PrintWriter respOut = new PrintWriter(response.getWriter());
	  * respOut.println("<P><B>This has been appended by an intrusive
	  * filter.</B>");
	  */
  }
  
 /* Test function used creating the connection to the database. 
  * Input:
  * Output:
  */
  public void init_old() throws ServletException
  {
    /*
     * Do required initialization
     * message = "Database read test with jpa.";
     */
	  
    /* 
     * kltsa 14/02/2014 , no issue: Call the object that can connect to database.
     */
    
	if(em == null)
	{	
      emf = Persistence.createEntityManagerFactory("esgf_user_table_entity_class");
      em = emf.createEntityManager();
	}  
  }

  /* Test function used for sending queries to the database. 
   * Input:
   * Output:
   */
  /*
  public void doGet_old(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {*/
    /* ok lets return some data from the database. */
   /*
    String queryString, username;
    queryString = "SELECT um FROM User um ";
    String usernamestr = null, userpassstr = null, username_named = null; 
    
    Query query = em.createQuery(queryString);  
    
    //Query query = em.createNativeQuery("Select firstname from public.user");
        
    
    //query.setParameter("firstname", username);
    
    User res = null;
    res = (User)query.getSingleResult();  
    */
    /* Get results using getters. *//*
    usernamestr = res.getUsername(); 
    userpassstr = res.getPassword();
    */
    /* ok lets cuse the named query method in order to read from the database. *//*
    User res2 = null;
    
    Query queryn = em.createNamedQuery("User.findUsername");
    res2 = (User)query.getSingleResult();        
    */
    /* get results in a different way. *//*
    username_named = res2.getUsername();
    
    
    //Set response content type
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<h3>" 
                 + message 
                 +"data read from database (username, password):\n" 
                 +usernamestr 
                 + "," 
                 + userpassstr 
                 + "\nand the username using a named query:\n " 
                 + username_named 
                 + "</h3>"
                );
  }
*/  
  /* Method for releasing any persistent objects, if any.  
   * Input:
   * Output:
   */
  public void destroy()
  {
      // do nothing.
  }

  
  /* The filter function.
   * Input: The http request.
   * Output: 
   */  
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException 
  {
    // TODO Auto-generated method stub
    if (debug) 
    {
      log("BasicAuthFilter:doFilter()");
    }
	    
    boolean carryOn = doBeforeProcessing(request, response);
	    
    if (carryOn) 
    {
      Throwable problem = null;
      try 
      {
	    chain.doFilter(request, response);
	  } 
      catch (Throwable t) 
      {
	   // If an exception is thrown somewhere down the filter chain,
	   // we still want to execute our after processing, and then
	   // rethrow the problem after that.
	   problem = t;
	   t.printStackTrace();
	  }

	  doAfterProcessing(request, response);

	  // If there was a problem, we want to rethrow it if it is
	  // a known type, otherwise log it.
	  if (problem != null) 
	  {
	    if (problem instanceof ServletException) 
	    {
	      throw (ServletException) problem;
	    }
	  
	    if (problem instanceof IOException) 
	    {
	      throw (IOException) problem;
	    }
	  
	    sendProcessingError(problem, response);
	  }
    }/* if(carryOn) */
  } 


  /* Function for initializing data structures 
   * containing database connection.
   * Input:
   * Output:
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException 
  {
    // TODO Auto-generated method stub
    this.filterConfig = filterConfig;

    if (filterConfig != null) 
    {
	  ctx = filterConfig.getServletContext(); 
	      
	  if (debug) 
	  {        
	    log("BasicAuthFilter:Initializing filter");
	  }
	
	  /* ok lets read from database. */
	  init_old();
	 
	  /*    
	  pu = PersistenceUtils.getInstance();

	  if (pu == null) 
	  {
	    log("About to createInstance");
	        
	    pu = PersistenceUtils.createInstance("appPU"); // Ideally, should be a getinstance call which does 
	                                                  // createInstance if no instance constructed yet 
	    // TODO - change name of persistence unit to read from config file
	  }      
	 
	  if (pu == null) 
	  {
	    log(String.format("BAF: PU is NULL"));
	  }*/
	}
  }

  /* Returns the configuration of this filter.
   * Input:
   * Output:
   */
  public FilterConfig getFilterConfig() 
  {
    return (this.filterConfig);
  }

 
  /* Sets filter configuration.
   * Input:
   * Output:
   */
  public void setFilterConfig(FilterConfig filterConfig) 
  {
    this.filterConfig = filterConfig;
  }
}