package com.developerhaus.board.dao;

import java.util.List;

import com.developerhaus.domain.Board;

public interface BoardDAO {
	
	public int getPostSeq();

	public List<Board> list();

	public int insert(Board board);
	
	public Board view(int postId);
	
	public int update(Board board);
	
	public int delete(int postId);
	
}
