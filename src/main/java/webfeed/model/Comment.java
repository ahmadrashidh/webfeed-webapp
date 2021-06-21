package webfeed.model;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;

public class Comment {
	
	public static final String KIND = "Comment";
	
	private transient Long id;
	public static final String ID = "id";
	
	private transient Long postId;
	
	private Long parentId;
	public static final String PARENT_ID = "parentId";
	
	private String text;
	public static final String TEXT = "text";
	
	private Long commentedBy;
	public static final String COMMENTED_BY = "commentedBy";
	
	public static final String CREATED_DATE = "createdDate";
	
	public static final String IS_ACTIVE = "isActive";
	
	public static final String PARENT_PATH = "parentPath";
	
	
	
	
	public Comment() {
		
	}
	
	public Comment(Entity entity) {
		this.setText(entity.getString(TEXT));
		this.setCommentedBy(entity.getLong(COMMENTED_BY));
	}
	

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the commentedBy
	 */
	public Long getCommentedBy() {
		return commentedBy;
	}

	/**
	 * @param commentedBy the commentedBy to set
	 */
	public void setCommentedBy(Long commentedBy) {
		this.commentedBy = commentedBy;
	}

	public Error validate() {
		if(this.text == null || this.text.isBlank()) {
			return new Error("Invalid Post Text","Post text is required field. Post Text cannot be missing or empty");		
		}
		
		if(this.commentedBy == null) {
			return new Error("Invalid Author Id","Author ID is required field. Author ID cannoot be missing or empty");
		}
		
		return null;
	}


	public static List<Value<String>> getUpdatedParentPath(List<Value<String>> oldParentPath, Comment comment) {
		
		List<Value<String>> parentPaths = new ArrayList<>(oldParentPath);
		parentPaths.add( StringValue.of(comment.getParentId().toString()));
		return parentPaths;
	}


}
