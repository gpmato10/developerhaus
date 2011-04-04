package com.developerhaus.post.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developerhaus.domain.File;
import com.developerhaus.domain.PageInfo;
import com.developerhaus.domain.Post;
import com.developerhaus.domain.PostFile;
import com.developerhaus.file.service.FileService;
import com.developerhaus.post.dao.PostDAO;
import com.developerhaus.post.dao.PostFileDAO;
import com.developerhaus.post.service.PostService;


@Service
public class PostServiceImpl implements PostService {

	@Autowired
	PostDAO postDAO;
	
	@Autowired
	PostFileDAO postFileDAO;
	
	@Autowired
	FileService fileService;
	
	
	public List<Post> list() {
		return postDAO.list();
	}

	public int insert(Post post) {
		post.setPostSeq(postDAO.getPostSeq());
		// 파일 저장
		int result = postDAO.insert(post);
		if(post.getUploadedFiles()!=null) {
			File[] files = fileService.insertDataFile(post.getUploadedFiles(), post.getRegUsr());
			postFileDAO.insertPostFile(post.getPostSeq(), files);
		}
		return result;
	}

	public int delete(int postSeq) {
		return postDAO.delete(postSeq);
	}

	public int update(Post post) {
		return postDAO.update(post);
	}

	public Post view(int postSeq) {
		System.out.println("view in service");
		Post post = postDAO.view(postSeq);
		List<PostFile> postFileList = postFileDAO.getPostFileList(post.getPostSeq());
		System.out.println("postFileList.size() : "+postFileList.size());
		post.setPostFiles((PostFile[])postFileList.toArray(new PostFile[postFileList.size()]));
		System.out.println(post.getPostFiles().length);
		return post;
	}

	public List list(int page) {
		
		int startRowNum = (page - 1) * PageInfo.ROW_PER_PAGE;
		int endRowNum = page * PageInfo.ROW_PER_PAGE	;
		
		return postDAO.list(startRowNum, endRowNum);
	}

	public PageInfo getPageInfo(int page) {
		
		int totalCount = postDAO.getTotalCount();
		
//		전체 페이지 개수
		int pageCount = totalCount / PageInfo.ROW_PER_PAGE;
		if(totalCount % PageInfo.ROW_PER_PAGE > 0){
			pageCount++;
		}
		
//		블럭 개수
		int block = pageCount / PageInfo.COUNT_PER_BLOCK;
		if(pageCount % PageInfo.COUNT_PER_BLOCK > 0){
			block++;
		}
		
		int startPage = ( (page - 1) / PageInfo.COUNT_PER_BLOCK ) * PageInfo.COUNT_PER_BLOCK + 1;
		
		return new PageInfo(page, pageCount, block, startPage);
	}

}
