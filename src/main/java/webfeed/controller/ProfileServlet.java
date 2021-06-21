package webfeed.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.oauth2.model.Userinfo;

import webfeed.dao.UserDao;
import webfeed.model.User;
import webfeed.utility.OAuthUtils;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

	private static final long serialVersionUID = 5773890289701406598L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		try {
			String sessionId = request.getSession().getId();
			System.out.println("Session in Profile:" + sessionId);

			Userinfo userInfo = OAuthUtils.getUserInfo(sessionId);

			UserDao userDao = new UserDao();
			User user = new User(userInfo);
			user.setApiToken(user.generateApiToken(sessionId));
			user.setSessionId(sessionId);

			if (userDao.isNewUser(user.getEmail())) {
				userDao.create(user);
			} else {
				user.setId(userDao.getUserId(user.getEmail()));
				userDao.update(user, null);
			}

			response.sendRedirect("http://localhost:8082?id=" + user.getId() + "&" + "apiToken=" + user.getApiToken());

		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}

	}
}