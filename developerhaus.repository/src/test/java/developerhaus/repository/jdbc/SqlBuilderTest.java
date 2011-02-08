package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.jdbc.criteria.CriterionOperator;
import developerhaus.repository.jdbc.criteria.DefaultCriteria;
import developerhaus.repository.jdbc.criteria.DefaultOrder;
import developerhaus.repository.jdbc.criteria.MultiValueCriterion;
import developerhaus.repository.jdbc.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.exception.SqlBuilderException;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;


public class SqlBuilderTest {
	
	private TableStrategy studentTableStrategy;
	
	@Before
	public void setUp(){
		studentTableStrategy = new DefaultTableStrategy("STUDENT", "stu")
								.setAllColumn("stu.SNO", "stu.SNAME", "stu.YEAR", "stu.DEPT");
	}
	
//	@Ignore
	@Test
	public void simpleSqlBuild() throws Exception {

		TableStrategy tableStrategy = new DefaultTableStrategy("test", "t");
		
		SqlBuilder sqlBuilder = new SqlBuilder(tableStrategy);
		String sql = sqlBuilder.select("t.SNO, t.SNAME, t.YEAR, t.DEP").from().build();
		this.singleTableValidCheck(sql, "simpleSqlBuild1");
		
		
		SqlBuilder sqlBuilder2 = new SqlBuilder(tableStrategy);
		String sql2 = sqlBuilder2.select(" * ").from().build();
		this.singleTableValidCheck(sql2, "simpleSqlBuild2");
		
		SqlBuilder sqlBuilder3 = new SqlBuilder(tableStrategy);
		String sql3 = sqlBuilder3.selectAll().from().build();
		this.singleTableValidCheck(sql3, "simpleSqlBuild3");
	}
	
	@Test
	public void oneTableSelectBuild() throws Exception {
		
		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategy);

		String sql = sqlBuilder.selectAll().from().build();
		this.singleTableValidCheck(sql, "oneTableSelectBuild");
	}
	
	@SuppressWarnings("unchecked")
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
		
		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategy, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		this.singleTableValidCheck(sql, "oneTableSelectBuildWithWhere");
		
		
		Criteria criteria2 = new DefaultCriteria();
		criteria2.add(new SingleValueCriterion(
								JdbcStudentRepository.STUDENT_NUMBER, 
								CriterionOperator.EQ, 
								"60022416"));
		SqlBuilder sqlBuilder2 = new SqlBuilder(studentTableStrategy, criteria2);
		String sql2 = sqlBuilder2.selectAll().from().where().build();
		this.singleTableValidCheck(sql2, "oneTableSelectBuildWithWhere2");
		
		
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
								CriterionOperator.LIKE_LEFT, 
								"공학과"));
		SqlBuilder sqlBuilder3 = new SqlBuilder(studentTableStrategy, criteria3);
		String sql3 = sqlBuilder3.selectAll().from().where().build();
		this.singleTableValidCheck(sql3, "oneTableSelectBuildWithWhere3");
		
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
		SqlBuilder sqlBuilder4 = new SqlBuilder(studentTableStrategy, criteria4);
		String sql4 = sqlBuilder4.selectAll().from().where().build();
		this.singleTableValidCheck(sql4, "oneTableSelectBuildWithWhere4");
		
		
	}
	
	@Test
	public void oneTableSelectBuildWithOrder() throws Exception {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new DefaultOrder(JdbcStudentRepository.DEPARTMENT, OrderType.ASC));
		criteria.add(new DefaultOrder(JdbcStudentRepository.YEAR, OrderType.DESC));
		
		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategy, criteria);
		String sql = sqlBuilder.selectAll().from().order().build();
		this.singleTableValidCheck(sql, "oneTableSelectBuildWithOrder");
		
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
		
		SqlBuilder sqlBuilder2 = new SqlBuilder(studentTableStrategy, criteria);
		String sql2 = sqlBuilder2.selectAll().from().where().order().build();
		this.singleTableValidCheck(sql2, "oneTableSelectBuildWithOrder2");
		
	}
	
	
	
	
	@Ignore
	@Test
//	TODO : 대소문자를 구분하는지 체크, 실제이름이 아니라 Alias를 이용하여 실제 컬럼명과 매핑
	public void oneTableInsertBuild() throws Exception {
//		INSERT INTO STUDENT(SNO, SNAME, YEAR, DEPT) VALUES(:SNO, :SNAME, :YEAR, :DEPT)
		SqlBuilder sqlBuilder = new SqlBuilder(studentTableStrategy);
		
		Student student = new Student();
		
		try {
			String sql = sqlBuilder.insert(student).build();
			assertTrue(true);
		} catch(SqlBuilderException se){
			fail();
		}
	}
	
	
	/**
	 * 단일 테이블의 조회 쿼리일경우 위치에 대한 간단한 검증
	 * @param sql
	 */
	private void singleTableValidCheck(String sql, String name){
		
		System.out.println(name + " : " + sql);
		
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
