package com.developerhaus.board.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.developerhaus.board.dao.BoardDAO;
import com.developerhaus.domain.Board;

@Repository
public class BoardDAOJdbc implements BoardDAO {

	private SimpleJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
	public List<Board> list() {
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT POST_ID, TITLE, CONTENTS, REG_USR, REG_DT, MOD_USR, MOD_DT	");
		sb.append("	FROM BOARD	");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<Board>(Board.class));
	}

	public int insert(Board board) {
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO ");
		sb.append("	BOARD ");
		sb.append("	(POST_ID, TITLE, CONTENTS, REG_USR, REG_DT, MOD_USR, MOD_DT)	");
		sb.append(" VALUES ");
		sb.append("	(:postId, :title, :contents, :regUsr, SYSDATE, 0, NULL)");
		return jdbcTemplate.update(sb.toString(), new BeanPropertySqlParameterSource(board));
	}

	public int delete(int postId) {
		Board board = new Board();
		board.setPostId(postId);
		StringBuffer sb = new StringBuffer();
		sb.append("	DELETE FROM BOARD ");
		sb.append("	WHERE POST_ID = :postId ");
		return jdbcTemplate.update(sb.toString(), new BeanPropertySqlParameterSource(board));
	}

	public int update(Board board) {
		StringBuffer sb = new StringBuffer();
		sb.append("	UPDATE BOARD ");
		sb.append("	SET TITLE = :title ");
		sb.append("	, CONTENTS = :contents	");
		sb.append("	, MOD_USR = :modUsr	");
		sb.append("	, MOD_DT = SYSDATE	");
		sb.append(" WHERE ");
		sb.append("	POST_ID = :postId ");
		return jdbcTemplate.update(sb.toString(), new BeanPropertySqlParameterSource(board));
	}

	public Board view(int postId) {
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT POST_ID, TITLE, CONTENTS, REG_USR, REG_DT, MOD_USR, MOD_DT	");
		sb.append("	FROM BOARD	");
		sb.append(" WHERE POST_ID = ? ");
		
		return jdbcTemplate.queryForObject(sb.toString(), new BeanPropertyRowMapper<Board>(Board.class), postId);
	}

	public int getPostSeq() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT MAX(POST_ID)+1 AS SEQ ");
		sb.append(" FROM BOARD ");
		return jdbcTemplate.queryForInt(sb.toString());
	}
	

}
