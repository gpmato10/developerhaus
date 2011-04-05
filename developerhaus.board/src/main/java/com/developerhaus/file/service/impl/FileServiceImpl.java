package com.developerhaus.file.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.developerhaus.domain.File;
import com.developerhaus.file.dao.FileDAO;
import com.developerhaus.file.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private @Value("#{resourceProperties['ROOT_DIR']}") String FILE_ROOT_PATH;
	public static final String FILE_SEP = java.io.File.separator;
	
	@Autowired
	private FileDAO fileDAO;
	
	public int delete(int fileSeq) {
		return fileDAO.delete(fileSeq);
	}

	public File get(int fileSeq) {
		return fileDAO.get(fileSeq);
	}

	public int insert(File file) {
		return fileDAO.insert(file);
	}

	public File insertDataFile(CommonsMultipartFile multipartFile, int regUsr) {
		String destFlPth = getDestFilePath(multipartFile.getOriginalFilename());
		try {
			multipartFile.getFileItem().write(new java.io.File(FILE_ROOT_PATH + destFlPth));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File file = new File();
		file.setFileSeq(fileDAO.getFileSeq());
		file.setFilePth(destFlPth);
		file.setFileExt(StringUtils.getFilenameExtension(destFlPth));
		file.setFileNm(StringUtils.stripFilenameExtension(multipartFile.getOriginalFilename()));
		file.setFileSz(multipartFile.getSize());
		file.setRegUsr(regUsr);
		this.insert(file);
		return file;
	}

	public File[] insertDataFile(CommonsMultipartFile[] multipartFiles, int regUsr) throws RuntimeException {
		File[] files = new File[multipartFiles.length];
		for(int i=0;i<multipartFiles.length;i++) {
			files[i] = insertDataFile(multipartFiles[i], regUsr);
		}
		return files;
	}

	/**
	 * Desc    : 파일구분(flCls)와 원본파일명으로 파일 저장 위치를 결정한다.
	 * @Method : getDestFlPath
	 * @return
	 */
	
	private String getDestFilePath(String origFileName) {
		String dirPath = FILE_SEP + genDirName();
		java.io.File dirFile = new java.io.File(FILE_ROOT_PATH + dirPath);
		if(!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String destFlPath = dirPath + FILE_SEP + genFileName() + "." + StringUtils.getFilenameExtension(origFileName);
		return destFlPath;
	}
	
	/**
	 * Desc    : yyyy/MM 으로 파일디렉토리를 결정한다.
	 * @Method : genDirName
	 * @return
	 */
	private String genDirName(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat currentDateDf = new SimpleDateFormat("yyyy"+FILE_SEP+"MM");
		return currentDateDf.format(cal.getTime());
	}
	
	/**
	 * Desc    : yyyyMMddHHmmssSSS 으로 파일명을 결정한다.
	 * @Method : genFileName
	 * @return
	 */
	private String genFileName(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat currentTimeDf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return currentTimeDf.format(cal.getTime());
	}

}
