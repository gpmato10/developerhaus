package developerhaus.repository.jdbc2;


import java.util.List;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.UserRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.jdbc3.criteria.CriterionOperator;
import developerhaus.repository.jdbc3.criteria.GenericCriteria;
import developerhaus.repository.jdbc3.criteria.GenericCriterion;

public class JdbcUserRepository extends JdbcRepositoryDao<User> implements UserRepository{
	

	@Override
	public User get(Integer id) {
		GenericCriteria criteria = new GenericCriteria();
		criteria.add(new GenericCriterion("id",id.toString(),CriterionOperator.EQ));
		User user = new User();
		user.setId(id.toString());
		return (User) super.get(user, criteria);
	}

	@Override
	public List<User> list(Criteria criteria) {
		User user = new User();
		return list(user,(GenericCriteria)criteria);
	}

	@Override
	public boolean update(User user) {
		GenericCriteria criteria = new GenericCriteria();
		criteria.add(new GenericCriterion("id",user.getId(),CriterionOperator.EQ));
		criteria.add(new GenericCriterion("name",user.getName(),CriterionOperator.NONE));
		return super.update(user,criteria);
	}

	@Override
	public List<UserPoint> getUserPointList() {
		return null;
	}

}
