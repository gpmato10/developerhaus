package com.developerhaus.comment;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.developerhaus.comment.dao.impl.CommentDAOJdbc;
import com.developerhaus.domain.Comment;
import com.developerhaus.domain.Post;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="applicationContext-dataSource.xml")
public class CommentDAOTest {

	@Autowired
	DataSource dataSource;
	
	
	@Test
	public void insertAndDeleteTest(){
		CommentDAOJdbc commentDao = new CommentDAOJdbc();
		commentDao.setDataSource(dataSource);
		Comment comment = new Comment();
		comment.setComment("이건 뭥미222");
		comment.setPostSeq(1);
		comment.setRegUsr(1);
		commentDao.insert(comment);
		Post post = new Post();
		post.setPostSeq(1);
		assertEquals(1,commentDao.list(post).size());
		comment.setCommentSeq(0);
//		commentDao.delete(comment);
//		assertEquals(0,commentDao.list(post).size());
	}
	@Test
	public void getListAndDeleteAllTest(){
		CommentDAOJdbc commentDao = new CommentDAOJdbc();
		commentDao.setDataSource(dataSource);
		Comment comment = new Comment();
		comment.setComment("이건 뭥미");
		comment.setPostSeq(2);
		comment.setRegUsr(1);
		commentDao.insert(comment);
		commentDao.insert(comment);
		commentDao.insert(comment);
		commentDao.insert(comment);
		Post post = new Post();
		post.setPostSeq(2);
		assertEquals(4,commentDao.list(post).size());
		commentDao.deleteAll(comment);
		assertEquals(0,commentDao.list(post).size());
	}
}
