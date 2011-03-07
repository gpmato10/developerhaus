package developerhaus.repository.mybatis;

import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.Case;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
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
import developerhaus.repository.mapper.UserRowMapper;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
//import static org.mybatis.jdbc.SqlBuilder.*;

public class MybatisUserRepository extends GenericMybatisSupportRepository<User, Integer>
		implements UserRepository {
	
	SqlSessionTemplate sqlSessionTemplate;
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public interface UserMapper {
		@SelectProvider(method="getQuery", type = MybatisUserRepository.class)
		public User get(Integer id);
		
		final String LIST = "SELECT * FROM USERS";
		@Select (LIST)
		public List<User> list(Criteria criteria);
	}
	
	private UserMapper getUserMapper() {
		return sqlSessionTemplate.getMapper(UserMapper.class);
	}
	
	@Override
	public User get(Integer id) {
		return getUserMapper().get(id);
	}
	
	public String getQuery(Integer id) {
		BEGIN();
		SELECT("*");
		FROM(User.TABLE_NAME);
		WHERE(User.SEQ + " = #{id}");
		return SQL();
	}
	
	@Override
	public List<User> list(Criteria criteria) {
		// TODO Auto-generated method stub
		return getUserMapper().list(criteria);
	}

	@Override
	public boolean update(User domain) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public List<UserPoint> getUserPointList(Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

}

