package webfeed.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LikeTest {

	
	@Test
	void validating_LikedByAsNull_ReturnsError() {

		Like like = new Like();

		assertNotNull(like.validate() != null);

	}
	
	@Test
	void validating_InvalidLikedBy_returnsAppropriateError() {
		
		Like like = new Like();
		Error error = like.validate();

		assertEquals("Invalid Liked By", error.getTitle());
		assertEquals("Liked by is a required field. It cannot be missing or empty", error.getDescription());

		
	}


}
