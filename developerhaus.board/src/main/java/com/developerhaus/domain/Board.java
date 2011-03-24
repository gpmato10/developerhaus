package com.developerhaus.domain;

public class Board {

	private int postSeq;
	private String title;
	private String contents;
	private int regUsr;
	private String regDt;
	private int modUsr;
	
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
	
	
}
