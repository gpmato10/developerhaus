package developerhaus.repository.jdbc;

import static developerhaus.repository.jdbc.RepositoryUtils.addAliasToColumn;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.UserRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.jdbc.criteria.CriterionOperator;
import developerhaus.repository.jdbc.criteria.DefaultCriteria;
import developerhaus.repository.jdbc.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

public class JdbcUserRepository implements UserRepository, TableStrategyAware{
	
	public final static String TABLE_NAME = "USERS";
	public final static String ALIAS = "user";
	
	// DB 의존성을 제거하기 위해 DB컬럼명 변화에 상관없이 대표되는 컬럼명 정의
	// TODO : 쿼리결과 도메인 속성명과 매핑하기 위한 정책 수립(Spring JDBC 붙인 후 다시 생각)
	public final static String SEQEUNCE = addAliasToColumn(ALIAS, "seq");  		// 시퀀스
	public final static String ID = addAliasToColumn(ALIAS, "id");  				// 아이디 
	public final static String NAME = addAliasToColumn(ALIAS,	"name");  			// 이름
	public final static String PASSWORD = addAliasToColumn(ALIAS, "password");	// 비밀번호 
	public final static String POINT = addAliasToColumn(ALIAS, "point");  			// 포인트

	protected SimpleJdbcTemplate template;
	//DataSource dataSource;

	@Resource
	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
		//this.dataSource = dataSource;
	}
	
	@Override
	public User get(Integer id) {
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion(
								this.SEQEUNCE, 
								CriterionOperator.EQ, 
								id)
					);
		
		MapSqlParameterSource msps=null;
		
//		SqlBuilder sqlBuilder = new SqlBuilder(getTableStrategy(), criteria);
		SqlBuilder sqlBuilder = new SqlBuilder(this, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		System.out.println(sql);
		
//		이렇게 구현해 주세요.
//		Student student = JDBC구현체.get(sql, Student.class, msps);
		return template.queryForObject(sql, User.class, msps);
	}

	@Override
	public List<User> list(Criteria criteria) {
		MapSqlParameterSource msps=null;
		
//		SqlBuilder sqlBuilder = new SqlBuilder(getTableStrategy(), criteria);
		SqlBuilder sqlBuilder = new SqlBuilder(this, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		System.out.println(sql);	
		//return getDataSource().queryForList(sql, msps);
		return template.query(sql, new BeanPropertyRowMapper<User>(User.class), msps);
	}

	@Override
	public boolean update(User domain) {
		MapSqlParameterSource msps=null;
		Criteria criteria = new DefaultCriteria();
		SqlBuilder sqlBuilder = new SqlBuilder(this, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
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
		return null;
	}

	@Override
	public TableStrategy getTableStrategy() {
		
		return new DefaultTableStrategy(TABLE_NAME, ALIAS)
			.setAllColumn(SEQEUNCE, ID, NAME, PASSWORD, POINT);
	}

}
