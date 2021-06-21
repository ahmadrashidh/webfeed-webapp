package webfeed.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreException;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import webfeed.exception.EntityNotFoundException;
import webfeed.model.Post;

public class PostDao implements Dao<Post> {

	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@Override
	public Optional<Post> get(long key) {
		return null;
	}
	
	public Post getById(long id) throws EntityNotFoundException {
		

		Key postKey = datastore.newKeyFactory().setKind(Post.KIND)
				.newKey(id);
		Entity postEntity = datastore.get(postKey);
		
		if(postEntity != null)
			return new Post(postEntity);
		else
			throw new EntityNotFoundException("Post Not Found", "Post against the provided Id is either unavailable or deleted");
		

		
	}

	@Override
	public List<Post> getAll() {
		
		List<Post> allPosts = new ArrayList<>();
		Query<Entity> query = Query.newEntityQueryBuilder()
								.setKind(Post.KIND)
								.setFilter(PropertyFilter.eq(Post.IS_ACTIVE, true))
								.setOrderBy(OrderBy.desc(Post.CREATED_DATE))
								.build();
		
		Iterator<Entity> posts = datastore.run(query);
		
		while(posts.hasNext()) {
			
			Post post = new Post(posts.next());
			allPosts.add(post);
		}
		
		return allPosts;
	}

	@Override
	public void create(Post post) throws DatastoreException {

		KeyFactory keyFactory = datastore.newKeyFactory().setKind(Post.KIND);
		Key postKey = datastore.allocateId(keyFactory.newKey());
		post.setId(postKey.getId());
		Entity postEntity = Entity.newBuilder(postKey)
				.set(Post.TEXT, post.getPostText())
				.set(Post.AUTHOR_ID, post.getAuthorId())
				.set(Post.IS_ACTIVE, true)
				.set(Post.CREATED_DATE, System.currentTimeMillis())
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
