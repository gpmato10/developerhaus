package com.developerhaus.post.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.developerhaus.domain.PageInfo;
import com.developerhaus.domain.Post;
import com.developerhaus.post.dao.PostDAO;

@Repository
public class PostDAOJdbc implements PostDAO {

	private SimpleJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
	public List<Post> list() {
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT POST_SEQ, TITLE, CONTENTS, REG_USR, REG_DT, MOD_USR, MOD_DT, UP_POST_SEQ, ODR, LVL");
		sb.append("	FROM POST	");
		sb.append("	ORDER BY UP_POST_SEQ DESC, ODR, LVL ");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<Post>(Post.class));
	}

	public int insert(Post post) {
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO ");
		sb.append("	POST ");
		sb.append("	(POST_SEQ, TITLE, CONTENTS, REG_USR, REG_DT, MOD_USR, MOD_DT)	");
		sb.append(" VALUES ");
		sb.append("	(:postSeq, :title, :contents, :regUsr, SYSDATE, 0, NULL)");
		return jdbcTemplate.update(sb.toString(), new BeanPropertySqlParameterSource(post));
	}

	public int delete(int postSeq) {
		Post post = new Post();
		post.setPostSeq(postSeq);
		StringBuffer sb = new StringBuffer();
		sb.append("	DELETE FROM POST ");
		sb.append("	WHERE POST_SEQ = :postSeq ");
		return jdbcTemplate.update(sb.toString(), new BeanPropertySqlParameterSource(post));
	}

	public int update(Post post) {
		StringBuffer sb = new StringBuffer();
		sb.append("	UPDATE POST ");
		sb.append("	SET TITLE = :title ");
		sb.append("	, CONTENTS = :contents	");
		sb.append("	, MOD_USR = :modUsr	");
		sb.append("	, MOD_DT = SYSDATE	");
		sb.append(" WHERE ");
		sb.append("	POST_SEQ = :postSeq ");
		return jdbcTemplate.update(sb.toString(), new BeanPropertySqlParameterSource(post));
	}

	public Post view(int postSeq) {
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT POST_SEQ, TITLE, CONTENTS, REG_USR, REG_DT, MOD_USR, MOD_DT	");
		sb.append("	FROM POST	");
		sb.append(" WHERE POST_SEQ = ? ");
		
		return jdbcTemplate.queryForObject(sb.toString(), new BeanPropertyRowMapper<Post>(Post.class), postSeq);
	}

	public int getPostSeq() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT MAX(POST_SEQ)+1 AS SEQ ");
		sb.append(" FROM POST ");
		return jdbcTemplate.queryForInt(sb.toString());
	}


	public List list(int startRowNum, int endRowNum) {
		StringBuffer sb = new StringBuffer();

		// Oracle
//		sb.append("SELECT * ");
//		sb.append("	FROM ( ");
//		sb.append(" 	SELECT  rownum as ROW_NUM,  datas.* ");

		// HSQL
		sb.append(" 	SELECT  limit :startRowNum :rowPerPage  datas.* ");
		
		sb.append(" 		FROM ( ");
		
		sb.append("	SELECT POST_SEQ, TITLE, CONTENTS, REG_USR, REG_DT, MOD_USR, MOD_DT, UP_POST_SEQ, ODR, LVL");
		sb.append("	FROM POST	");
		sb.append("	ORDER BY UP_POST_SEQ DESC, ODR, LVL ");
		
		sb.append("		) datas ");
		
		// Oracle
//		sb.append("		  ) ");
//		sb.append("	  WHERE ROW_NUM > :startRowNum ");
//		sb.append("	    AND ROW_NUM <= :endRowNum ");
		
		System.out.println("query : " + sb.toString());
		System.out.println("startRowNum : " + startRowNum);
		System.out.println("endRowNum : " + endRowNum);
		
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("startRowNum", startRowNum);
		msps.addValue("endRowNum", endRowNum);
		msps.addValue("rowPerPage", PageInfo.ROW_PER_PAGE);
		
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<Post>(Post.class), msps);
	}

	public int getTotalCount() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("	SELECT count(1)");
		sb.append("	FROM POST	");

		return jdbcTemplate.queryForObject(sb.toString(), Integer.class);
	}
}
