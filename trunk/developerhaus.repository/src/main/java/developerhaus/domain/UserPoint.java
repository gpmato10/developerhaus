
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
	/**
	 * @return the userSeq
	 */
	public int getUserSeq() {
		return userSeq;
	}
	/**
	 * @param userSeq the userSeq to set
	 */
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(int point) {
		this.point = point;
	}
	/**
	 * @return the pointType
	 */
	public String getPointType() {
		return pointType;
	}
	/**
	 * @param pointType the pointType to set
	 */
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
	/**
	 * @return the regDt
	 */
	public String getRegDt() {
		return regDt;
	}
	/**
	 * @param regDt the regDt to set
	 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	/**
	 * @param userPointSeq the userPointSeq to set
	 */
	public void setUserPointSeq(int userPointSeq) {
		this.userPointSeq = userPointSeq;
	}
	/**
	 * @return the userPointSeq
	 */
	public int getUserPointSeq() {
		return userPointSeq;
	}
	
	
}
