package developerhaus.repository.jdbc;

import static developerhaus.repository.jdbc.RepositoryUtils.*;


import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.UserRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.mapper.UserRowMapper;

public class JdbcUserRepository implements UserRepository{
	
	// DB 의존성을 제거하기 위해 DB컬럼명 변화에 상관없이 대표되는 속성명 정의
	// 정의된 속성명과 도메인 명의 관계 : 도메인명의 UPPER_CASE가 속성명이여야 한다는 규칙정의 필요
	// TODO : 쿼리결과 도메인 속성명과 매핑하기 위한 정책 수립(Spring JDBC 붙인 후 다시 생각)
//	public final static String SEQ = addAliasToColumn(ALIAS, "seq");  		// 시퀀스
//	public final static String ID = addAliasToColumn(ALIAS, "id");  				// 아이디 
//	public final static String NAME = addAliasToColumn(ALIAS,	"name");  			// 이름
//	public final static String PASSWORD = addAliasToColumn(ALIAS, "password");	// 비밀번호 
//	public final static String POINT = addAliasToColumn(ALIAS, "point");  			// 포인트

	SimpleJdbcTemplate template;
	
	UserRowMapper mappedUser = new UserRowMapper();
	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}
	
	@Override
	public User get(Integer id) {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>("seq", CriterionOperator.EQ, id));
		
		SqlBuilder sqlBuilder = new SqlBuilder(mappedUser, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		System.out.println(sql);
		
		return template.queryForObject(sql,mappedUser, sqlBuilder.getMapSqlParameterSource());

	}

	@Override
	public List<User> list(Criteria criteria) {
		
		SqlBuilder sqlBuilder = new SqlBuilder(mappedUser, criteria);
		String sql = sqlBuilder.selectAll().from().where().order().build();
		
		return template.query(sql, mappedUser, sqlBuilder.getMapSqlParameterSource());
	}

	@Override
	public boolean update(User domain) {
		
		MapSqlParameterSource msps=null;
		Criteria criteria = new DefaultCriteria();
		SqlBuilder sqlBuilder = new SqlBuilder(mappedUser, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		sql=" update ";
		System.out.println(sql);	
		
		
		int result = template.update(sql, msps);
		if (result>0){
			return true;
		}else{
			return false;
		}
	}



	@Override
	public List<UserPoint> getUserPointList(Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
