package webfeed.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;

class CommentTest {


	@Test
	void validating_TextAsNull_ReturnsError() {

		Comment comment = new Comment();
		comment.setCommentedBy(3434242324324234L);

		assertTrue(comment.validate() != null);

	}

	@ParameterizedTest
	@ValueSource(strings = {""," "})
	void validating_TextAsEmptyOrBlank_ReturnsError(String arg) {
		
		Comment comment = new Comment();
		comment.setCommentedBy(273623232L);
		comment.setText(arg);

		assertNotNull(comment.validate());

	}
	

	@Test
	void validating_CommentedByAsNull_ReturnsError() {
		
		Comment comment = new Comment();
		comment.setText("Hello");

		assertTrue(comment.validate() != null);
		
	}
	
	
	@Test
	void validating_InvalidText_returnsAppropriateError() {
		
		Comment comment = new Comment();
		comment.setCommentedBy(3434242324324234L);
		Error error = comment.validate();

		assertEquals(error.getTitle(), "Invalid Comment Text");
		assertEquals(error.getDescription(), "Comment text is required field. It cannot be missing or empty");

		
	}
	
	@Test
	void validating_InvalidCommentBy_returnsAppropriateError() {
		
		Comment comment = new Comment();
		comment.setText("fdkdlsf");
		Error error = comment.validate();

		assertEquals(error.getTitle(), "Invalid Commented By Id");
		assertEquals(error.getDescription(), "Commented By is required field. It cannot be missing or empty");

		
	}
	
	@Test
	void getUpdatedParentPath_EmptyParentPath_ReturnsUpdatedParentPath() {
		
		Comment comment = new Comment();
		comment.setParentId(233242323424L);
		
		List<Value<String>> parentPath = new ArrayList<>();
		
		List<Value<String>> updatedParentPath = Comment.getUpdatedParentPath(parentPath, comment);
		
		assertEquals(StringValue.of(comment.getParentId().toString()) ,  updatedParentPath.get(0));
		
	}
	
	@Test
	void getUpdatedParentPath_ParentPath_ReturnsUpdatedParentPath() {
		
		Comment comment = new Comment();
		comment.setParentId(233242323424L);
		
		List<Value<String>> parentPath = new ArrayList<>();
		parentPath.add(StringValue.of("343232"));
		List<Value<String>> updatedParentPath = Comment.getUpdatedParentPath(parentPath, comment);
		
		assertEquals(StringValue.of("343232") ,  updatedParentPath.get(0));
		assertEquals(StringValue.of(comment.getParentId().toString()) ,  updatedParentPath.get(1));
		
	}

}
