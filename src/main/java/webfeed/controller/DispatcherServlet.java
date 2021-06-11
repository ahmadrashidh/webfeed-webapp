package webfeed.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {

	private static final String ALL_POSTS = "getPosts";
	private static final String POSTS_BY_ID = "getPostsById";
	private static final String ALL_LIKES = "getLikes";
	private static final String LIKES_BY_ID = "getLikesById";
	private static final String ALL_COMMENTS = "getComments";
	private static final String COMMENTS_BY_ID = "getCommentsById";

	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String action = getAction(request);
		RequestDispatcher dispatcher;

		System.out.println("action:" + action);
		request.setAttribute("Action", action);
	
		switch (action) {
		case ALL_POSTS:
		case POSTS_BY_ID:
			dispatcher = request.getRequestDispatcher("/posts");
			System.out.println("Dispatching to posts");
			dispatcher.forward(request, response);
			break;
		case ALL_LIKES:
			dispatcher = request.getRequestDispatcher("/likes");
			System.out.println("Dispatching to likes");
			dispatcher.forward(request, response);
			break;
		case ALL_COMMENTS:
			dispatcher = request.getRequestDispatcher("/comments");
			System.out.println("Dispatching to comments");
			dispatcher.forward(request, response);
			break;
		case LIKES_BY_ID:
		case COMMENTS_BY_ID:
		default:
			response.setStatus(404);
		}

	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String action = getAction(request);
		RequestDispatcher dispatcher;

		System.out.println("action:" + action);
		request.setAttribute("Action", action);

		switch (action) {
		case ALL_POSTS:
		case POSTS_BY_ID:
			dispatcher = request.getRequestDispatcher("/posts");
			System.out.println("Dispatching to posts");
			dispatcher.forward(request, response);
			break;
		case ALL_LIKES:
			dispatcher = request.getRequestDispatcher("/likes");
			System.out.println("Dispatching to likes");
			dispatcher.forward(request, response);
			break;
		case ALL_COMMENTS:
			dispatcher = request.getRequestDispatcher("/comments");
			System.out.println("Dispatching to comments");
			dispatcher.forward(request, response);
			break;
		case LIKES_BY_ID:
		case COMMENTS_BY_ID:
		default:
			response.getWriter().write("Unserviced");
		}
	}
	
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String action = getAction(request);
		RequestDispatcher dispatcher;

		System.out.println("action:" + action);
		request.setAttribute("Action", action);

		switch (action) {
		case ALL_POSTS:
		case POSTS_BY_ID:
			dispatcher = request.getRequestDispatcher("/posts");
			System.out.println("Dispatching to posts");
			dispatcher.forward(request, response);
			break;
		case ALL_LIKES:
			dispatcher = request.getRequestDispatcher("/likes");
			System.out.println("Dispatching to likes");
			dispatcher.forward(request, response);
			break;
		case ALL_COMMENTS:
			dispatcher = request.getRequestDispatcher("/comments");
			System.out.println("Dispatching to comments");
			dispatcher.forward(request, response);
			break;
		case LIKES_BY_ID:
		case COMMENTS_BY_ID:
		default:
			response.getWriter().write("Unserviced");
		}
	}


	private String getAction(HttpServletRequest request) {

		String path = request.getPathInfo();
		System.out.println("Path:" + path);

		StringBuilder endpoint = new StringBuilder(getPathType(path));

		List<String> params = getPathParams(path);

		if (!params.isEmpty()) {
			request.setAttribute("PathParams", params);
			endpoint.append(getParamAction(endpoint.toString(), params));
		}

		System.out.println("endpoint" + endpoint.toString());
		return endpoint.toString();
	}

	private String getParamAction(String endpoint, List<String> params) {

		switch (endpoint) {
		case ALL_LIKES:
		case ALL_COMMENTS:
			return params.size() > 1 && params.get(1) != null ? "ById" : "";
		case ALL_POSTS:
			return params.size() > 0 && params.get(0) != null ? "ById" : "";
		default:
			return "";
		}

	}

	private List<String> getPathParams(String path) {

		List<String> pathParams = new ArrayList<>();

		String[] pathSplits = path.split("/");

		for (int position = 2, index = 0; position < pathSplits.length; position = position + 2, index++) {
			if (!(pathSplits[position] != null && pathSplits[position].isBlank())) {
				pathParams.add(index, pathSplits[position]);
			}
		}

		System.out.println("Path Params");
		pathParams.forEach(System.out::println);

		return pathParams;

	}

	private String getPathType(String path) {

		if (isThisPath(path, 1, "posts")) {

			if (isThisPath(path, 3, "likes"))
				return ALL_LIKES;
			else if (isThisPath(path, 3, "comments"))
				return ALL_COMMENTS;
			else
				return ALL_POSTS;
			
		} else {
			return "respondUnserviced";
		}
	}

	private boolean isThisPath(String path, int position, String reqPath) {
		String[] pathSplits = path.split("/");
		return pathSplits.length > position && pathSplits[position].equals(reqPath);
	}

}
