package com.developerhaus.comment.dao;

import java.util.List;

import com.developerhaus.domain.Post;
import com.developerhaus.domain.Comment;

public interface CommentDAO {

//	public int getCommentSeq();

	public List<Comment> list(Post post);

	public int insert(Comment comment);
	
	public int update(Comment comment);
	
	public int delete(Comment comment);
}
