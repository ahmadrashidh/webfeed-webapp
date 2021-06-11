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
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import webfeed.model.Post;

public class PostDao implements Dao<Post> {

	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@Override
	public Optional<Post> get(long key) {
		
		return null;
	}

	@Override
	public List<Post> getAll() {
		
		List<Post> allPosts = new ArrayList<>();
		Query<Entity> query = Query.newEntityQueryBuilder()
								.setKind("Post")
								.setFilter(PropertyFilter.eq("isActive", true))
								.setOrderBy(OrderBy.desc("createdOn"))
								.build();
		
		Iterator<Entity> posts = datastore.run(query);
		
		while(posts.hasNext()) {
			Post post = new Post();
			post.getPostFromEntity(posts.next());
			
			allPosts.add(post);
		}
		
		return allPosts;
	}

	@Override
	public void create(Post post) {
		//Long postId = System.currentTimeMillis();
//		Key postKey = datastore.newKeyFactory()
//			    .setKind("Post")
//			    .newKey(postId.toString());
		KeyFactory keyFactory = datastore.newKeyFactory().setKind("Post");
		Key postKey = datastore.allocateId(keyFactory.newKey());
		post.setId(postKey.getId().toString());
		Entity postEntity = Entity.newBuilder(postKey)
				.set("postText", post.getPostText())
				.set("isActive", true)
				.set("createdOn", Timestamp.now())
				.build();
		datastore.put(postEntity);
		
	}

	@Override
	public void update(Post t, String[] params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Post t) {
		// TODO Auto-generated method stub

	}

}
