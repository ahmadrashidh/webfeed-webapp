package webfeed.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import webfeed.dao.LikeDao;
import webfeed.exception.InvalidInputException;
import webfeed.model.Error;
import webfeed.model.Like;
import webfeed.utility.Json;
import webfeed.utility.Response;

@WebServlet("/likes/*")
public class LikeServlet extends HttpServlet {

	private static final long serialVersionUID = 1642099255461890718L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Error error = new Error("Unknown Endpoint", "We don't serve this endpoint");
		Response responseObj = new Response.ResponseBuilder(501, Response.JSON).setError(error).build();

		try {

			@SuppressWarnings("unchecked")
			List<String> pathParamList = (ArrayList<String>) request.getAttribute("PathParams");

			Long postId = Long.parseLong(pathParamList.get(0));

			responseObj = getAllLikes(postId);

		} catch (NumberFormatException e) {
			error = new Error("Invalid URL parameters", "Ensure you have provided right URL parameters");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_BAD_REQUEST, Response.JSON)
					.setError(error).build();

		} catch (Exception e) {

			error = new Error("Something went wrong", "Some unknown error occurred during the execution of your query");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Response.JSON)
					.setError(error).build();

		} finally {

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());

		}

	}

	private Response getAllLikes(Long postId) {

		LikeDao likeDao = new LikeDao();
		List<Like> allLikes = likeDao.getAllByPost(postId);

		TypeToken<List<Like>> type = new TypeToken<List<Like>>() {
		};

		Json<List<Like>> json = new Json<>(type.getType());
		String responsePayload = json.convertToPayload(allLikes);

		return new Response.ResponseBuilder(200, "application/json").setJson(responsePayload).build();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Error error = new Error("Unknown Endpoint", "We don't serve this endpoint");
		Response responseObj = new Response.ResponseBuilder(501, Response.JSON).setError(error).build();

		try {

			@SuppressWarnings("unchecked")
			List<String> pathParamList = (ArrayList<String>) request.getAttribute("PathParams");

			Long postId = Long.parseLong(pathParamList.get(0));

			BufferedReader requestBuffer = request.getReader();

			responseObj = saveLikeForPost(requestBuffer, postId);

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());

		} catch (NumberFormatException e) {

			error = new Error("Invalid URL parameters", "Ensure you have provided right URL parameters");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_BAD_REQUEST, Response.JSON)
					.setError(error).build();

		} catch (Exception e) {

			error = new Error("Something went wrong", "Some unknown error occurred during the execution of your query");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Response.JSON)
					.setError(error).build();

		} finally {

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());

		}

	}

	private Response saveLikeForPost(BufferedReader requestBuffer, Long postId) throws IOException {
		
		Response responseObj;
		
		try {
		LikeDao likeDao = new LikeDao();
		Json<Like> reqJson = new Json<>(Like.class);
		String jsonPayload = reqJson.readJson(requestBuffer);

		Like like = reqJson.convertToModel(jsonPayload);
		Error error  = like.validate();
		
		if(error != null) {
			throw new InvalidInputException(error.getTitle(), error.getDescription());
		}
		
		like.setPostId(postId);
		likeDao.create(like);


		responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_OK, Response.JSON).build();
		
		} catch (InvalidInputException e) {

			Error error = new Error(e.getTitle(), e.getMessage());
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_BAD_REQUEST, Response.JSON)
					.setError(error).build();

		} catch (JsonParseException e) {

			Error error = new Error("JSON not well formed", "Make sure you entered valid values");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_BAD_REQUEST, Response.JSON)
					.setError(error).build();

		}
		
		return responseObj;
		
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

		@SuppressWarnings("unchecked")
		List<String> pathParamList = (ArrayList<String>) request.getAttribute("PathParams");

		String postId = pathParamList.get(0);

		Response responseObj = deleteLikeForPost(postId);

		response.setStatus(responseObj.getStatusCode());
		response.setContentType(responseObj.getContentType());

	}

	private Response deleteLikeForPost(String postId) {
		// TODO Auto-generated method stub
		return null;
	}

}
