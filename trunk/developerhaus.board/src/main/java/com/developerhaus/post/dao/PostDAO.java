package com.developerhaus.post.dao;


import java.util.List;

import com.developerhaus.domain.Post;

public interface PostDAO {
	
	public int getPostSeq();

	public List<Post> list();

	public int insert(Post post);
	
	public Post view(int postSeq);
	
	public int update(Post post);
	
	public int delete(int postSeq);
	
}
