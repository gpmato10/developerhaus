package com.developerhaus.file.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.developerhaus.domain.File;
import com.developerhaus.file.dao.FileDAO;

@Repository
public class FileDAOJdbc implements FileDAO {

	private SimpleJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
	public int delete(int fileSeq) {
		File file = new File();
		file.setFileSeq(fileSeq);
		StringBuffer sb = new StringBuffer();
		sb.append("	DELETE FROM FILE ");
		sb.append("	WHERE FILE_SEQ = :postSeq ");
		return jdbcTemplate.update(sb.toString(), new BeanPropertySqlParameterSource(file));
	}

	public File get(int fileSeq) {
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT FILE_SEQ, FILE_NM, FILE_PTH, FILE_EXT, FILE_SZ, REG_USR, REG_DT	");
		sb.append("	FROM FILE	");
		sb.append(" WHERE FILE_SEQ = ? ");
		
		return jdbcTemplate.queryForObject(sb.toString(), new BeanPropertyRowMapper<File>(File.class), fileSeq);
	}

	public long getFileSeq() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT MAX(FILE_SEQ)+1 AS SEQ ");
		sb.append(" FROM FILE ");
		return jdbcTemplate.queryForInt(sb.toString());
	}

	public int insert(File file) {
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO ");
		sb.append("	FILE ");
		sb.append("	(FILE_SEQ, FILE_NM, FILE_PTH, FILE_EXT, FILE_SZ, REG_USR, REG_DT)	");
		sb.append(" VALUES ");
		sb.append("	(:fileSeq, :fileNm, :filePth, :fileExt, fileSz, regUsr, SYSDATE)");
		return jdbcTemplate.update(sb.toString(), new BeanPropertySqlParameterSource(file));
	}

}
