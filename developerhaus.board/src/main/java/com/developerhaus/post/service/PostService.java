package com.developerhaus.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.developerhaus.domain.Post;

public interface PostService {

	public List<Post> list();

	public int insert(Post post);
	
	public Post view(int postSeq);
	
	public int update(Post post);
	
	public int delete(int postSeq);
}
