package com.developerhaus.domain;

public class PageInfo {
	
	// 한페이지 내의 게시글 개수
	public final static int ROW_PER_PAGE = 3;
	
	// 한블럭내의 페이지 개수
	public final static int COUNT_PER_BLOCK = 10;
	
	private int currentPage;
	private int pageCount;
	private int startPage;
	private int endPage;
	private int block;
	
	
	public PageInfo() {
		super();
	}


	public PageInfo(int currentPage, int pageCount, int block, int startPage) {
		super();
		this.currentPage = currentPage;
		this.pageCount = pageCount;
		this.block = block;
		this.startPage = startPage;
		
		// 시작페이지를 기준으로 마지막페이지를 구한다.
		int endPage = startPage + PageInfo.COUNT_PER_BLOCK - 1;
		if(endPage >= pageCount){
			endPage = pageCount;
		}
		this.endPage = endPage;
		
	}
	

	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public int getPageCount() {
		return pageCount;
	}


	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}


	public int getStartPage() {
		return startPage;
	}


	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}


	public int getEndPage() {
		return endPage;
	}


	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}


	public int getBlock() {
		return block;
	}


	public void setBlock(int block) {
		this.block = block;
	}
	
	public int getPreBlockStartPage(){
		return this.startPage - COUNT_PER_BLOCK;
	}
	
	public int getNextBlockStartPage(){
		return this.startPage + COUNT_PER_BLOCK;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PageInfo [currentPage=").append(currentPage)
				.append(", pageCount=").append(pageCount).append(", block=")
				.append(block).append(", startPage=").append(startPage)
				.append(", endPage=").append(endPage).append("]");
		return builder.toString();
	}
	
	


}
