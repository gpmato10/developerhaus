package developerhaus.repository.jdbc;

import static developerhaus.repository.jdbc.RepositoryUtils.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.spec.PSource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.UserRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

public class JdbcUserRepository implements UserRepository, TableStrategyAware{
	
	public final static String TABLE_NAME = "USERS";
	public final static String ALIAS = "user";
	
	// DB 의존성을 제거하기 위해 DB컬럼명 변화에 상관없이 대표되는 속성명 정의
	// 정의된 속성명과 도메인 명의 관계 : 도메인명의 UPPER_CASE가 속성명이여야 한다는 규칙정의 필요
	// TODO : 쿼리결과 도메인 속성명과 매핑하기 위한 정책 수립(Spring JDBC 붙인 후 다시 생각)
//	public final static String SEQ = addAliasToColumn(ALIAS, "seq");  		// 시퀀스
//	public final static String ID = addAliasToColumn(ALIAS, "id");  				// 아이디 
//	public final static String NAME = addAliasToColumn(ALIAS,	"name");  			// 이름
//	public final static String PASSWORD = addAliasToColumn(ALIAS, "password");	// 비밀번호 
//	public final static String POINT = addAliasToColumn(ALIAS, "point");  			// 포인트

	public final static String SEQ = "seq";  			// 시퀀스
	public final static String ID = "id";  				// 아이디 
	public final static String NAME = "name";  			// 이름
	public final static String PASSWORD = "password";	// 비밀번호 
	public final static String POINT = "point";  		// 포인트

	protected SimpleJdbcTemplate template;
	
	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}
	
	@Override
	public User get(Integer id) {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion<String, CriterionOperator, Integer>("seq", CriterionOperator.EQ, id));
		
		SqlBuilder sqlBuilder = new SqlBuilder(this, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		
		return template.queryForObject(sql, userRowMapper, sqlBuilder.getMapSqlParameterSource());

	}

	@Override
	public List<User> list(Criteria criteria) {
		
		SqlBuilder sqlBuilder = new SqlBuilder(this, criteria);
		String sql = sqlBuilder.selectAll().from().where().order().build();
		
		return template.query(sql, userRowMapper, sqlBuilder.getMapSqlParameterSource());
	}

	@Override
	public boolean update(User domain) {
		
		MapSqlParameterSource msps=null;
		Criteria criteria = new DefaultCriteria();
		SqlBuilder sqlBuilder = new SqlBuilder(this, criteria);
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
	public List<UserPoint> getUserPointList() {
		String sql = "select * from userpoint where userid = :id ";
		//pointRepository.getUserPintList(new Criteria().add(new Criterion("",,"")));
		return null;//pointRepository.getList(new Criteria().add(new Criterion("",,"")));;
	}

	@Override
	public TableStrategy getTableStrategy() {
		
		return new DefaultTableStrategy(TABLE_NAME, ALIAS)
			.setAllColumn(SEQ, ID, NAME, PASSWORD, POINT);
	}
	
	private RowMapper<User> userRowMapper = new RowMapper<User>(){
		
		public User mapRow(ResultSet resultSet, int i ) throws SQLException{
			
			int seq = resultSet.getInt(	getColumnName(SEQ) );
			String id = resultSet.getString( getColumnName(ID) );
			String name	= resultSet.getString( getColumnName(NAME) );
			String password = resultSet.getString( getColumnName(PASSWORD) );
			int point = resultSet.getInt( getColumnName(POINT) );
			
			return new User(seq, name, id, password, point, null);
		}
	};
}
