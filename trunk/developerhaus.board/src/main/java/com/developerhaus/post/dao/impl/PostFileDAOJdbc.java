package com.developerhaus.post.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.developerhaus.domain.File;
import com.developerhaus.domain.Post;
import com.developerhaus.domain.PostFile;
import com.developerhaus.post.dao.PostFileDAO;

@Repository
public class PostFileDAOJdbc implements PostFileDAO {

private SimpleJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
	@Override
	public int insertPostFile(PostFile postFile) {
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO ");
		sb.append("	POST_FILE ");
		sb.append("	(POST_SEQ, FILE_SEQ)	");
		sb.append(" VALUES ");
		sb.append("	(:postSeq, :fileSeq)");
		return jdbcTemplate.update(sb.toString(), new BeanPropertySqlParameterSource(postFile));
	}

	@Override
	public List<PostFile> getPostFileList(int postSeq) {
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT A.POST_SEQ, A.FILE_SEQ, B.FILE_NM, B.FILE_EXT ");
		sb.append("	FROM POST_FILE A, FILE B	");
		sb.append("	WHERE A.FILE_SEQ = B.FILE_SEQ	");
		sb.append("	AND A.POST_SEQ = ?	");
		sb.append("	ORDER BY A.FILE_SEQ ");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<PostFile>(PostFile.class), postSeq);
	}

	@Override
	public int insertPostFile(int postSeq, File[] files) {
		PostFile postFile = new PostFile();
		postFile.setPostSeq(postSeq);
		int result = 0;
		for(int i=0;i<files.length;i++) {
			postFile.setFileSeq(files[i].getFileSeq());
			result += this.insertPostFile(postFile);
		}
		return result;
	}

}
