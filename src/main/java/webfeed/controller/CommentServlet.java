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

import webfeed.dao.CommentDao;
import webfeed.exception.EntityNotFoundException;
import webfeed.exception.InvalidInputException;
import webfeed.model.Comment;
import webfeed.model.CommentId;
import webfeed.model.CommentList;
import webfeed.model.Error;
import webfeed.utility.Json;
import webfeed.utility.Response;

@WebServlet("/comments/*")
public class CommentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1642099255461890718L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Error error = new Error("Unknown Endpoint", "We don't serve this endpoint");
		Response responseObj = new Response.ResponseBuilder(501, Response.JSON).setError(error).build();

		try {

			String action = String.valueOf(request.getAttribute("Action"));
			@SuppressWarnings("unchecked")
			List<String> pathParamList = (ArrayList<String>) request.getAttribute("PathParams");
			Long postId = Long.parseLong(pathParamList.get(0));
			switch (action) {
			case DispatcherServlet.ALL_COMMENTS:
				responseObj = getAllComments(postId);
				break;
			case DispatcherServlet.COMMENTS_BY_ID:
				Long commentId = Long.parseLong(pathParamList.get(1));
				responseObj = getCommentsById(commentId);
				break;
			default:

			}

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

	private Response getCommentsById(Long commentId) {

		Response responseObj;

		try {

			CommentDao commentDao = new CommentDao();
			List<CommentList> allComments = commentDao.getByComment(commentId);

			List<CommentList> commentThread = CommentList.generateCommentThread(allComments, commentId);

			TypeToken<List<CommentList>> type = new TypeToken<List<CommentList>>() {
			};

			Json<List<CommentList>> json = new Json<>(type.getType());
			String responsePayload = json.convertToPayload(commentThread);

			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_OK, Response.JSON)
					.setJson(responsePayload).build();

		} catch (EntityNotFoundException e) {

			Error error = new Error(e.getTitle(), e.getMessage());
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_NOT_FOUND, Response.JSON).setError(error)
					.build();

		}

		return responseObj;

	}

	private Response getAllComments(Long postId) {

		CommentDao commentDao = new CommentDao();
		List<CommentList> allComments = commentDao.getAllByPost(postId);

		List<CommentList> commentThread = CommentList.generateCommentThread(allComments, postId);

		TypeToken<List<CommentList>> type = new TypeToken<List<CommentList>>() {
		};

		Json<List<CommentList>> json = new Json<>(type.getType());
		String responsePayload = json.convertToPayload(commentThread);

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

			responseObj = saveCommentForPost(requestBuffer, postId);


		} catch (NumberFormatException e) {

			error = new Error("Invalid URL parameters", "Ensure you have provided right URL parameters");
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_BAD_REQUEST, Response.JSON)
					.setError(error).build();

		} catch (Exception e) {

			error = new Error("Something went wrong in Comment Servlet", e.getMessage());
			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Response.JSON)
					.setError(error).build();

		} finally {

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());

		}

	}

	private Response saveCommentForPost(BufferedReader requestBuffer, Long postId) throws IOException {

		Response responseObj;

		try {

			CommentDao commentDao = new CommentDao();
			Json<Comment> reqJson = new Json<>(Comment.class);
			String jsonPayload = reqJson.readJson(requestBuffer);
			System.out.println("JSON:" + jsonPayload);
			Comment comment = reqJson.convertToModel(jsonPayload);
			comment.setPostId(postId);

			Error error = comment.validate();
			if (error != null)
				throw new InvalidInputException(error.getTitle(), error.getDescription());

			if (comment.getParentId() != null) {
				commentDao.createUnderComment(comment);
			} else {
				commentDao.create(comment);
			}

			CommentId commentId = new CommentId();
			commentId.setId(comment.getId());

			Json<CommentId> resJson = new Json<>(CommentId.class);
			String resPayload = resJson.convertToPayload(commentId);

			responseObj = new Response.ResponseBuilder(HttpServletResponse.SC_CREATED, Response.JSON).setJson(resPayload)
					.build();

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

}
