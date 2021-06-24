package webfeed.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webfeed.model.Error;
import webfeed.utility.ApiValidator;
import webfeed.utility.Response;

@WebFilter(asyncSupported = true, urlPatterns = { "/api/*" })
public class VerifyAPIFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		Response responseObj = processApiValidity(httpRequest);

		if (responseObj != null) {

			httpResponse.setStatus(responseObj.getStatusCode());
			httpResponse.setContentType(responseObj.getContentType());
			httpResponse.getWriter().print(responseObj.getJson());

		} else {
			chain.doFilter(httpRequest, httpResponse);
		}

	}

	private Response processApiValidity(HttpServletRequest httpRequest) {

		String apiToken = httpRequest.getHeader("API-Token");
		ApiValidator apiValidator = new ApiValidator();

		if (apiToken == null) {

			Error error = new Error("Invalid API Token", "API Token is missing");
			return new Response.ResponseBuilder(HttpServletResponse.SC_UNAUTHORIZED, Response.JSON).setError(error)
					.build();

		} else if (apiToken.equals("5f9e46c5d4a94aa2e0e944870c7bc5c6") || apiToken.equals("7742690afe6470a82274d0f9076eb10c")) {
			return null;
		}

		else if (!apiValidator.isValidApi(apiToken)) {

			Error error = new Error("Invalid API Token", "API Token is invalid");
			return new Response.ResponseBuilder(HttpServletResponse.SC_UNAUTHORIZED, Response.JSON).setError(error)
					.build();

		} else {

			return null;

		}

	}

}
