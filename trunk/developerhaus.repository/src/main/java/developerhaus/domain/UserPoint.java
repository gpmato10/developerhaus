
package developerhaus.domain;

/**
 * User Point History 클래스
 * 
 * @author sunghee, Park
 * 
 */
public class UserPoint {

	private int userPointSeq;
	private int userSeq;
	private int point;
	private String pointType;
	private String regDt;
	// hibernate 에서 필요
	private User mappedUser;
	
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
	public void setMappedUser(User mappedUser) {
		this.mappedUser = mappedUser;
	}
	public User getMappedUser() {
		return mappedUser;
	}
	
}
