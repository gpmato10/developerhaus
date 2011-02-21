package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import developerhaus.domain.Student;
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
	private UserRowMapper userRowMapper;
	private UserPointRowMapper userPointRowMapper;
	
	@Before
	public void setUp(){
		
				
		studentTableStrategyAware = new TableStrategyAware() {
			
			@Override
			public TableStrategy getTableStrategy() {
				return new DefaultTableStrategy("STUDENT", "stu")
				.setAllColumn("sno", "sname", "year", "dept");
			}
		};
		
		userRowMapper = new UserRowMapper();
		userPointRowMapper = new UserPointRowMapper();
	}
	
//	@Ignore
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
//	@Ignore
	@Test
	public void oneTableSelectBuildWithWhere() throws Exception {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add( new SingleValueCriterion<CriterionOperator, String>(
								"password", 
								CriterionOperator.EQ, 
								"1111") );
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>("point", CriterionOperator.GT, 0));
		
		SqlBuilder sqlBuilder = new SqlBuilder(userRowMapper, criteria);
		
		String sql = sqlBuilder.selectAll().from().where().build();
		this.oneSelectValidCheck(sql, "oneTableSelectBuildWithWhere", sqlBuilder.getMapSqlParameterSource().getValues());
		
		Criteria criteria3 = new DefaultCriteria();
		criteria3.add(new SingleValueCriterion<CriterionOperator, String>(
								"name", 
								CriterionOperator.LIKE, 
								"희"));
		criteria3.add(new SingleValueCriterion<CriterionOperator, String>(
								"name", 
								CriterionOperator.LIKE_LEFT, 
								"성희"));
		criteria3.add(new SingleValueCriterion<CriterionOperator, String>(
								"name", 
								CriterionOperator.LIKE_RIGHT, 
								"박성"));
		
		SqlBuilder sqlBuilder3 = new SqlBuilder(userRowMapper, criteria3);
		String sql3 = sqlBuilder3.selectAll().from().where().build();
		this.oneSelectValidCheck(sql3, "oneTableSelectBuildWithWhere3", sqlBuilder3.getMapSqlParameterSource().getValues());
//		
		Criteria criteria4 = new DefaultCriteria();
		criteria4.add(new MultiValueCriterion<CriterionOperator, String>(
							"password", 
							CriterionOperator.IN, 
							"1111", "3333"));	
		criteria4.add(new MultiValueCriterion<CriterionOperator, Integer>(
							"point", 
							CriterionOperator.BETWEEN, 
							2, 3));
		
		criteria4.add(new MultiValueCriterion<CriterionOperator, String>(
							"name", 
							CriterionOperator.NOT_IN, "강동원", "박희희"));
		SqlBuilder sqlBuilder4 = new SqlBuilder(userRowMapper, criteria4);
		String sql4 = sqlBuilder4.selectAll().from().where().build();
		this.oneSelectValidCheck(sql4, "oneTableSelectBuildWithWhere4", sqlBuilder4.getMapSqlParameterSource().getValues());
		
	}
	
//	@Ignore
	@Test
	public void oneTableSelectBuildWithOrder() throws Exception {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new DefaultOrder("name", OrderType.ASC));
		criteria.add(new DefaultOrder("seq", OrderType.DESC));
		
		SqlBuilder sqlBuilder = new SqlBuilder(userRowMapper, criteria);
		String sql = sqlBuilder.selectAll().from().order().build();
		this.oneSelectValidCheck(sql, "oneTableSelectBuildWithOrder");
		
		criteria.add(new SingleValueCriterion<CriterionOperator, String>(
								"name", 
								CriterionOperator.EQ,
								"박희희")
					);
		
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>(
								"point",
								CriterionOperator.GTE,
								2)
					);
		
		SqlBuilder sqlBuilder2 = new SqlBuilder(userRowMapper, criteria);
		String sql2 = sqlBuilder2.selectAll().from().where().order().build();
		this.oneSelectValidCheck(sql2, "oneTableSelectBuildWithOrder2", sqlBuilder2.getMapSqlParameterSource().getValues());
		
	}
	
//	@Ignore
	@Test
	public void twoTableSelectBuild() throws Exception {
		
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new JoinCriterion(userRowMapper, "seq", userPointRowMapper, "userpointseq"));
		
//		criteria.add(new SingleValueCriterion<CriterionOperator, String>(userRowMapper, "password", CriterionOperator.EQ, "1111"));
		
		SqlBuilder sqlBuilder = new SqlBuilder(
									userRowMapper,
									criteria,
									userPointRowMapper);
	
		String sql = sqlBuilder.selectAll().from().where().build();
		this.oneSelectValidCheck(sql, "twoTableSelectBuild", sqlBuilder.getMapSqlParameterSource().getValues());
		
	}
	
	
	@Ignore
	@Test
//	TODO : 대소문자를 구분하는지 체크, 실제이름이 아니라 Alias를 이용하여 실제 컬럼명과 매핑
	public void updateBuild() throws Exception {
//		INSERT INTO STUDENT(SNO, SNAME, YEAR, DEPT) VALUES(:SNO, :SNAME, :YEAR, :DEPT)
		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategyAware);
		
		Student student = new Student();
		
		try {
			String sql = sqlBuilder.insert(student).build();
			System.out.println(sql);
			assertTrue(true);
		} catch(SqlBuilderException se){
			fail();
		}
		
//		UPDATE STUDENT set sname = '11' where sno = '1111';
//		update users u set u.point = 12 where u.seq = 14;
//		DELETE STUDENT where sno = '1111';
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion(JdbcStudentRepository.STUDENT_NAME, CriterionOperator.EQ, 11));
		SqlBuilder sqlBuilder2 = new SqlBuilder(studentTableStrategyAware);
		
//		String deleteSql = sqlBuilder2.update().build();
	}
	
//	
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
