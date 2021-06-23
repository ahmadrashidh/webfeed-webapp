package webfeed.model;

import com.google.cloud.datastore.Entity;

public class Like {
	
	public static final String KIND = "Like";
	
	private transient Long postId;
	
	private Long likedBy;
	public static final String LIKED_BY = "likedBy";
	
	public static final String CREATED_DATE = "createdDate";
	
	public static final String IS_ACTIVE = "isActive";
	
	public Like(Entity entity) {
		this.setLikedBy(entity.getLong(LIKED_BY));
	}
	
	public Like() {
		
	}

	/**
	 * @return the likedBy
	 */
	public Long getLikedBy() {
		return likedBy;
	}

	/**
	 * @param likedBy the likedBy to set
	 */
	public void setLikedBy(Long likedBy) {
		this.likedBy = likedBy;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Error validate() {
		
		if(this.likedBy == null ) {
			return new Error("Invalid Liked By", "Liked by is a required field. It cannot be missing or empty");
		}
		
		return null;
	}



}
