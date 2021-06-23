package webfeed.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;



class PostTest {

	@Test
	void validating_TextAsNull_ReturnsError() {

		Post post = new Post();
		post.setAuthorId(3434242324324234L);

		assertNotNull(post.validate());

	}

	@ParameterizedTest
	@ValueSource(strings = {""," "})
	void validating_TextAsEmptyOrBlank_ReturnsError(String arg) {
		
		Post post = new Post();
		post.setAuthorId(3434242324324234L);
		post.setPostText(arg);

		assertNotNull(post.validate());

	}
	

	@Test
	void validating_AuthorIdAsNull_ReturnsError() {
		
		Post post = new Post();
		post.setPostText("Hello");

		assertNotNull(post.validate());
		
	}
	
	
	@Test
	void validating_InvalidText_returnsAppropriateError() {
		
		Post post = new Post();
		post.setAuthorId(3434242324324234L);
		Error error = post.validate();

		assertEquals("Invalid Post Text",error.getTitle());
		assertEquals( "Post text is required field. It cannot be missing or empty",error.getDescription());

		
	}
	
	@Test
	void validating_InvalidAuthorId_returnsAppropriateError() {
		
		Post post = new Post();
		post.setPostText("Hello");
		Error error = post.validate();

		assertEquals("Invalid Author Id",error.getTitle());
		assertEquals( "AuthorId is required field. It cannot be missing or empty",error.getDescription());

		
	}
	
	
	
}
