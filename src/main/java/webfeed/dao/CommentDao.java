package webfeed.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;

import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Value;

import webfeed.model.Comment;
import webfeed.model.CommentList;

public class CommentDao implements Dao<Comment> {

	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@Override
	public Optional<Comment> get(long key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Comment comment) {

		KeyFactory keyFactory = datastore.newKeyFactory().setKind("Comment");
		Key commentKey = datastore.allocateId(keyFactory.newKey());
		comment.setId(commentKey.getId().toString());
		System.out.println("commentKey:" + commentKey);
		Entity commentEntity = Entity.newBuilder(commentKey).set("text", comment.getText())
				.set("commentedBy", comment.getAuthor()).set("parentPath", ListValue.of(comment.getPostId()))
				.set("parent", comment.getPostId()).set("isActive", true).set("createdOn", Timestamp.now()).build();
		datastore.put(commentEntity);

	}

	public void createUnderComment(Comment comment) {

		List<Comment> allComments = new ArrayList<>();
		Key commentKey = datastore.newKeyFactory().setKind("Comment")
				.newKey(Long.parseLong(comment.getParentCommentId()));
		Entity commentEntity = datastore.get(commentKey);

		List<Value<String>> parentPathValues = commentEntity.getList("parentPath");
		Value<String> parentCommentId = StringValue.of(comment.getParentCommentId());
		List<Value<String>> parentPaths = new ArrayList<>();
		parentPathValues.forEach(parentPaths::add);
		parentPaths.add(parentCommentId);

		KeyFactory keyFactory = datastore.newKeyFactory().setKind("Comment");
		commentKey = datastore.allocateId(keyFactory.newKey());
		comment.setId(commentKey.getId().toString());

		commentEntity = Entity.newBuilder(commentKey).set("text", comment.getText())
				.set("commentedBy", comment.getAuthor()).set("parentPath", parentPaths)
				.set("parent", comment.getParentCommentId()).set("isActive", true).set("createdOn", Timestamp.now())
				.build();
		System.out.println("Entity: " + commentEntity);
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

	public List<CommentList> getAllByPost(String postId) {

		Map<String, CommentList> allComments = new LinkedHashMap<>();

		Query<Entity> query = Query.newEntityQueryBuilder().setKind("Comment")
				.setFilter(PropertyFilter.eq("parentPath", postId))
				.setLimit(2)
				.setOrderBy(OrderBy.desc("createdOn"))
				.build();
			

		System.out.println("Query:" + query.toString());

		QueryResults<Entity> comments = datastore.run(query);

		System.out.println("CommentEntityIterator:" + comments);

		while (comments.hasNext()) {
			
			CommentList comment = new CommentList();
			comment.getCommentFromEntity(comments.next());
			allComments.put(comment.getId(), comment);
		}

		List<CommentList> commentThread = CommentList.getCommentThread(allComments, postId);
		commentThread.forEach(comment->System.out.println(comment.getText()));
		
		return commentThread;
	}

}
