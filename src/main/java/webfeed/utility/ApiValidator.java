package webfeed.utility;

import webfeed.dao.UserDao;
import webfeed.exception.EntityNotFoundException;

public class ApiValidator {

	public boolean isValidApi(String apiToken) {

		try {
			
			UserDao userDao = new UserDao();
			String sessionId = userDao.getSession(apiToken);
			return isSessionActive(sessionId);

		} catch (EntityNotFoundException e) {
			
			return false;
		
		}

	}

	private static boolean isSessionActive(String sessionId) {

		return OAuthUtils.isUserLoggedIn(sessionId);

	}

}
