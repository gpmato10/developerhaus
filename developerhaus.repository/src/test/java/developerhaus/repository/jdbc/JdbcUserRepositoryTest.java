package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.activation.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import developerhaus.domain.User;
import developerhaus.repository.UserRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.SingleValueCriterion;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="applicationContext-test.xml")
public class JdbcUserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void getUser() throws Exception {
		Integer id = 3;
		User user = userRepository.get(id);
		
		System.out.println(user);
	}
	
	@Ignore
	@Test
	public void getUserPoint() throws Exception {
		
		
		
	}
	
	
	@Ignore
	@Test
	public void getList() throws Exception {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion<String, CriterionOperator, String>("name", CriterionOperator.LIKE, "희"));
	}
	
	@Ignore
	@Test
	public void filedTest() throws Exception {
		
//		Criteria criteria = new DefaultCriteria();
//		criteria.add(new SingleValueCriterion("seq", CriterionOperator.EQ, id));
//		
//		SqlBuilder sqlBuilder = new SqlBuilder(this, criteria);
//		String sql = sqlBuilder.selectAll().from().where().build();

		JdbcUserRepository r = new JdbcUserRepository();
		Class c = JdbcUserRepository.class;
		
		Field f = c.getField("name".toUpperCase());
		System.out.println(f.get(r));
		
	}
	

}
