package webfeed.model;

import com.google.cloud.datastore.Entity;

public class Post {
	
	public static final String KIND = "Post";
	
	private Long id;
	public static final String ID = "id";
	
	private String text;
	public static final String TEXT = "postText";
	
	private Long authorId;
	public static final String AUTHOR_ID = "authorId";
	
	private Long createdDate;
	public static final String CREATED_DATE = "createdDate";
	
	private long likesCount;
	
	private long commentsCount;
	
	public static final String IS_ACTIVE = "isActive";
	
	public Post() {
		
	}
	
	public Post(Entity entity) {
		
		this.setId( entity.getKey().getId());
		this.setPostText(entity.getString(TEXT));
		this.setCreatedDate(entity.getLong(CREATED_DATE));
		this.setAuthorId(entity.getLong(AUTHOR_ID));
	}
	
	public String getPostText() {
		return text;
	}

	public void setPostText(String postText) {
		this.text = postText;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(long likesCount) {
		this.likesCount = likesCount;
	}

	public long getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(long commentsCount) {
		this.commentsCount = commentsCount;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		
		this.createdDate = createdDate;

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

	public Error validate() {
		
		if(this.text == null || this.text.isBlank()) {
			return new Error("Invalid Post Text","Post text should be provided");		
		}
		
		if(this.authorId == null) {
			return new Error("Invalid Author Id","AuthorId should be provided");
		}
		
		return null;
	}






}
