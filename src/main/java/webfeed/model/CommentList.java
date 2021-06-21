package webfeed.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.datastore.Entity;

public class CommentList {

	private String text;

	private Long authorId;

	private Long id;

	private transient Long parentId;
	
	private long createdDate;

	List<CommentList> comments;

	public CommentList() {

	}

	public CommentList(Entity entity) {
		comments = new ArrayList<>();
		this.setText(entity.getString(Comment.TEXT));
		this.setAuthorId(entity.getLong(Comment.COMMENTED_BY));
		this.setParentId(entity.getLong(Comment.PARENT_ID));
		this.setCreatedDate(entity.getLong(Comment.CREATED_DATE));
		this.setId(entity.getKey().getId());
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	

	public List<CommentList> getComments() {
		return comments;
	}

	public void setComments(List<CommentList> comments) {
		this.comments = comments;
	}

	public static List<CommentList> generateCommentThread(List<CommentList> allComments, Long parentId) {
		
		Map<String,CommentList> commentsForAccess = new LinkedHashMap<>();
		
		allComments.forEach(comment -> {
			commentsForAccess.put(comment.getId().toString(), comment);
		});
		
		List<CommentList> commentThread = new ArrayList<>();
		for (CommentList comment : commentsForAccess.values()) {
			if (comment.getParentId() != null && !comment.getParentId().equals(parentId)) {
				commentsForAccess.get(comment.getParentId().toString()).addComment(comment);
			} else {
				commentThread.add(comment);
			}
		}

		return commentThread;

	}

	/**
	 * @return the authorId
	 */
	public Long getAuthorId() {
		return authorId;
	}

	/**
	 * @param authorId the authorId to set
	 */
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the createdDate
	 */
	public long getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}


}
