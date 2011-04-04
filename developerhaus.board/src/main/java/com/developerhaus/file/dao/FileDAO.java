package com.developerhaus.file.dao;

import com.developerhaus.domain.File;

public interface FileDAO {

	public int getFileSeq();
	public File get(int fileSeq);
	public int insert(File file);
	public int delete(int fileSeq);
	
}
