package developerhaus.repository.mapper;

import static developerhaus.repository.jdbc.RepositoryUtils.getColumnName;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

public class UserPointRowMapper implements RowMapper<UserPoint>,TableStrategyAware{

	public final static String TABLE_NAME = "point";
	public final static String ALIAS = "point";
	
	private String USERPOINTSEQ = "user_point_seq";
	private String USERSEQ = "user_seq";
	private String POINT = "point";
	private String POINTTYPE = "point_type";
	private String REGDT = "reg_dt";
	



	@Override
	public UserPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		int userPointSeq = rs.getInt(	getColumnName(USERPOINTSEQ) );
		int userSeq = rs.getInt( getColumnName(USERSEQ) );
		int point	= rs.getInt( getColumnName(POINT) );
		String pointType = rs.getString( getColumnName(POINTTYPE) );
		String regDt = rs.getString( getColumnName(REGDT) );
		
		return new UserPoint(userPointSeq, userSeq, point, pointType,regDt);
	}
	
	@Override
	public TableStrategy getTableStrategy() {
		
		return new DefaultTableStrategy(TABLE_NAME, ALIAS)
			.setAllColumn(USERPOINTSEQ, USERSEQ, POINT,POINTTYPE,REGDT);
	}

	
	
}
