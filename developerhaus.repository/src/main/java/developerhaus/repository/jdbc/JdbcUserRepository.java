package developerhaus.repository.jdbc;

import static developerhaus.repository.jdbc.RepositoryUtils.addAliasToColumn;

import java.util.List;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.UserRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

public class JdbcUserRepository implements UserRepository, TableStrategyAware{
	
	public final static String TABLE_NAME = "USERS";
	public final static String ALIAS = "user";
	
	// DB 의존성을 제거하기 위해 DB컬럼명 변화에 상관없이 대표되는 컬럼명 정의
	// TODO : 쿼리결과 도메인 속성명과 매핑하기 위한 정책 수립(Spring JDBC 붙인 후 다시 생각)
	public final static String SEQEUNCE = addAliasToColumn(ALIAS, "seq");  		// 시퀀스
	public final static String ID = addAliasToColumn(ALIAS, "id");  				// 아이디 
	public final static String NAME = addAliasToColumn(ALIAS,	"name");  			// 이름
	public final static String PASSWORD = addAliasToColumn(ALIAS, "password");	// 비밀번호 
	public final static String POINT = addAliasToColumn(ALIAS, "point");  			// 포인트

	@Override
	public User get(Integer id) {
		return null;
	}

	@Override
	public List<User> list(Criteria criteria) {
		return null;
	}

	@Override
	public boolean update(User domain) {
		return false;
	}

	@Override
	public List<UserPoint> getUserPointList() {
		return null;
	}

	@Override
	public TableStrategy getTableStrategy() {
		
		return new DefaultTableStrategy(TABLE_NAME, ALIAS)
			.setAllColumn(SEQEUNCE, ID, NAME, PASSWORD, POINT);
	}
}
