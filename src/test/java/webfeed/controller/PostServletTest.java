package webfeed.controller;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import webfeed.dao.PostDao;
import webfeed.model.Post;

class PostServletTest {

	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	
	@Mock
	PostDao postDao;
	
	@BeforeEach
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void doGet_getAllPosts_returnResponse() throws IOException, ServletException {
		
		List<Post> allPosts = new ArrayList<>();
		Post post = new Post();
		post.setPostText("Test Text");
		post.setId(3492482840L);
		post.setCreatedDate(System.currentTimeMillis());
		allPosts.add(post);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(request.getAttribute("Action")).thenReturn("getPosts");
		when(postDao.getAll()).thenReturn(allPosts);
		when(response.getWriter()).thenReturn(pw);
		
	
		new PostServlet().doGet(request, response);
		
		String result = sw.getBuffer().toString().trim();
		System.out.println(result);
		
		
		
	}

}
