package com.developerhaus.comment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developerhaus.comment.service.CommentService;
import com.developerhaus.domain.Comment;
import com.developerhaus.domain.Post;
import com.developerhaus.comment.dao.CommentDAO;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	CommentDAO commentDao;
	
	@Override
	public List<Comment> list(Post post) {
		return commentDao.list(post);
	}

	@Override
	public int add(Comment comment) {
		return commentDao.insert(comment);
	}

	@Override
	public int update(Comment comment) {
		return 0;
	}

	@Override
	public int remove(Comment comment) {
		return commentDao.delete(comment);
	}

	@Override
	public int removeAll(Comment comment) {
		return commentDao.deleteAll(comment);
	}

}
