package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import javax.activation.DataSource;

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
import developerhaus.repository.jdbc.criteria.CriterionOperator;
import developerhaus.repository.jdbc.criteria.DefaultCriteria;
import developerhaus.repository.jdbc.criteria.SingleValueCriterion;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="applicationContext-test.xml")
public class JdbcUserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void getCount() throws Exception {
		
		Integer id = 3;
		User user = userRepository.get(id);
		
		System.out.println(user);
	}

}
