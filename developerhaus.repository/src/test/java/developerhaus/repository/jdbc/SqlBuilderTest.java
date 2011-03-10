package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import developerhaus.domain.Student;
import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.DefaultOrder;
import developerhaus.repository.criteria.JoinCriterion;
import developerhaus.repository.criteria.MultiValueCriterion;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.exception.SqlBuilderException;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;
import developerhaus.repository.mapper.UserPointRowMapper;
import developerhaus.repository.mapper.UserRowMapper;


public class SqlBuilderTest {
	
	private TableStrategyAware studentTableStrategyAware;
//	private TableStrategyAware userRowMapper;
	private TableStrategyAware user;
	private TableStrategyAware userPoint;
	
	@Before
	public void setUp(){
		
		user = new User();
		userPoint = new UserPoint();
	}
	
	@Ignore
	@Test
	public void simpleSqlBuild() throws Exception {

		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategyAware);
		String sql = sqlBuilder.select("sno, sname, year, dep").from().build();
		this.oneSelectValidCheck(sql, "simpleSqlBuild1");
		
		
		SqlBuilder sqlBuilder2 = new SqlBuilder(studentTableStrategyAware);
		String sql2 = sqlBuilder2.select(" * ").from().build();
		this.oneSelectValidCheck(sql2, "simpleSqlBuild2");
		
		SqlBuilder sqlBuilder3 = new SqlBuilder(studentTableStrategyAware);
		String sql3 = sqlBuilder3.selectAll().from().build();
		this.oneSelectValidCheck(sql3, "simpleSqlBuild3");
	}
	
	
	@SuppressWarnings("unchecked")
	@Ignore
	@Test
	public void oneTableSelectBuildWithWhere() throws Exception {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add( new SingleValueCriterion<CriterionOperator, String>(
								user,
								"password", 
								CriterionOperator.EQ, 
								"1111") );
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>(
								user,
								"point", 
								CriterionOperator.GT, 
								0));
		
		SqlBuilder sqlBuilder = new SqlBuilder(user, criteria);
		
		String sql = sqlBuilder.selectAll().from().where().build();
		this.oneSelectValidCheck(sql, "oneTableSelectBuildWithWhere", sqlBuilder.getMapSqlParameterSource().getValues());
		
		Criteria criteria3 = new DefaultCriteria();
		criteria3.add(new SingleValueCriterion<CriterionOperator, String>(
								user,
								"name", 
								CriterionOperator.LIKE, 
								"희"));
		criteria3.add(new SingleValueCriterion<CriterionOperator, String>(
								user,
								"name", 
								CriterionOperator.LIKE_LEFT, 
								"성희"));
		criteria3.add(new SingleValueCriterion<CriterionOperator, String>(
								user,
								"name", 
								CriterionOperator.LIKE_RIGHT, 
								"박성"));
		
		SqlBuilder sqlBuilder3 = new SqlBuilder(user, criteria3);
		String sql3 = sqlBuilder3.selectAll().from().where().build();
		this.oneSelectValidCheck(sql3, "oneTableSelectBuildWithWhere3", sqlBuilder3.getMapSqlParameterSource().getValues());
//		
		Criteria criteria4 = new DefaultCriteria();
		criteria4.add(new MultiValueCriterion<CriterionOperator, String>(
							user,
							"password", 
							CriterionOperator.IN, 
							"1111", "3333"));	
		criteria4.add(new MultiValueCriterion<CriterionOperator, Integer>(
							user,
							"point", 
							CriterionOperator.BETWEEN, 
							2, 3));
		
		criteria4.add(new MultiValueCriterion<CriterionOperator, String>(
							user,
							"name", 
							CriterionOperator.NOT_IN, "강동원", "박희희"));
		SqlBuilder sqlBuilder4 = new SqlBuilder(user, criteria4);
		String sql4 = sqlBuilder4.selectAll().from().where().build();
		this.oneSelectValidCheck(sql4, "oneTableSelectBuildWithWhere4", sqlBuilder4.getMapSqlParameterSource().getValues());
		
	}
	
	@Ignore
	@Test
	public void oneTableSelectBuildWithOrder() throws Exception {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new DefaultOrder("name", OrderType.ASC));
		criteria.add(new DefaultOrder("seq", OrderType.DESC));
		
		SqlBuilder sqlBuilder = new SqlBuilder(user, criteria);
		String sql = sqlBuilder.selectAll().from().order().build();
		this.oneSelectValidCheck(sql, "oneTableSelectBuildWithOrder");
		
		criteria.add(new SingleValueCriterion<CriterionOperator, String>(
								user,
								"name", 
								CriterionOperator.EQ,
								"박희희")
					);
		
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>(
								user,
								"point",
								CriterionOperator.GTE,
								2)
					);
		
		SqlBuilder sqlBuilder2 = new SqlBuilder(user, criteria);
		String sql2 = sqlBuilder2.selectAll().from().where().order().build();
		this.oneSelectValidCheck(sql2, "oneTableSelectBuildWithOrder2", sqlBuilder2.getMapSqlParameterSource().getValues());
		
	}
	
