package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.jdbc.criteria.CriterionOperator;
import developerhaus.repository.jdbc.criteria.DefaultCriteria;
import developerhaus.repository.jdbc.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.exception.SqlBuilderException;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;


public class SqlBuilderTest {
	
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
		
		TableStrategy tableStrategy 
			= new DefaultTableStrategy("STUDENT", "stu")
				.setAllColumn("stu.SNO", "stu.SNAME", "stu.YEAR", "stu.DEPT");
		SqlBuilder sqlBuilder = new SqlBuilder(tableStrategy);

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
		
		TableStrategy tableStrategy 
			= new DefaultTableStrategy("STUDENT", "stu")
				.setAllColumn("stu.SNO", "stu.SNAME", "stu.YEAR", "stu.DEPT");
		
		Criteria criteria = new DefaultCriteria();
		criteria.add( new SingleValueCriterion(
								JdbcStudentRepository.DEPARTMENT, 
								"컴퓨터공학과", 
								CriterionOperator.EQ) );
		criteria.add(new SingleValueCriterion(JdbcStudentRepository.YEAR, 2, CriterionOperator.GT));
		
		SqlBuilder sqlBuilder = new SqlBuilder(tableStrategy, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		this.singleTableValidCheck(sql, "oneTableSelectBuildWithWhere");
		
		
		Criteria criteria2 = new DefaultCriteria();
		criteria2.add(new SingleValueCriterion(
								JdbcStudentRepository.STUDENT_NUMBER, 
								"60022416", 
								CriterionOperator.EQ));
		SqlBuilder sqlBuilder2 = new SqlBuilder(tableStrategy, criteria2);
		String sql2 = sqlBuilder2.selectAll().from().where().build();
		this.singleTableValidCheck(sql2, "oneTableSelectBuildWithWhere2");
	}
	
//	@Ignore
	@Test
//	TODO : 대소문자를 구분하는지 체크, 실제이름이 아니라 Alias를 이용하여 실제 컬럼명과 매핑
	public void oneTableInsertBuild() throws Exception {
//		INSERT INTO STUDENT(SNO, SNAME, YEAR, DEPT) VALUES(:SNO, :SNAME, :YEAR, :DEPT)
		TableStrategy tableStrategy 
			= new DefaultTableStrategy("STUDENT", "stu")
				.setAllColumn("stu.SNO", "stu.SNAME", "stu.YEAR", "stu.DEPT");
		SqlBuilder sqlBuilder = new SqlBuilder(tableStrategy);
		
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
		
		System.out.println("name : " + sql);
		
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
