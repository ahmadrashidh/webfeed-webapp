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

import webfeed.dao.LikeDao;
import webfeed.model.Like;
import webfeed.utility.Json;
import webfeed.utility.Response;

@WebServlet("/likes/*")
public class LikeServlet extends HttpServlet {

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
			
			Response responseObj = getAllLikes(postId);

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());
			
		} catch (Exception e) {

			System.out.println(e.getStackTrace());

		}
		
		
		
		
	}
	
	
	private Response getAllLikes(String postId) {
		
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
		
		try {
			
			@SuppressWarnings("unchecked")
			List<String> pathParamList = (ArrayList<String>) request.getAttribute("PathParams");
			
			String postId = pathParamList.get(0);
			
			BufferedReader requestBuffer = request.getReader();
			
			Response responseObj = saveLikeForPost(requestBuffer,postId);
			
			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			
		} catch (IOException e) {
			
			System.out.println("IO Exception");
			
		}
		
		
	
	}


	private Response saveLikeForPost(BufferedReader requestBuffer, String postId) throws IOException {
		
		LikeDao likeDao = new LikeDao();
		Json<Like> reqJson = new Json<>(Like.class);
		String jsonPayload = reqJson.readJson(requestBuffer);

		Like like = reqJson.convertToModel(jsonPayload);
		like.setPostId(postId);
		likeDao.create(like);;

		return new Response.ResponseBuilder(200, "application/json").build();
		
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
