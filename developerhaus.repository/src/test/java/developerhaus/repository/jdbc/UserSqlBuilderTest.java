package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import developerhaus.domain.User;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;
import developerhaus.repository.mapper.UserRowMapper;

public class UserSqlBuilderTest {
	

	@Test
	public void simpleBuild() throws Exception {
		
		TableStrategyAware userTsa = new User();
		Integer id = 3;
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion<CriterionOperator, Integer>("seq", CriterionOperator.EQ, id));
		
		SqlBuilder sqlBuilder = new SqlBuilder(userTsa, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		
		this.oneSelectValidCheck(sql, "simpleBuild", sqlBuilder.getMapSqlParameterSource().getValues());
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
