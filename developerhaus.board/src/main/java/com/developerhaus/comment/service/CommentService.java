package com.developerhaus.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.developerhaus.domain.Comment;
import com.developerhaus.domain.Post;

@Service
public interface CommentService {

	public List<Comment> list(Post post);

	public int add(Comment comment);
	
	public int update(Comment comment);
	
	public int remove(Comment comment);
	
	public int removeAll(Comment comment);
}
