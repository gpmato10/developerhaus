package com.developerhaus.board.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.developerhaus.board.service.BoardService;
import com.developerhaus.domain.Board;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	BoardService boardService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		System.out.println("list");
		
		List list = boardService.list();
		System.out.println("list:"+list);
		model.addAttribute("list", list);
		return "board/list";
	}

	@RequestMapping(value = "/view/{postSeq}", method = RequestMethod.GET)
	public String view(@PathVariable("postSeq") int postSeq, HttpServletRequest request, Model model) {
		Board board = boardService.view(postSeq);
		model.addAttribute("board", board);
		return "board/view";
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public String insert(HttpServletRequest request, Model model) {
		System.out.println("insert get");
		return "board/insert";
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String save(Board board, HttpServletRequest request, Model model) {
		System.out.println("board.getTitle():"+board.getTitle());
		System.out.println("board.getContents():"+board.getContents());
		System.out.println("board.getPostSeq():"+board.getPostSeq());
		if(board.getPostSeq() > 0) {
			boardService.update(board);
		} else {
			boardService.insert(board);
		}
		model.addAttribute("board", board);
		return "board/view";
	}
	
	@RequestMapping(value = "/delete/{postSeq}", method = RequestMethod.GET)
	public String delete(@PathVariable("postSeq") int postSeq, HttpServletRequest request, Model model) {
		boardService.delete(postSeq);
		return list(request, model);
	}
}
