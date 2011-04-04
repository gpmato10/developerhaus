package com.developerhaus.post.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.developerhaus.domain.PageInfo;
import com.developerhaus.domain.Post;
import com.developerhaus.post.service.PostService;

@Controller
@RequestMapping("/post")
public class PostController {

	@Autowired
	PostService postService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) throws Exception {
		System.out.println("list");
		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		System.out.println("page : " + page);
		
		List list = postService.list(page);
		PageInfo pageInfo = postService.getPageInfo(page);
		
		System.out.println("list:"+list);
		System.out.println("pageInfo : " + pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		
		return "post/list";
	}

	@RequestMapping(value = "/view/{postSeq}", method = RequestMethod.GET)
	public String view(@PathVariable("postSeq") int postSeq, HttpServletRequest request, Model model) {
		System.out.println("view in controller");
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
		return "redirect:view/"+post.getPostSeq();
	}
	
	@RequestMapping(value = "/delete/{postSeq}", method = RequestMethod.GET)
	public String delete(@PathVariable("postSeq") int postSeq, HttpServletRequest request, Model model) throws Exception {
		postService.delete(postSeq);
		return list(request, model);
	}
}
