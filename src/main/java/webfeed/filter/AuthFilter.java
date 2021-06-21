package webfeed.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webfeed.utility.OAuthUtils;

@WebFilter(asyncSupported = true, urlPatterns = { "/profile" })
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String sessionId = httpRequest.getSession().getId();

	    boolean isUserLoggedIn =
	        OAuthUtils.isUserLoggedIn(sessionId);
	    System.out.println("Session in OAuth:" + sessionId);
	    
	    if (isUserLoggedIn) {
	    	
	    	
	    	chain.doFilter(httpRequest, httpResponse);	
	    		 
	      
	    } else {
	    	
	    	request.getRequestDispatcher("index.html").forward(request, response);
	    }
		
		
	}
	
	

}
