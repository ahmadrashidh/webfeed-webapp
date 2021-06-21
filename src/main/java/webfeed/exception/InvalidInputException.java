package webfeed.exception;

public class InvalidInputException extends Exception {
	
	private static final long serialVersionUID = 1L;

	private final String title;
	
	private final String message;
	
	public InvalidInputException(String title, String message) {
		this.title = title;
		this.message = message;
	}

	public String getTitle() {
		return title;
	}


	public String getMessage() {
		return message;
	}

}
