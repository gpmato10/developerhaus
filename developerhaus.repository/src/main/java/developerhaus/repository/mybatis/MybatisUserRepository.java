package developerhaus.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.junit.runners.Parameterized.Parameters;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.UserRepository;
import developerhaus.repository.api.GenericRepository;
import developerhaus.repository.api.criteria.Criteria;

public class MybatisUserRepository extends GenericMybatisSupportRepository<User, Integer>
		implements UserRepository {
	
	SqlSessionTemplate sqlSessionTemplate;
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public interface UserMapper extends GenericRepository<User, Integer> {
		
		final String GET = "SELECT * FROM USERS WHERE seq = #{id}";
		@Override
		@Select (GET)
		public User get(@Param("id") Integer id);
		
	}
	
	private UserMapper getUserMapper() {
		return sqlSessionTemplate.getMapper(UserMapper.class);
	}
	
	@Override
	public User get(Integer id) {
		return getUserMapper().get(id);
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

	
	@Override
	public List<UserPoint> getUserPointList() {
		// TODO Auto-generated method stub
		return null;
	}

}
