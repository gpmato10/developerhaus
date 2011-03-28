package com.developerhaus.post.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developerhaus.domain.Post;
import com.developerhaus.post.dao.PostDAO;
import com.developerhaus.post.service.PostService;


@Service
public class PostServiceImpl implements PostService {

	@Autowired
	PostDAO postDAO;
	
	public List<Post> list() {
		return postDAO.list();
	}

	public int insert(Post post) {
		post.setPostSeq(postDAO.getPostSeq());
		return postDAO.insert(post);
	}

	public int delete(int postSeq) {
		return postDAO.delete(postSeq);
	}

	public int update(Post post) {
		return postDAO.update(post);
	}

	public Post view(int postSeq) {
		return postDAO.view(postSeq);
	}

}
