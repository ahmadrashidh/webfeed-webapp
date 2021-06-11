package webfeed.model;

import com.google.cloud.datastore.Entity;

public class Comment {
	
	private String parentCommentId;
	
	private String text;
	
	private String author;
	
	private transient String postId;
	
	private transient String id;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(String parentCommentId) {
		this.parentCommentId = parentCommentId;
	}

	public Comment getCommentFromEntity(Entity next) {
		this.setText(next.getString("text"));
		return this;
		
	}


	

}
