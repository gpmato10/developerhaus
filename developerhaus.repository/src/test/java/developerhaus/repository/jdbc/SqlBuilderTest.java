package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import org.junit.Test;

import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;


public class SqlBuilderTest {
	
	@Test
	public void simpleSqlBuild() throws Exception {

		TableStrategy tableStrategy = new DefaultTableStrategy("test");
		SqlBuilder sqlBuilder = new SqlBuilder(tableStrategy);
		
		String sql = sqlBuilder.select(" * ").from().where("").build();
		this.singleTableValidCheck(sql);
		
		SqlBuilder sqlBuilder2 = new SqlBuilder(tableStrategy);
		
		String sql2 = sqlBuilder2.selectAll().from().where("").build();
		this.singleTableValidCheck(sql2);
	}
	
	@Test
	public void oneTableSelectBuild() throws Exception {
		
		TableStrategy tableStrategy 
			= new DefaultTableStrategy("STUDENT")
				.setAllColumn("SNO", "SNAME", "YEAR", "DEPT");
		SqlBuilder sqlBuilder = new SqlBuilder(tableStrategy);

		String sql = sqlBuilder.selectAll().from().build();
		this.singleTableValidCheck(sql);
		
	}
	
	/**
	 * 단일 테이블의 조회 쿼리일경우 위치에 대한 간단한 검증
	 * @param sql
	 */
	private void singleTableValidCheck(String sql){
		
		System.out.println("query : " + sql);
		
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
