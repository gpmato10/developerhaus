
package developerhaus.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.DefaultOrder;
import developerhaus.repository.criteria.SingleValueCriterion;

/**
 * @author 박 성희
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/developerhaus/repository/jdbc/applicationContext-test.xml")
//@ContextConfiguration(locations="/developerhaus/repository/hibernate/applicationContext-hibernate-test.xml")
public class RepositoryTest {
	@Autowired
	UserRepository repository;
	
	/**
	 * repository.get() 테스트
	 * @throws Exception
	 */
	@Test
	public void get() throws Exception {
		int seq = 1;
		User user = repository.get(seq);
	
		assertEquals(user.getId(), "gosu");
		assertEquals(user.getName(), "고수");
	}
	
	/**
	 * repository.list() 테스트
	 * @throws Exception
	 */
	@Test
	public void list() throws Exception {
		Criterion<String, CriterionOperator, String> criterion = new SingleValueCriterion<CriterionOperator, String>(new User(), "id", CriterionOperator.LIKE_LEFT, "mudo");		
		
		Order order = new DefaultOrder("name", OrderType.DESC);
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(criterion);
		criteria.add(order);
		
		List<User> list = repository.list(criteria);
		assertEquals(list.size(), 7);
		assertEquals(list.get(0).getName(), "하하");
	}
	
	/**
	 * user.getUserPointList() 테스트
	 * @throws Exception
	 */
	@Test
	public void getPointList() throws Exception {
		int seq = 1;
		User user = repository.get(seq);
		user.setUserRepository(repository);
		
		List<UserPoint> pointList = user.getUserPointList();
		assertEquals(pointList.size(), 3);
	}
	
}
