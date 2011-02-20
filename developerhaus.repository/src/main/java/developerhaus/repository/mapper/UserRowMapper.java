package developerhaus.repository.mapper;

import static developerhaus.repository.jdbc.RepositoryUtils.getColumnName;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import developerhaus.domain.User;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

public class UserRowMapper implements RowMapper<User>,TableStrategyAware{
	
	public final static String TABLE_NAME = "USERS";
	public final static String ALIAS = "user";
	// DB 의존성을 제거하기 위해 DB컬럼명 변화에 상관없이 대표되는 컬럼명 정의
	// TODO : 쿼리결과 도메인 속성명과 매핑하기 위한 정책 수립(Spring JDBC 붙인 후 다시 생각)
	public final static String SEQ = "seq";  		// 시퀀스
	public final static String ID =  "id";  				// 아이디 
	public final static String NAME = 	"name";  			// 이름
	public final static String PASSWORD = "password";	// 비밀번호 
	public final static String POINT =  "point";  			// 포인트
	
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		int seq = rs.getInt(	getColumnName(SEQ) );
		String id = rs.getString( getColumnName(ID) );
		String name	= rs.getString( getColumnName(NAME) );
		String password = rs.getString( getColumnName(PASSWORD) );
		int point = rs.getInt( getColumnName(POINT) );
		
		return new User(seq, name, id, password, point, null);
	}

	@Override
	public TableStrategy getTableStrategy() {
		
		return new DefaultTableStrategy(TABLE_NAME, ALIAS)
			.setAllColumn(SEQ, ID, NAME, PASSWORD, POINT);
	}

	
}