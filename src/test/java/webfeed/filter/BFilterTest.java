package webfeed.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import webfeed.utility.ApiValidator;

class BFilterTest {
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	FilterChain chain;
	
	
	
	@BeforeEach
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void noApiToken_returnErrorResponse() throws IOException, ServletException {
		
		when(request.getHeader("API-Token")).thenReturn(null);
		
		
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(response.getWriter()).thenReturn(pw);
		
		new VerifyAPIFilter().doFilter(request, response, chain);
		
		String result = sw.getBuffer().toString().trim();
		String expected = "{\"title\":\"Invalid API Token\",\"description\":\"API Token is missing\"}";
		
		assertEquals(expected,result);
		
	}
	
	@Test
	void invalidApiToken_returnErrorResponse() throws IOException, ServletException {
		
		when(request.getHeader("API-Token")).thenReturn("sda");
		ApiValidator apiValidator = mock(ApiValidator.class);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(response.getWriter()).thenReturn(pw);
		when(apiValidator.isValidApi(anyString())).thenReturn(false);
		
		new VerifyAPIFilter().doFilter(request, response, chain);
		
		String result = sw.getBuffer().toString().trim();
		String expected = "{\"title\":\"Invalid API Token\",\"description\":\"API Token is invalid\"}";
		
		assertEquals(expected,result);
		
	}
	
	@Test
	void validApiToken_returnErrorResponse() throws IOException, ServletException {
		
		when(request.getHeader("API-Token")).thenReturn(null);
		ApiValidator apiValidator = mock(ApiValidator.class);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(response.getWriter()).thenReturn(pw);
		when(apiValidator.isValidApi(anyString())).thenReturn(true);
		
		new VerifyAPIFilter().doFilter(request, response, chain);
		

		
		
		
	}

}
