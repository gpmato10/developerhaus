package developerhaus.repository.hibernate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import developerhaus.domain.User;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;

public class HibernateRepositoryTest {	
	HibernateRepository<User, Integer> repository;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		ApplicationContext context = new GenericXmlApplicationContext("/developerhaus/repository/hibernate/applicationContext-hibernate-test.xml");
		repository = (HibernateRepository<User, Integer>) context.getBean("hibernateRepository");
		repository.setTargetClass(User.class);
	}
	
	@Test
	public void getCount() throws Exception {
		Criterion<String, String, CriterionOperator> criterion = new DefaultCriterion<String, String, CriterionOperator>("name", "박성희", CriterionOperator.EQ);
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(criterion);
		
		assertEquals(repository.list(criteria).size(), 2);
	}

	@Test
	public void updateAndGet() throws Exception {
		User user = new User();
		user.setPassword("5555");
		user.setSeq(2);
		user.setName("박성희");
		user.setId("want815");
		
		repository.update(user);
		
		int id = 2;
		User user2 = repository.get(id);
	
		assertEquals(user.getId(), user2.getId());
		assertEquals(user.getPassword(), user2.getPassword());
		
		
	}
}