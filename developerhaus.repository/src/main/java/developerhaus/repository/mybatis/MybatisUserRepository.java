package developerhaus.repository.mybatis;

import java.util.List;

import developerhaus.repository.api.criteria.Criteria;
import developerhaus.user.User;

public class MybatisUserRepository extends GenericMybatisSupportRepository<User, Integer> 
		implements UserRepository {

	@Override
	public User get(Integer id) {
		return super.get(id);
	}

	@Override
	public List<User> list(Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(User domain) {
		// TODO Auto-generated method stub
		return false;
	}

}
