package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import java.util.Map;

import org.apache.ibatis.ognl.MapElementsAccessor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.jdbc.criteria.CriterionOperator;
import developerhaus.repository.jdbc.criteria.DefaultCriteria;
import developerhaus.repository.jdbc.criteria.DefaultOrder;
import developerhaus.repository.jdbc.criteria.JoinCriterion;
import developerhaus.repository.jdbc.criteria.MultiValueCriterion;
import developerhaus.repository.jdbc.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.exception.SqlBuilderException;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;


public class SqlBuilderTest {
	
	private TableStrategyAware studentTableStrategyAware;
	private TableStrategyAware userTableStrategyAware;
	
	@Before
	public void setUp(){
		studentTableStrategyAware = new TableStrategyAware() {
			
			@Override
			public TableStrategy getTableStrategy() {
				return new DefaultTableStrategy("STUDENT", "stu")
				.setAllColumn("stu.SNO", "stu.SNAME", "stu.YEAR", "stu.DEPT");
			}
		};
		
		userTableStrategyAware = new TableStrategyAware() {
			
			@Override
			public TableStrategy getTableStrategy() {
				return new DefaultTableStrategy(JdbcUserRepository.TABLE_NAME, JdbcUserRepository.ALIAS)
					.setAllColumn(JdbcUserRepository.SEQEUNCE, JdbcUserRepository.ID, JdbcUserRepository.NAME, JdbcUserRepository.PASSWORD, JdbcUserRepository.POINT);
			}
		};
		
	}
	
	@Ignore
	@Test
	public void simpleSqlBuild() throws Exception {

		TableStrategyAware tsa = new TableStrategyAware() {
			
			@Override
			public TableStrategy getTableStrategy() {
				// TODO Auto-generated method stub
				return  new DefaultTableStrategy("test", "t");
			}
		};
		
		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategyAware);
		String sql = sqlBuilder.select("t.SNO, t.SNAME, t.YEAR, t.DEP").from().build();
		this.oneSelectValidCheck(sql, "simpleSqlBuild1");
		
		
		SqlBuilder sqlBuilder2 = new SqlBuilder(studentTableStrategyAware);
		String sql2 = sqlBuilder2.select(" * ").from().build();
		this.oneSelectValidCheck(sql2, "simpleSqlBuild2");
		
