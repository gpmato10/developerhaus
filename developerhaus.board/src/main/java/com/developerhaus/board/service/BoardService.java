package com.developerhaus.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.developerhaus.domain.Board;

public interface BoardService {

	public List<Board> list();

	public int insert(Board board);
	
	public Board view(int postSeq);
	
	public int update(Board board);
	
	public int delete(int postSeq);
}
