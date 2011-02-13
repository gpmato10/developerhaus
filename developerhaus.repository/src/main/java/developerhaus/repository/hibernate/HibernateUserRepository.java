package developerhaus.repository.hibernate;

import java.util.List;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.UserRepository;

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
	public List<UserPoint> getUserPointList() {
		// TODO Auto-generated method stub
		return null;
	}

}