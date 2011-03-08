package developerhaus.repository.jdbc;


import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.UserRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.DefaultOrder;
import developerhaus.repository.criteria.JoinCriterion;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.mapper.UserRowMapper;

public class JdbcUserRepository implements UserRepository{
	
	private SimpleJdbcTemplate template;
//	private UserRowMapper mappedUser = new UserRowMapper();
	private User user = new User();
	private UserPoint userPoint = new UserPoint();
	
	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}
	
	@Override
	public User get(Integer id) {
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>(user, "seq", CriterionOperator.EQ, id));
		
		SqlBuilder sqlBuilder = new SqlBuilder(user, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		System.out.println(sql);
		System.out.println(sqlBuilder.getMapSqlParameterSource().getValues());
		
		return template.queryForObject(sql, new GeneralBeanPropertyRowMapper<User>(User.class), sqlBuilder.getMapSqlParameterSource());
	}
	

	@Override
	public List<User> list(Criteria criteria) {
		SqlBuilder sqlBuilder = new SqlBuilder(user, criteria);
		String sql = sqlBuilder.selectAll().from().where().order().build();
		System.out.println(":sql:"+sql);
		System.out.println(sqlBuilder.getMapSqlParameterSource().getValues());
		return template.query(sql, new GeneralBeanPropertyRowMapper<User>(User.class), sqlBuilder.getMapSqlParameterSource());
	}
	

	@Override
	public boolean update(User domain) {
		
		MapSqlParameterSource msps=null;
		Criteria criteria = new DefaultCriteria();
		SqlBuilder sqlBuilder = new SqlBuilder(user, criteria);
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
	public List<UserPoint> getUserPointList(User user) {
		Criteria criteria = new DefaultCriteria();
		
		Criterion jcriterion = new JoinCriterion(new User(), "seq", new UserPoint(), "userSeq");
		Criterion<String, CriterionOperator, Integer> criterion = new SingleValueCriterion<CriterionOperator, Integer>(new User(), "seq", CriterionOperator.EQ, user.getSeq());
		Order order = new DefaultOrder(new UserPoint(), "regDt", OrderType.DESC);

		criteria.add(jcriterion);
		criteria.add(criterion);
		criteria.add(order);
		
		SqlBuilder sqlBuilder = new SqlBuilder(userPoint, criteria);
		
		String sql = sqlBuilder.selectAll().from().where().order().build();
		System.out.println(sql);
		System.out.println(sqlBuilder.getMapSqlParameterSource().getValues());
		
//		return template.queryForObject(sql, new GeneralBeanPropertyRowMapper<User>(User.class), sqlBuilder.getMapSqlParameterSource());
		
		return template.query(sql, new GeneralBeanPropertyRowMapper<UserPoint>(UserPoint.class), sqlBuilder.getMapSqlParameterSource());
	}
	
	@Override
	public List<UserPoint> getUserPointListById(String id) {
		User user = new User();
		user.setId(id);
		return getUserPointList(user);
	}
}
