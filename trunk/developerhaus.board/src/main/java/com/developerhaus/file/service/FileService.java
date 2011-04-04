package com.developerhaus.file.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.developerhaus.domain.File;

public interface FileService {

	public File get(int fileSeq);
	public int insert(File file);
	public int delete(int fileSeq);
	
	public File insertDataFile(CommonsMultipartFile multipartFile, int regUsr) throws RuntimeException;
	public File[] insertDataFile(CommonsMultipartFile[] multipartFiles, int regUsr) throws RuntimeException;
	
}
