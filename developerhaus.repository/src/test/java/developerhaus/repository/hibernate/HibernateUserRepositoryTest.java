package developerhaus.repository.hibernate;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.DefaultOrder;
import developerhaus.repository.criteria.JoinCriterion;
import developerhaus.repository.criteria.SingleValueCriterion;

public class HibernateUserRepositoryTest {	
	HibernateUserRepository repository;
	
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		ApplicationContext context = new GenericXmlApplicationContext("/developerhaus/repository/hibernate/applicationContext-hibernate-test.xml");
		repository = (HibernateUserRepository) context.getBean("userRepository");
	}
	
	@Test
	@Ignore
	public void joinSite() throws Exception {
		// 회원 가입시 포인트를 100 준다.... 
	}
	
	@Test
	public void getUserPointList() throws Exception {
		Criterion jcriterion = new JoinCriterion(new User(), "seq", new UserPoint(), "userSeq");
		Criterion<String, CriterionOperator, String> criterion = new SingleValueCriterion<CriterionOperator, String>(new User(), "id", CriterionOperator.EQ, "want813");
		Order order = new DefaultOrder("regDt", OrderType.DESC);

		Criteria criteria = new DefaultCriteria();
		criteria.add(jcriterion);
		criteria.add(criterion);
		criteria.add(order);
		
		List<UserPoint> list = repository.getUserPointList(criteria);
		UserPoint userPoint = list.get(0);
		System.out.println("userPoint:"+userPoint);
		assertNotNull(list);
		assertEquals(list.size(), 3);
		System.out.println("list.size():"+list.size());
	}
	
	@Test
	@Ignore
	public void paging() throws Exception {
		Criterion<String, CriterionOperator, String> criterion = new SingleValueCriterion<CriterionOperator, String>("name", CriterionOperator.LIKE_LEFT, "박");		
		Order order = new DefaultOrder("name", OrderType.DESC);
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(criterion);
		criteria.add(order);
		
		List<User> list = repository.page(criteria, 0, 5);
		assertEquals(list.size(), 5);
		
		int cnt = repository.count(criteria);
		assertEquals(cnt, 10);
		
		
	}
	
	@Test
	@Ignore
	public void getCount() throws Exception {
		Criterion<String, CriterionOperator, String> criterion = new SingleValueCriterion<CriterionOperator, String>("name", CriterionOperator.LIKE_LEFT, "박");		
		Criterion<String, CriterionOperator, String> criterion2 = new SingleValueCriterion<CriterionOperator, String>("name", CriterionOperator.LIKE_RIGHT, "희");
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
	@Ignore
	public void updateAndGet() throws Exception {
		User user = new User();
		user.setSeq(1);
		user.setName("박성희");
		user.setId("want813");
		user.setPassword("5555");
		
		repository.update(user);
		
		int id = 1;
		User user2 = repository.get(id);
	
		assertUser(user, user2);
	}

	private void assertUser(User user, User user2) {
		assertEquals(user.getId(), user2.getId());
		assertEquals(user.getPassword(), user2.getPassword());
		assertEquals(user.getName(), user2.getName());
	}
}
