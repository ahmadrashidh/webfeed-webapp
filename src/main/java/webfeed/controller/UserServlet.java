package webfeed.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webfeed.dao.UserDao;
import webfeed.model.User;
import webfeed.utility.Json;
import webfeed.utility.Response;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		Response responseObj = new Response.ResponseBuilder(500, "application/json").build();
		try {

			@SuppressWarnings("unchecked")
			List<String> pathParams = (List<String>) request.getAttribute("PathParams");
			Long userId = Long.parseLong(pathParams.get(0));
			responseObj = getUserById(userId);

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());

		} catch (Exception e) {

			response.setStatus(responseObj.getStatusCode());
			response.setContentType(responseObj.getContentType());
			response.getWriter().print(responseObj.getJson());

		}

	}

	private Response getUserById(Long userId) {
		
		UserDao userDao = new UserDao();
		User post = userDao.getById(userId);

		Json<User> json = new Json<>(User.class);
		String responsePayload = json.convertToPayload(post);

		return new Response.ResponseBuilder(200, "application/json").setJson(responsePayload).build();
	}

}
