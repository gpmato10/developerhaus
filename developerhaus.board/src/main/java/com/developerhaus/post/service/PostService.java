package com.developerhaus.post.service;

import java.util.List;


import com.developerhaus.domain.PageInfo;
import com.developerhaus.domain.Post;

public interface PostService {
	
	public List<Post> list();

	public int insert(Post post);
	
	public Post view(int postSeq);
	
	public int update(Post post);
	
	public int delete(int postSeq);

	public List list(int page);
	
	public PageInfo getPageInfo(int page);
}
