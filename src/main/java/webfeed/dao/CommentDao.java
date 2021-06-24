package webfeed.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Value;

import webfeed.exception.EntityNotFoundException;
import webfeed.model.Comment;
import webfeed.model.CommentList;

public class CommentDao implements Dao<Comment> {

	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@Override
	public Optional<Comment> get(long key) {
		return null;
	}

	@Override
	public List<Comment> getAll() {

		return null;
	}

	@Override
	public void create(Comment comment) {

		KeyFactory keyFactory = datastore.newKeyFactory().setKind(Comment.KIND);
		Key commentKey = datastore.allocateId(keyFactory.newKey());
		
		Entity commentEntity = Entity.newBuilder(commentKey).set(Comment.TEXT, comment.getText())
				.set(Comment.COMMENTED_BY, comment.getCommentedBy())
				.set(Comment.PARENT_PATH, ListValue.of(comment.getPostId().toString()))
				.set(Comment.PARENT_ID, comment.getPostId())
				.set(Comment.IS_ACTIVE, true)
				.set(Comment.CREATED_DATE, System.currentTimeMillis())
				.build();
		datastore.put(commentEntity);
		comment.setId(commentKey.getId());

	}

	public void createUnderComment(Comment comment) {

		Key commentKey = datastore.newKeyFactory().setKind(Comment.KIND)
				.newKey(comment.getParentId());
		Entity commentEntity = datastore.get(commentKey);

		List<Value<String>> parentPathValues = commentEntity.getList(Comment.PARENT_PATH); //Immutable
		List<Value<String>> parentPaths = Comment.getUpdatedParentPath(parentPathValues, comment);

		KeyFactory keyFactory = datastore.newKeyFactory().setKind(Comment.KIND);
		commentKey = datastore.allocateId(keyFactory.newKey());
		comment.setId(commentKey.getId());

		commentEntity = Entity.newBuilder(commentKey)
				.set(Comment.TEXT, comment.getText())
				.set(Comment.COMMENTED_BY, comment.getCommentedBy())
				.set(Comment.PARENT_PATH, parentPaths)
				.set(Comment.PARENT_ID, comment.getParentId())
				.set(Comment.IS_ACTIVE, true)
				.set(Comment.CREATED_DATE, System.currentTimeMillis())
				.build();
		datastore.put(commentEntity);

	}

	@Override
	public void update(Comment t, String[] params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Comment t) {
		// TODO Auto-generated method stub

	}

	public List<CommentList> getAllByPost(Long postId) {

		List<CommentList> allComments = new ArrayList<>();
		System.out.println(postId.toString());
		

		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind(Comment.KIND)
				.setFilter(PropertyFilter.eq(Comment.PARENT_PATH, postId.toString()))
				.setOrderBy(OrderBy.asc(Comment.CREATED_DATE))
				.build();

		QueryResults<Entity> comments = datastore.run(query);

		while (comments.hasNext()) {
			CommentList comment = new CommentList(comments.next());
			allComments.add(comment);
		}
		
		
		return allComments;
	}

	public List<CommentList> getByComment(Long commentId) throws EntityNotFoundException {
		
		List<CommentList> allComments = new ArrayList<>();

		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind(Comment.KIND)
				.setFilter(PropertyFilter.eq(Comment.PARENT_PATH,commentId.toString()))
				.setOrderBy(OrderBy.asc(Comment.CREATED_DATE))
				.build();

		QueryResults<Entity> comments = datastore.run(query);
		
		if(!comments.hasNext())
			throw new EntityNotFoundException("Comment Not Found","Comment against provided ID is not available or deleted");

		while (comments.hasNext()) {
			CommentList comment = new CommentList(comments.next());
			allComments.add(comment);
		}
		
		
		return allComments;
	}

}
