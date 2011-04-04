package com.developerhaus.domain;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class Post {

	private int postSeq;
	private String title;
	private String contents;
	private int regUsr;
	private String regDt;
	private int modUsr;
	private int upPostSeq;
	private int odr;
	private int lvl;
	
	private CommonsMultipartFile[] uploadedFiles;
	private PostFile[] postFiles;
	
	public int getPostSeq() {
		return postSeq;
	}
	public void setPostSeq(int postSeq) {
		this.postSeq = postSeq;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getRegUsr() {
		return regUsr;
	}
	public void setRegUsr(int regUsr) {
		this.regUsr = regUsr;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public void setModUsr(int modUsr) {
		this.modUsr = modUsr;
	}
	public int getModUsr() {
		return modUsr;
	}
	public int getUpPostSeq() {
		return upPostSeq;
	}
	public void setUpPostSeq(int upPostSeq) {
		this.upPostSeq = upPostSeq;
	}
	public int getOdr() {
		return odr;
	}
	public void setOdr(int odr) {
		this.odr = odr;
	}
	public int getLvl() {
		return lvl;
	}
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	public void setPostFiles(PostFile[] postFiles) {
		this.postFiles = postFiles;
	}
	public PostFile[] getPostFiles() {
		return postFiles;
	}
	public void setUploadedFiles(CommonsMultipartFile[] uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}
	public CommonsMultipartFile[] getUploadedFiles() {
		return uploadedFiles;
	}
}