		SqlBuilder sqlBuilder3 = new SqlBuilder(studentTableStrategyAware);
		String sql3 = sqlBuilder3.selectAll().from().build();
		this.oneSelectValidCheck(sql3, "simpleSqlBuild3");
	}
	
	@Ignore
	@Test
	public void oneTableSelectBuild() throws Exception {
		
		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategyAware);

		String sql = sqlBuilder.selectAll().from().build();
		this.oneSelectValidCheck(sql, "oneTableSelectBuild");
	}
	
	@SuppressWarnings("unchecked")
	@Ignore
	@Test
	public void oneTableSelectBuildWithWhere() throws Exception {
		
//		SELECT stu.SNO, stu.SNAME, stu.YEAR, stu.DEPT 
//			FROM STUDENT stu
//		  WHERE stu.DEPT = '컴퓨터공학과'
//		    and stu.YEAR > 2
		
		Criteria criteria = new DefaultCriteria();
		criteria.add( new SingleValueCriterion(
								JdbcStudentRepository.DEPARTMENT, 
								CriterionOperator.EQ, 
								"컴퓨터공학과") );
		criteria.add(new SingleValueCriterion(JdbcStudentRepository.YEAR, CriterionOperator.GT, 2));
		
		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategyAware, criteria);
		
		String sql = sqlBuilder.selectAll().from().where().build();
		this.oneSelectValidCheck(sql, "oneTableSelectBuildWithWhere", sqlBuilder.getMapSqlParameterSource().getValues());
		
		Criteria criteria2 = new DefaultCriteria();
		criteria2.add(new SingleValueCriterion(
								JdbcStudentRepository.STUDENT_NUMBER, 
								CriterionOperator.EQ, 
								"60022416"));
		SqlBuilder sqlBuilder2 = new SqlBuilder(studentTableStrategyAware, criteria2);
		String sql2 = sqlBuilder2.selectAll().from().where().build();
		this.oneSelectValidCheck(sql2, "oneTableSelectBuildWithWhere2", sqlBuilder2.getMapSqlParameterSource().getValues());
		
		
		Criteria criteria3 = new DefaultCriteria();
		criteria3.add(new SingleValueCriterion(
								JdbcStudentRepository.DEPARTMENT, 
								CriterionOperator.LIKE, 
								"공학"));
		criteria3.add(new SingleValueCriterion(
								JdbcStudentRepository.DEPARTMENT, 
								CriterionOperator.LIKE_LEFT, 
								"컴퓨터"));
		criteria3.add(new SingleValueCriterion(
								JdbcStudentRepository.DEPARTMENT, 
								CriterionOperator.LIKE_RIGHT, 
								"공학과"));
		SqlBuilder sqlBuilder3 = new SqlBuilder(studentTableStrategyAware, criteria3);
		String sql3 = sqlBuilder3.selectAll().from().where().build();
		this.oneSelectValidCheck(sql3, "oneTableSelectBuildWithWhere3", sqlBuilder3.getMapSqlParameterSource().getValues());
		
		Criteria criteria4 = new DefaultCriteria();
		criteria4.add(new MultiValueCriterion(
							JdbcStudentRepository.DEPARTMENT, 
							CriterionOperator.IN, 
							"컴퓨터공학과", "전자공학과", "산업시스템공학과"));	
		criteria4.add(new MultiValueCriterion(
							JdbcStudentRepository.YEAR, 
							CriterionOperator.BETWEEN, 
							2, 3));
		
		criteria4.add(new MultiValueCriterion(
							JdbcStudentRepository.STUDENT_NUMBER, 
							CriterionOperator.NOT_IN, "123", "456"));
		SqlBuilder sqlBuilder4 = new SqlBuilder(studentTableStrategyAware, criteria4);
		String sql4 = sqlBuilder4.selectAll().from().where().build();
		this.oneSelectValidCheck(sql4, "oneTableSelectBuildWithWhere4", sqlBuilder4.getMapSqlParameterSource().getValues());
		
		
	}
	
	@Ignore
	@Test
	public void oneTableSelectBuildWithOrder() throws Exception {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new DefaultOrder(JdbcStudentRepository.DEPARTMENT, OrderType.ASC));
		criteria.add(new DefaultOrder(JdbcStudentRepository.YEAR, OrderType.DESC));
		
		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategyAware, criteria);
		String sql = sqlBuilder.selectAll().from().order().build();
		this.oneSelectValidCheck(sql, "oneTableSelectBuildWithOrder");
		
		criteria.add(new SingleValueCriterion(
								JdbcStudentRepository.STUDENT_NUMBER, 
								CriterionOperator.EQ,
								"60022416")
					);
		
		criteria.add(new SingleValueCriterion(
								JdbcStudentRepository.DEPARTMENT,
								CriterionOperator.LIKE,
								"공학")
					);
		
		SqlBuilder sqlBuilder2 = new SqlBuilder(studentTableStrategyAware, criteria);
		String sql2 = sqlBuilder2.selectAll().from().where().order().build();
		this.oneSelectValidCheck(sql2, "oneTableSelectBuildWithOrder2");
		
	}
	
	@Ignore
	@Test
	public void twoTableSelectBuild() throws Exception {
		
		JdbcStudentRepository studentRepository = new JdbcStudentRepository();
		JdbcUniversityRepository universityRepository = new JdbcUniversityRepository();
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new JoinCriterion(
						JdbcStudentRepository.UNIVERSITY_ID, 
						JdbcUniversityRepository.UNIVERSITY_ID));
		
		criteria.add(new SingleValueCriterion(JdbcStudentRepository.YEAR, CriterionOperator.EQ, 1));
		
		SqlBuilder sqlBuilder = new SqlBuilder(
									studentRepository,
									criteria,
									universityRepository.getTableStrategy());
	
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
	
	
	@Ignore
	@Test
	public void userSqlBuild() throws Exception {
		
		String userId = "want813";
		
		SqlBuilder sqlBuilder = new SqlBuilder(userTableStrategyAware);
		
		String sql = sqlBuilder.selectAll().from().build();
		this.oneSelectValidCheck(sql, "userSqlBuild");
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion(JdbcUserRepository.ID, CriterionOperator.EQ, userId));
		criteria.add(new DefaultOrder(JdbcUserRepository.SEQEUNCE, OrderType.DESC));
		
		SqlBuilder sqlBuilder2 = new SqlBuilder(userTableStrategyAware, criteria);
		
		String sql2 = sqlBuilder2.selectAll().from().where().order().build();
		this.oneSelectValidCheck(sql2, "userSqlBuild2", sqlBuilder2.getMapSqlParameterSource().getValues());
		
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
