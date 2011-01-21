/**========================================================
*파일명         : HibernateUserRepository.java
*파일용도       : 
*
*마지막변경일자 : 2011. 1. 20.
*마지막변경자   : want
=========================================================*/
package developerhaus.repository.hibernate;

import java.util.List;

import developerhaus.domain.User;
import developerhaus.repository.api.criteria.Criteria;

/**
 * @author want
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

}
