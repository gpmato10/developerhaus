package com.developerhaus.post.dao;

import java.util.List;

import com.developerhaus.domain.File;
import com.developerhaus.domain.PostFile;

public interface PostFileDAO {

	public int insertPostFile(PostFile postFile);
	public List<PostFile> getPostFileList(int postSeq);
	public int insertPostFile(int postSeq, File[] files);
}
