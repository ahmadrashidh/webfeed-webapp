package webfeed.utility;

import webfeed.model.Error;

public class Response {

	private int statusCode;

	private int contentLength;

	private String contentType;
	
	public static final String JSON = "application/json";

	private String responseJson;

	private Error error;

	private boolean isError = false;

	private Response(ResponseBuilder response) {

		this.statusCode = response.statusCode;
		this.contentType = response.contentType;
		this.contentLength = response.contentLength;
		this.responseJson = response.responseJson;
		this.error = response.error;
		this.isError = response.isError;

	}

	public static class ResponseBuilder {

		private int statusCode;

		private int contentLength;

		private String contentType;

		private String responseJson;

		private Error error;

		private boolean isError = false;

		public ResponseBuilder(int statusCode, String contentType) {

			this.statusCode = statusCode;
			this.contentType = contentType;

		}

		public ResponseBuilder setContentLength(int contentLength) {

			this.contentLength = contentLength;
			return this;

		}

		public ResponseBuilder setJson(String responseJson) {
			this.responseJson = responseJson;
			return this;
		}

		public ResponseBuilder setError(Error error) {
			this.isError = true;
			this.error = error;
			return this;
		}

		public Response build() {
			return new Response(this);

		}

	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public int getContentLength() {
		return this.contentLength;
	}

	public String getContentType() {
		return this.contentType;
	}

	public String getJson() {

		if (isError) {
			Json<Error> json = new Json<>(Error.class);
			return json.convertToPayload(this.error);
		}

		return this.responseJson;
	}

	public Error getErrorResponse() {
		return this.error;
	}

}
