package developerhaus.repository.ibatis;

import developerhaus.domain.User;

public class IbatisUserRepository extends GenericIbatisSupportRepository<User, Integer> {

	public User get(Integer id) {
		return super.get(id);
	}
}
