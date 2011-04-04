package com.developerhaus.domain;

public class PostFile {

	private int postSeq;
	private int fileSeq;
	
	private String fileNm;
	private String fileExt;
	
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public int getPostSeq() {
		return postSeq;
	}
	public void setPostSeq(int postSeq) {
		this.postSeq = postSeq;
	}
	public void setFileSeq(int fileSeq) {
		this.fileSeq = fileSeq;
	}
	public int getFileSeq() {
		return fileSeq;
	}
	
}
