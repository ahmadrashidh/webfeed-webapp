package webfeed.model;

public class Error {

	private String errorMessage;

	public Error() {

	}

	public Error(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

}
