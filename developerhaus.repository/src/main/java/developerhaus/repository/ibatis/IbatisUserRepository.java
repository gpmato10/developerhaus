package developerhaus.repository.ibatis;

import java.util.List;

import developerhaus.domain.User;
import developerhaus.repository.api.criteria.Criteria;

public class IbatisUserRepository extends GenericIbatisSupportRepository<User, Integer> {

	@Override
	public User get(Integer id) {
		return super.get(id);
	}
	
	@Override
	public List<User> list(Criteria criteria) {
		return super.list(criteria);
	}
	
	public boolean update(User domain) {
		return super.update(domain);
	}
}
