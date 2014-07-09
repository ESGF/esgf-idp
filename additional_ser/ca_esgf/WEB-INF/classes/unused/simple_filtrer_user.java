
/*
 * kltsa 25/02/2014, no issue :Simple servlet for testing the basicAuthFilter_esgf.
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class simple_filtrer_user
 */
@WebServlet("/simple_filtrer_user")
public class simple_filtrer_user extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public simple_filtrer_user() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init(final ServletConfig config)
  		  throws ServletException {
  	  super.init(config);
    }  

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	  response.setContentType("text/html");
	  PrintWriter out = response.getWriter();
	  out.println("<h3>" 
		           +"Simple_filter_user invokedusing get." 
		           + "</h3>"
		          );		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	  /* return some data. */
  
	  response.setContentType("text/html");
	  PrintWriter out = response.getWriter();
	  out.println("<h3>" 
	               +"Simple_filter_user invokedusing post." 
	               + "</h3>"
	             );		
	}

}
