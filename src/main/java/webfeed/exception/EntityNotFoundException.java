package webfeed.exception;

public class EntityNotFoundException extends Exception {
	

	private static final long serialVersionUID = 1L;

	private final String title;
	
	private final String message;
	
	public EntityNotFoundException(String title, String message) {
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
