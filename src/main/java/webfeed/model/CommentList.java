package webfeed.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.cloud.datastore.Entity;

public class CommentList {

	private String text;

	private String author;
	
	private String id;
	
	private transient String parent;

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	List<CommentList> comments;

	public CommentList() {
		comments = new ArrayList<>();
	}
	
	public void addComment(CommentList comment) {
		comments.add(comment);
	}

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<CommentList> getComments() {
		return comments;
	}

	public void setComments(List<CommentList> comments) {
		this.comments = comments;
	}
	
	public CommentList getCommentFromEntity(Entity commentEntity) {
		this.setText(commentEntity.getString("text"));
		this.setAuthor(commentEntity.getString("commentedBy"));
		this.setParent(commentEntity.getString("parent"));
		this.setId(commentEntity.getKey().getId().toString());
		return this;
	}
	
	public static List<CommentList> getCommentThread(Map<String,CommentList> allComments, String postId) {
		
		List<CommentList> commentThread = new ArrayList<>();
		for(CommentList comment : allComments.values()) {
			if(comment.getParent() != null && !comment.getParent().equals(postId)) {
				allComments.get(comment.getParent()).addComment(comment);
			} else {
				commentThread.add(comment);
			}
		}
		
		return commentThread;
		
		
	}

}
