
package developerhaus.domain;

import java.io.Serializable;

import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

/**
 * User Point History 클래스
 * 
 * @author sunghee, Park
 * 
 */
public class UserPoint implements Serializable, TableStrategyAware {
	
	public final static String TABLE_NAME = "USER_POINT";
	public final static String ALIAS = "point";
	
	public final static String USERPOINTSEQ = "user_point_seq";
	public final static String USERSEQ = "user_seq";
	public final static String POINT = "point";
	public final static String POINTTYPE = "point_type";
	public final static String REGDT = "reg_dt";
	
	// hibernate 에서 join 시 (subCriteria) 필요
	// public final static String MAPPED_USERSEQ = "mappedUser";
	
	@Override
	public TableStrategy getTableStrategy() {
		
		return this.getTableStrategy(ALIAS);
	}
	
	@Override
	public TableStrategy getTableStrategy(String alias) {
		
		return new DefaultTableStrategy(TABLE_NAME, alias)
		.setAllColumn(USERPOINTSEQ, USERSEQ, POINT,POINTTYPE,REGDT);
	}
	

	private int userPointSeq;
	private int userSeq;
	private int point;
	private String pointType;
	private String regDt;
	// hibernate 에서 필요
	private User mappedUserSeq;
	
	public UserPoint() {
	}
	
	public UserPoint(int userPointSeq, int userSeq, int point,
			String pointType, String regDt) {
		this.userPointSeq = userPointSeq;
		this.userSeq = userSeq;
		this.point = point;
		this.pointType = pointType;
		this.regDt = regDt;
	}
	
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getPointType() {
		return pointType;
	}
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public void setUserPointSeq(int userPointSeq) {
		this.userPointSeq = userPointSeq;
	}
	public int getUserPointSeq() {
		return userPointSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public int getUserSeq() {
		return userSeq;
	}
	public void setMappedUserSeq(User mappedUserSeq) {
		this.mappedUserSeq = mappedUserSeq;
	}
	public User getMappedUserSeq() {
		return mappedUserSeq;
	}
	
}
