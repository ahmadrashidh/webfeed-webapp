package webfeed.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DispatcherServletTest {
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	RequestDispatcher dispatcher;
	

	@BeforeEach
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void doGet_unservicedPath_returnsErrorResponse() throws IOException, ServletException {

		when(request.getPathInfo()).thenReturn("/h");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(response.getWriter()).thenReturn(pw);

		new DispatcherServlet().doGet(request, response);

		
		String result = sw.getBuffer().toString().trim();
		String expected = "{\"title\":\"Unknown Endpoint\",\"description\":\"We do not serve this endpoint\"}";
		
		verify(response).setStatus(501);
		assertEquals(expected, result);

	}

	@Test
	void doGet_postsPath_forwardsToPostServletWithAction() throws IOException, ServletException {
		

		when(request.getPathInfo()).thenReturn("/posts");
		when(request.getRequestDispatcher("/posts")).thenReturn(dispatcher);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(response.getWriter()).thenReturn(pw);

		new DispatcherServlet().doGet(request, response);
		verify(request).setAttribute("Action", "getPosts");
		verify(dispatcher).forward(request, response);

	}
	
	@Test
	void doGet_postsPathWithParam_forwardsToPostServletWithAttributes() throws IOException, ServletException {
		

		when(request.getPathInfo()).thenReturn("/posts/65238778236289");
		when(request.getRequestDispatcher("/posts")).thenReturn(dispatcher);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(response.getWriter()).thenReturn(pw);

		new DispatcherServlet().doGet(request, response);
		
		List<String> params = new ArrayList<>();
		params.add("65238778236289");
	
		verify(request).setAttribute("PathParams", params);
		verify(request).setAttribute("Action", "getPostsById");
		verify(dispatcher).forward(request, response);

	}
	
	@Test
	void doGet_likesPath_forwardsToLikeServletWithAction() throws IOException, ServletException {
		

		when(request.getPathInfo()).thenReturn("/posts/3332323434242/likes");
		when(request.getRequestDispatcher("/likes")).thenReturn(dispatcher);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(response.getWriter()).thenReturn(pw);
		
		List<String> params = new ArrayList<>();
		params.add("3332323434242");
	
		

		new DispatcherServlet().doGet(request, response);
		
		verify(request).setAttribute("PathParams", params);
		verify(request).setAttribute("Action", "getLikes");
		verify(dispatcher).forward(request, response);

	}
	
	@Test
	void doGet_commentsPath_forwardsToCommentServletWithAction() throws IOException, ServletException {
		

		when(request.getPathInfo()).thenReturn("/posts/3332323434242/comments");
		when(request.getRequestDispatcher("/comments")).thenReturn(dispatcher);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(response.getWriter()).thenReturn(pw);
		
		List<String> params = new ArrayList<>();
		params.add("3332323434242");
	
	
		new DispatcherServlet().doGet(request, response);
		
		verify(request).setAttribute("PathParams", params);
		verify(request).setAttribute("Action", "getComments");
		verify(dispatcher).forward(request, response);

	}
	
	

}
