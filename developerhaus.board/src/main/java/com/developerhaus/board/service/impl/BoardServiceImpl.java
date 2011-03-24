package com.developerhaus.board.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developerhaus.board.dao.BoardDAO;
import com.developerhaus.board.service.BoardService;
import com.developerhaus.domain.Board;


@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDAO boardDAO;
	
	public List<Board> list() {
		return boardDAO.list();
	}

	public int insert(Board board) {
		board.setPostSeq(boardDAO.getPostSeq());
		return boardDAO.insert(board);
	}

	public int delete(int postSeq) {
		return boardDAO.delete(postSeq);
	}

	public int update(Board board) {
		return boardDAO.update(board);
	}

	public Board view(int postSeq) {
		return boardDAO.view(postSeq);
	}

}
