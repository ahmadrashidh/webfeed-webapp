package webfeed.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import webfeed.model.Like;
import webfeed.model.Post;

public class LikeDao implements Dao<Like> {
	
	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@Override
	public Optional<Like> get(long key) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public List<Like> getAll() {
		
		return null;
		
	}
	
	public List<Like> getAllByPost(Long postId) {
		List<Like> allLikes = new ArrayList<>();
		Query<Entity> query = Query.newEntityQueryBuilder()
			    .setKind(Like.KIND)
			    .setFilter(PropertyFilter.hasAncestor(datastore.newKeyFactory()
			    .setKind(Post.KIND)
			    .newKey(postId)))
			    .build();
		
		Iterator<Entity> likes = datastore.run(query);
		
		while(likes.hasNext()) {
			Like like = new Like(likes.next());
			allLikes.add(like);
		}
		
		return allLikes;
	}

	@Override
	public void create(Like like) {
	
		KeyFactory keyFactory = datastore.newKeyFactory()
				.addAncestors(PathElement.of(Post.KIND, like.getPostId()))
				.setKind(Like.KIND);
		Key likeKey = datastore.allocateId(keyFactory.newKey());
		Entity likeEntity = Entity.newBuilder(likeKey)
				.set(Like.LIKED_BY, like.getLikedBy())
				.set(Like.IS_ACTIVE, true)
				.set(Like.CREATED_DATE, Timestamp.now())
				.build();
		datastore.put(likeEntity);
	}

	@Override
	public void update(Like t, String[] params) {
		// TODO Auto-generated method stub
		
	}

	
	public void delete(String postId) {
	
		
	}


	@Override
	public void delete(Like t) {
		// TODO Auto-generated method stub
		
	}
	
	

}
