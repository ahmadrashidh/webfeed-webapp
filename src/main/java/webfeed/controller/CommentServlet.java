package webfeed.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;

import webfeed.dao.CommentDao;
import webfeed.dao.LikeDao;
import webfeed.model.Comment;
import webfeed.model.CommentId;
import webfeed.model.CommentList;
import webfeed.model.Like;
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

		try {
			@SuppressWarnings("unchecked")
			List<String> pathParamList = (ArrayList<String>) request.getAttribute("PathParams");
			
			String postId = pathParamList.get(0);
			
			Response responseObj = getAllComments(postId);

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());
			
		} catch (Exception e) {

			System.out.println(e.getStackTrace());

		}

	}

	private Response getAllComments(String postId) {
		
		CommentDao commentDao = new CommentDao();
		List<CommentList> allComments = commentDao.getAllByPost(postId);

//		TypeToken<List<CommentList>> type = new TypeToken<List<CommentList>>() {
//		};
//
//		Json<List<CommentList>> json = new Json<>(type.getType());
//		String responsePayload = json.convertToPayload(allComments);

		return new Response.ResponseBuilder(200, "application/json").build();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {

			@SuppressWarnings("unchecked")
			List<String> pathParamList = (ArrayList<String>) request.getAttribute("PathParams");

			String postId = pathParamList.get(0);

			BufferedReader requestBuffer = request.getReader();

			Response responseObj = saveCommentForPost(requestBuffer, postId);

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().write(responseObj.getJson());

		} catch (IOException e) {

			System.out.println("IO Exception");

		}

	}

	private Response saveCommentForPost(BufferedReader requestBuffer, String postId) throws IOException {
		
		CommentDao commentDao = new CommentDao();
		Json<Comment> reqJson = new Json<>(Comment.class);
		String jsonPayload = reqJson.readJson(requestBuffer);

		Comment comment = reqJson.convertToModel(jsonPayload);
		comment.setPostId(postId);
		
		if(comment.getParentCommentId() != null ) {
			commentDao.createUnderComment(comment);
		} else {
			commentDao.create(comment);
		}
		
		
		CommentId commentId = new CommentId();
		commentId.setCommentId(comment.getId());
		
		Json<CommentId> resJson = new Json<>(CommentId.class);
		String resPayload = resJson.convertToPayload(commentId);
		
		return new Response.ResponseBuilder(201, "application/json").setJson(resPayload).build();
	}

}
