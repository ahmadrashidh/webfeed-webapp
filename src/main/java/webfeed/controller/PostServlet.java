package webfeed.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;

import webfeed.dao.LikeDao;
import webfeed.dao.PostDao;
import webfeed.model.Post;
import webfeed.model.PostID;
import webfeed.utility.Json;
import webfeed.utility.Response;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String JSON = "application/json";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {

			Response responseObj = getAllPosts();
			
			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());
			
		} catch (Exception e) {

			System.out.println(e.getStackTrace());

		}

	}


	private Response getAllPosts() {
		
		PostDao postDao = new PostDao();
		LikeDao likeDao = new LikeDao();
		List<Post> allPosts = postDao.getAll();
		
		allPosts.forEach(post -> {
			post.setLikes(likeDao.getAllByPost(post.getId()));
		});

		TypeToken<List<Post>> type = new TypeToken<List<Post>>() {
		};

		Json<List<Post>> json = new Json<>(type.getType());
		String responsePayload = json.convertToPayload(allPosts);

		return new Response.ResponseBuilder(200, JSON).setJson(responsePayload).build();

	}


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


		try {

			BufferedReader requestBuffer = request.getReader();
			
			

			Response responseObj = saveAllPosts(requestBuffer);

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());

		} catch (IOException e) {

			System.out.println("IO Exception" + e.getMessage());

		}

	}

	private Response saveAllPosts(BufferedReader requestBuffer) throws IOException {
		PostDao postDao = new PostDao();
		Json<Post> reqJson = new Json<>(Post.class);
		Json<PostID> resJson = new Json<>(PostID.class);
		String jsonPayload = reqJson.readJson(requestBuffer);

		Post post = reqJson.convertToModel(jsonPayload);
		postDao.create(post);

		PostID postId = new PostID();
		postId.setPostId(post.getId());

		String responsePayload = resJson.convertToPayload(postId);
		return new Response.ResponseBuilder(200, "application/json").setJson(responsePayload).build();

	}

}