//	@Ignore
	@Test
	public void twoTableSelectBuild() throws Exception {
		
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new JoinCriterion<CriterionOperator>(user, "seq", userPoint, "userpointseq"));
		criteria.add(new JoinCriterion<CriterionOperator>(user, "point", userPoint, "point"));
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>(user, "seq", CriterionOperator.GTE, 0));
		criteria.add(new MultiValueCriterion<CriterionOperator, Integer>(user, "point",	CriterionOperator.BETWEEN, 0, 100));
		criteria.add(new DefaultOrder(user, "point", OrderType.DESC));
		criteria.add(new DefaultOrder(userPoint, "point", OrderType.ASC));
		
		SqlBuilder sqlBuilder = new SqlBuilder(
									user,
									criteria);
	
		String sql = sqlBuilder.selectAll().from().where().order().build();
		this.oneSelectValidCheck(sql, "twoTableSelectBuild", sqlBuilder.getMapSqlParameterSource().getValues());
		
	}
	
	
//	@Ignore
//	@Test
//	public void userSqlBuild() throws Exception {
//		
//		String userId = "want813";
//		
//		SqlBuilder sqlBuilder = new SqlBuilder(userTableStrategyAware);
//		
//		String sql = sqlBuilder.selectAll().from().build();
//		this.oneSelectValidCheck(sql, "userSqlBuild");
//		
//		Criteria criteria = new DefaultCriteria();
//		criteria.add(new SingleValueCriterion(JdbcUserRepository.ID, CriterionOperator.EQ, userId));
//		criteria.add(new DefaultOrder(JdbcUserRepository.SEQ, OrderType.DESC));
//		
//		SqlBuilder sqlBuilder2 = new SqlBuilder(userTableStrategyAware, criteria);
//		
//		String sql2 = sqlBuilder2.selectAll().from().where().order().build();
//		this.oneSelectValidCheck(sql2, "userSqlBuild2", sqlBuilder2.getMapSqlParameterSource().getValues());
//		
//	}
	
	@Test
	public void userPointTest() throws Exception {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>(userPoint, "userseq", CriterionOperator.EQ, 1));
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>(userPoint, "point", CriterionOperator.GT, 0));
		
		SqlBuilder sqlBuilder = new SqlBuilder(userPoint, criteria);
		
		String sql = sqlBuilder.selectAll().from().where().order().build();
		this.oneSelectValidCheck(sql, "userPointTest", sqlBuilder.getMapSqlParameterSource().getValues());
		
	}
	
	
	/**
	 * 단일 테이블의 조회 쿼리일경우 위치에 대한 간단한 검증
	 * @param sql
	 */
	private void oneSelectValidCheck(String sql, String name){
		this.oneSelectValidCheck(sql, name, null);
	}
	
	private void oneSelectValidCheck(String sql, String name, Map map){
		
		System.out.println(name + " : " + sql);
		if(map != null){
			System.out.println("msps values" + map);
		}
		
		assertNotNull(sql);
		
		String upperCaseSql = sql.toUpperCase();
		int selectIndex = upperCaseSql.indexOf("SELECT");
		int fromIndex = upperCaseSql.indexOf("FROM");
		int whereIndex = upperCaseSql.indexOf("WHERE");
		
		assertTrue("select", selectIndex > -1);
		assertTrue("from", fromIndex > -1);
		assertTrue("from select", fromIndex > selectIndex);

		// where가 없는 쿼리일 경우 체크하지 않음
		if(whereIndex > -1){
			assertTrue("from where", whereIndex > fromIndex);
		}
	}
}
