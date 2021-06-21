package webfeed.dao;

import java.util.List;
import java.util.Optional;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.ProjectionEntity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import webfeed.exception.EntityNotFoundException;
import webfeed.model.User;

public class UserDao implements Dao<User> {

	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public User getById(long id) {

		Key postKey = datastore.newKeyFactory().setKind(User.KIND).newKey(id);
		Entity userEntity = datastore.get(postKey);

		if (userEntity != null)
			return new User(userEntity);

		return null;
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isNewUser(String email) {
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(User.KIND)
				.setFilter(PropertyFilter.eq(User.EMAIL, email)).build();
		QueryResults<Entity> users = datastore.run(query);

		return users.hasNext() ? false : true;
	}

	public Long getUserId(String email) {
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(User.KIND)
				.setFilter(PropertyFilter.eq(User.EMAIL, email)).build();
		QueryResults<Entity> users = datastore.run(query);

		return users.hasNext() ? users.next().getKey().getId() : null;

	}

	@Override
	public void create(User user) {

		KeyFactory keyFactory = datastore.newKeyFactory().setKind(User.KIND);
		Key userKey = datastore.allocateId(keyFactory.newKey());
		Entity postEntity = Entity.newBuilder(userKey).set(User.NAME, user.getName()).set(User.EMAIL, user.getEmail())
				.set(User.PICTURE_LINK, user.getPictureLink()).set(User.IS_ACTIVE, false)
				.set(User.API_TOKEN, user.getApiToken()).set(User.SESSION_ID, user.getSessionId())
				.set(User.CREATED_DATE, System.currentTimeMillis()).build();
		datastore.put(postEntity);

	}

	@Override
	public void update(User user, String[] params) {

		Key userKey = datastore.newKeyFactory().setKind(User.KIND).newKey(user.getId());
		Entity postEntity = Entity.newBuilder(userKey).set(User.NAME, user.getName()).set(User.EMAIL, user.getEmail())
				.set(User.PICTURE_LINK, user.getPictureLink()).set(User.API_TOKEN, user.getApiToken())
				.set(User.SESSION_ID, user.getSessionId()).set(User.CREATED_DATE, System.currentTimeMillis()).build();
		datastore.put(postEntity);

	}

	@Override
	public void delete(User t) {

	}

	@Override
	public Optional<User> get(long key) {

		return null;
	}

	public String getSession(String apiToken) throws EntityNotFoundException {

		Query<ProjectionEntity> query = Query.newProjectionEntityQueryBuilder()
				.setKind("User")
				.setProjection(User.SESSION_ID)
				.setFilter(PropertyFilter.eq(User.API_TOKEN, apiToken))
				.build();
		
		QueryResults<ProjectionEntity> users = datastore.run(query);
		
		if(!users.hasNext())
			throw new EntityNotFoundException("Invalid API Token", "API Token is either missing, incorrect or expired");
		
		return users.next().getString(User.SESSION_ID);
		

		
	}

}
