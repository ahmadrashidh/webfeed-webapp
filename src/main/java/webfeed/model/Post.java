package webfeed.model;

import java.util.List;

import com.google.cloud.datastore.Entity;

public class Post {
	
	private String id;
	
	private String postText;
	
	private String createdOn;
	
	private List<Like> likes;
	

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Post getPostFromEntity(Entity entity) {
		this.setId(entity.getKey().getId().toString());
		this.setPostText(entity.getString("postText"));
		this.setCreatedOn(entity.getTimestamp("createdOn").toString());
		return this;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String timeStamp) {
		this.createdOn = timeStamp;
	}

	public List<Like> getLikes() {
		return likes;
	}

	public void setLikes(List<Like> likes) {
		this.likes = likes;
	}
	
	

}
