package webfeed.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import webfeed.dao.PostDao;
import webfeed.exception.InvalidInputException;
import webfeed.exception.EntityNotFoundException;
import webfeed.model.Error;
import webfeed.model.Post;
import webfeed.model.PostID;
import webfeed.utility.Json;
import webfeed.utility.Response;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String JSON = "application/json";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		Error error = new Error("Unknown Endpoint", "We don't serve this endpoint");
		Response responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_NOT_IMPLEMENTED, Response.JSON).setError(error).build();

		try {

			String action = request.getAttribute("Action").toString();
			switch (action) {
			case DispatcherServlet.ALL_POSTS:
				
				responseObj = getAllPosts();
				break;
				
			case DispatcherServlet.POSTS_BY_ID:

				@SuppressWarnings("unchecked")
				List<String> pathParams = (List<String>) request.getAttribute("PathParams");
				Long postId = Long.parseLong(pathParams.get(0));
				responseObj = getPostById(postId);
				break;

			default:

			}

		} catch(NumberFormatException e) {
			error = new Error("Invalid URL parameters", "Ensure you have provided right URL parameters");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_BAD_REQUEST, Response.JSON).setError(error).build();
			
		} catch (Exception e) {

			error = new Error("Something went wrong", "Some unknown error occurred during the execution of your query");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Response.JSON).setError(error).build();

		} finally {

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());

		}

	}

	private Response getPostById(Long postId) {

		Response responseObj;

		try {

			PostDao postDao = new PostDao();
			Post post = postDao.getById(postId);

			Json<Post> json = new Json<>(Post.class);
			String responsePayload = json.convertToPayload(post);

			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_OK, JSON).setJson(responsePayload).build();

		} catch (EntityNotFoundException e) {

			Error error = new Error(e.getTitle(), e.getMessage());
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_NOT_FOUND, Response.JSON).setError(error).build();

		}

		return responseObj;
	}

	private Response getAllPosts() {

		PostDao postDao = new PostDao();
		List<Post> allPosts = postDao.getAll();

		TypeToken<List<Post>> type = new TypeToken<List<Post>>() {
		};

		Json<List<Post>> json = new Json<>(type.getType());
		String responsePayload = json.convertToPayload(allPosts);

		return new Response.ResponseBuilder(HttpServletResponse.SC_OK, JSON).setJson(responsePayload).build();

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Error error = new Error("Unknown Endpoint", "We don't serve this endpoint");
		Response responseObj = new Response.ResponseBuilder(501, Response.JSON).setError(error).build();

		try {

			BufferedReader requestBuffer = request.getReader();

			responseObj = saveAllPosts(requestBuffer);

		} catch (Exception e) {

			error = new Error("Something went wrong", "Some unknown error occurred during the execution of your query");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Response.JSON).setError(error).build();

		}

		response.setStatus(responseObj.getStatusCode());
		response.setContentType(responseObj.getContentType());
		response.getWriter().print(responseObj.getJson());

	}

	private Response saveAllPosts(BufferedReader requestBuffer) throws IOException {

		Response responseObj;

		try {

			PostDao postDao = new PostDao();
			Json<Post> reqJson = new Json<>(Post.class);
			Json<PostID> resJson = new Json<>(PostID.class);
			String jsonPayload = reqJson.readJson(requestBuffer);

			Post post = reqJson.convertToModel(jsonPayload);
			Error error = post.validate();
			
			if(error == null) {
				postDao.create(post);
			} else {
				throw new InvalidInputException(error.getTitle(), error.getDescription());
			}
			
			PostID postId = new PostID();
			postId.setId(post.getId());

			String responsePayload = resJson.convertToPayload(postId);
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_OK, Response.JSON).setJson(responsePayload).build();

		} catch (InvalidInputException e) {
			
			Error error = new Error(e.getTitle(), e.getMessage());
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_BAD_REQUEST, Response.JSON).setError(error).build();
			
			
		} catch (JsonParseException e) {

			Error error = new Error("JSON not well formed", "Make sure you entered valid values");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_BAD_REQUEST, Response.JSON).setError(error).build();

		}
		
		return responseObj;

	}

}
