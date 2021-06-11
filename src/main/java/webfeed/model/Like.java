package webfeed.model;

import com.google.cloud.datastore.Entity;

public class Like {
	
	private transient String postId;
	
	private String likedBy;

	public String getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(String likedBy) {
		this.likedBy = likedBy;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public Like getLikeFromEntity(Entity entity) {
		
		this.setLikedBy(entity.getString("likedBy"));
		return this;
	}
	
	

}
