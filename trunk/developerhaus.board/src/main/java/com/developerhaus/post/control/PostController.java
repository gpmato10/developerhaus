package com.developerhaus.post.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.developerhaus.domain.Post;
import com.developerhaus.post.service.PostService;

@Controller
@RequestMapping("/post")
public class PostController {

	@Autowired
	PostService postService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		System.out.println("list");
		
		List list = postService.list();
		System.out.println("list:"+list);
		model.addAttribute("list", list);
		return "post/list";
	}

	@RequestMapping(value = "/view/{postSeq}", method = RequestMethod.GET)
	public String view(@PathVariable("postSeq") int postSeq, HttpServletRequest request, Model model) {
		Post post = postService.view(postSeq);
		model.addAttribute("post", post);
		return "post/view";
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public String insert(HttpServletRequest request, Model model) {
		System.out.println("insert get");
		return "post/insert";
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String save(Post post, HttpServletRequest request, Model model) {
		if(post.getPostSeq() > 0) {
			postService.update(post);
		} else {
			postService.insert(post);
		}
		model.addAttribute("post", post);
		return "post/view";
	}
	
	@RequestMapping(value = "/delete/{postSeq}", method = RequestMethod.GET)
	public String delete(@PathVariable("postSeq") int postSeq, HttpServletRequest request, Model model) {
		postService.delete(postSeq);
		return list(request, model);
	}
}
