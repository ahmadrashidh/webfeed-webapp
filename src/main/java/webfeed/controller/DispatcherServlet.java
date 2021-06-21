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

import webfeed.model.Error;
import webfeed.utility.Response;

@WebServlet("/api/*")
public class DispatcherServlet extends HttpServlet {

	static final String ALL_USERS = "getUsers";
	static final String USERS_BY_ID = "getUsersById";
	static final String ALL_POSTS = "getPosts";
	static final String POSTS_BY_ID = "getPostsById";
	static final String ALL_LIKES = "getLikes";
	static final String LIKES_BY_ID = "getLikesById";
	static final String ALL_COMMENTS = "getComments";
	static final String COMMENTS_BY_ID = "getCommentsById";

	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		Error error = new Error("Unknown Endpoint", "We don't serve this endpoint");
		Response responseObj = new Response.ResponseBuilder(501, Response.JSON).setError(error).build();

		try {

			String action = getAction(request);

			request.setAttribute("Action", action);

			switch (action) {
			case USERS_BY_ID:
				request.getRequestDispatcher("/users").forward(request, response);
				break;
			case ALL_POSTS:
			case POSTS_BY_ID:
				request.getRequestDispatcher("/posts").forward(request, response);
				break;
			case ALL_LIKES:
				request.getRequestDispatcher("/likes").forward(request, response);
				break;
			case ALL_COMMENTS:
				request.getRequestDispatcher("/comments").forward(request, response);
				break;
			case LIKES_BY_ID:
			case COMMENTS_BY_ID:
			default:

			}

		} catch (Exception e) {

			error = new Error("Something went wrong", "Some unknown error occurred during the execution of your query");
			responseObj = new Response.ResponseBuilder(500, Response.JSON).setError(error).build();

		} finally {

			response.setContentType(responseObj.getContentType());
			response.setStatus(responseObj.getStatusCode());
			response.getWriter().print(responseObj.getJson());
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		Error error = new Error("Unknown Endpoint", "We don't serve this endpoint");
		Response responseObj = new Response.ResponseBuilder(501, Response.JSON).setError(error).build();

		try {

			String action = getAction(request);

			System.out.println("action:" + action);
			request.setAttribute("Action", action);

			switch (action) {
			case ALL_POSTS:
				request.getRequestDispatcher("/posts").forward(request, response);
				break;
			case ALL_LIKES:
				request.getRequestDispatcher("/likes").forward(request, response);
				break;
			case ALL_COMMENTS:
				request.getRequestDispatcher("/comments").forward(request, response);
				break;
			default:

			}

		} catch (Exception e) {

			error = new Error("Something went wrong", "Some unknown error occurred during the execution of your query");
			responseObj = new Response.ResponseBuilder(501, Response.JSON).setError(error).build();

		} finally {

			response.setContentType(responseObj.getContentType());
			response.setStatus(responseObj.getStatusCode());
			response.getWriter().print(responseObj.getJson());

		}
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

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
		case ALL_USERS:
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

		} else if (isThisPath(path, 1, "users")) {
			return ALL_USERS;
		} else {
			return "respondUnserviced";
		}
	}

	private boolean isThisPath(String path, int position, String reqPath) {
		String[] pathSplits = path.split("/");
		return pathSplits.length > position && pathSplits[position].equals(reqPath);
	}

}
