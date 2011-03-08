package developerhaus.repository.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.UserRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.DefaultOrder;
import developerhaus.repository.criteria.JoinCriterion;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.hibernate.criteria.HibernateCriteriaUtils;

/**
 * 하이버네이트 User Repository
 * 
 * @author sunghee, Park
 * 
 */
public class HibernateUserRepository extends GenericHibernateSupportRepository<User, Integer>
		implements UserRepository {

	@Override
	public User get(Integer id) {
		return super.get(id);
	}

	@Override
	public List<User> list(Criteria criteria) {
		return super.list(criteria);
	}

	@Override
	public boolean update(User domain) {
		return super.update(domain);
	}

	@Override
	public List<UserPoint> getUserPointList(User user) {
		Criteria criteria = new DefaultCriteria();
		
		Criterion jcriterion = new JoinCriterion(new User(), "seq", new UserPoint(), "userSeq");
		Criterion<String, CriterionOperator, Integer> criterion = new SingleValueCriterion<CriterionOperator, Integer>(new User(), "seq", CriterionOperator.EQ, user.getSeq());
		Order order = new DefaultOrder("regDt", OrderType.DESC);

		criteria.add(jcriterion);
		criteria.add(criterion);
		criteria.add(order);
		
		DetachedCriteria hcriteria = HibernateCriteriaUtils.getHibernateCriteria(UserPoint.class, criteria);
		return hibernateTemplate.findByCriteria(hcriteria);
	}
	
	@Override
	public List<UserPoint> getUserPointListById(String id) {
		User user = new User();
		user.setId(id);
		return getUserPointList(user);
	}


}
