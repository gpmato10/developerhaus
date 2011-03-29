package com.developerhaus.domain;

public class Comment {
	
	private int commentSeq;
	private int postSeq;
	private String comment;
	private int regUsr;
	private String regDt;
	
	public int getCommentSeq() {
		return commentSeq;
	}
	public void setCommentSeq(int commentSeq) {
		this.commentSeq = commentSeq;
	}
	public int getPostSeq() {
		return postSeq;
	}
	public void setPostSeq(int postSeq) {
		this.postSeq = postSeq;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	@Override
	public String toString() {
		return "Comment [commentSeq=" + commentSeq + ", postSeq=" + postSeq
				+ ", comment=" + comment + ", regUsr=" + regUsr + ", regDt="
				+ regDt + "]";
	}
	

}
