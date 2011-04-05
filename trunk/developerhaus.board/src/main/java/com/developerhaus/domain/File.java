package com.developerhaus.domain;

public class File {
	
	public File() {
		// TODO Auto-generated constructor stub
	}
	public File(int fileSeq) {
		this.fileSeq = fileSeq;
	}
	private int fileSeq;
	private String fileNm;
	private String filePth;
	private String fileExt;
	private long fileSz;
	private int regUsr;
	private String regDt;
	public int getFileSeq() {
		return fileSeq;
	}
	public void setFileSeq(int fileSeq) {
		this.fileSeq = fileSeq;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public String getFilePth() {
		return filePth;
	}
	public void setFilePth(String filePth) {
		this.filePth = filePth;
	}
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public long getFileSz() {
		return fileSz;
	}
	public void setFileSz(long fileSz) {
		this.fileSz = fileSz;
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

	
}
