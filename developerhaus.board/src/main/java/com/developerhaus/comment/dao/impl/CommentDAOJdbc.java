package com.developerhaus.comment.dao.impl;

import java.util.List;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.developerhaus.comment.dao.CommentDAO;
import com.developerhaus.domain.Post;
import com.developerhaus.domain.Comment;


@Repository
public class CommentDAOJdbc implements CommentDAO{

	private SimpleJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
	public List<Comment> list(Post post) {
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT COMMENT_SEQ, POST_SEQ, COMMENT, REG_USR, REG_DT	");
		sb.append("	FROM COMMENT WHERE POST_SEQ = :POST_SEQ	");
		return jdbcTemplate.query(sb.toString(),
				new BeanPropertyRowMapper<Comment>(Comment.class),
				new MapSqlParameterSource().addValue("POST_SEQ",post.getPostSeq()));
	}
	
	

	public int insert(Comment comment) {
		StringBuffer sb = new StringBuffer();
		sb.append("	insert into COMMENT (COMMENT_SEQ, POST_SEQ, COMMENT, REG_USR,  REG_DT)	");
		sb.append("	values( :commentSeq, :postSeq , :comment , :regUsr, SYSDATE	)");
		return jdbcTemplate.update(sb.toString(),new BeanPropertySqlParameterSource(getSeqSettedComment(comment)));
	}
	
	public Comment getSeqSettedComment(Comment comment){
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT MAX(COMMENT_SEQ)+1 AS COMMENT_SEQ	");
		sb.append("	FROM COMMENT WHERE POST_SEQ = :POST_SEQ	");
		comment.setCommentSeq(jdbcTemplate.queryForInt(sb.toString(), new MapSqlParameterSource().addValue("POST_SEQ",comment.getPostSeq())));
		return comment;
	}
	

	public int update(Comment comment) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delete(Comment comment) {
		StringBuffer sb = new StringBuffer();
		sb.append("	delete from COMMENT where COMMENT_SEQ = :commentSeq AND POST_SEQ = :postSeq	");
		return jdbcTemplate.update(sb.toString(),new BeanPropertySqlParameterSource(comment));
	}
	
	public int deleteAll(Comment comment) {
		StringBuffer sb = new StringBuffer();
		sb.append("	delete from COMMENT where POST_SEQ = :postSeq	");
		return jdbcTemplate.update(sb.toString(),new BeanPropertySqlParameterSource(comment));
	}

}
