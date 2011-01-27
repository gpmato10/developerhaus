package developerhaus.repository.hibernate;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.hibernate.criteria.CriterionOperator;
import developerhaus.repository.hibernate.criteria.DefaultCriteria;
import developerhaus.repository.hibernate.criteria.DefaultCriterion;
import developerhaus.repository.hibernate.criteria.DefaultOrder;
import developerhaus.user.User;

public class HibernateUserRepositoryTest {	
	HibernateUserRepository repository;
	
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		ApplicationContext context = new GenericXmlApplicationContext("/developerhaus/repository/hibernate/applicationContext-hibernate-test.xml");
		repository = (HibernateUserRepository) context.getBean("hibernateUserRepository");
	}
	
	@Test
	public void paging() throws Exception {
		Criterion<String, String, CriterionOperator> criterion = new DefaultCriterion("name", "박", CriterionOperator.LIKE_LEFT);		
		Order order = new DefaultOrder("name", OrderType.DESC);
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(criterion);
		criteria.add(order);
		
		List<User> list = repository.page(criteria, 0, 5);
		System.out.println("list.size():"+list.size());
		assertEquals(list.size(), 5);
		
		int cnt = repository.count(criteria);
		System.out.println("cnt:"+cnt);
		
		assertEquals(cnt, 10);
		
		
	}
	
	@Test
	public void getCount() throws Exception {
		Criterion<String, String, CriterionOperator> criterion = new DefaultCriterion("name", "박", CriterionOperator.LIKE_LEFT);		
		Criterion<String, String, CriterionOperator> criterion2 = new DefaultCriterion("name", "희", CriterionOperator.LIKE_RIGHT);
		/*Criterion<String, String, CriterionOperator> criterion3 = new DefaultCriterion(CriterionOperator.OR, criterion, criterion2, criterion2);
		*/
		Order order = new DefaultOrder("name", OrderType.DESC);
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(criterion);
		//criteria.add(criterion2);
		criteria.add(order);
		
		List<User> list = repository.list(criteria);
		
		assertEquals(list.size(), 10);

		assertEquals(list.get(0).getName(), "박희희");
	}

	@Test
	public void updateAndGet() throws Exception {
		User user = new User();
		user.setSeq(2);
		user.setName("박성희");
		user.setId("want815");
		user.setPassword("5555");
		
		repository.update(user);
		
		int id = 2;
		User user2 = repository.get(id);
	
		assertUser(user, user2);
	}

	private void assertUser(User user, User user2) {
		assertEquals(user.getId(), user2.getId());
		assertEquals(user.getPassword(), user2.getPassword());
		assertEquals(user.getName(), user2.getName());
	}
}
