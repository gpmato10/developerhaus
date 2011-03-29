package com.developerhaus.comment.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.developerhaus.comment.service.CommentService;
import com.developerhaus.domain.Comment;
import com.developerhaus.domain.Post;


@Controller
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	CommentService commentService;
	
	@RequestMapping(value = "/list/{postSeq}", method = RequestMethod.GET)
	public String list(@PathVariable("postSeq") int postSeq ,HttpServletRequest request , Model model){
		Post post = new Post();
		post.setPostSeq(postSeq);
		List list = commentService.list(post);
		model.addAttribute("list", list);
		model.addAttribute("postSeq", postSeq);
		return "comment/list";
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String add(Comment comment, HttpServletRequest request, Model model){
		commentService.add(comment);
		model.addAttribute("comment", comment);
		return list(comment.getPostSeq(),request,model);
	}
	
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public String delete(Comment comment,HttpServletRequest request, Model model){
		commentService.remove(comment);
		return list(comment.getPostSeq(),request,model);
	}
}
