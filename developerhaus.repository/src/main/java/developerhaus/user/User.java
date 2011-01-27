package developerhaus.user;

import java.io.Serializable;
import java.util.List;
/**
 * User Domain
 * 
 * @author sunghee, Park
 * 
 */
public class User implements Serializable{

	private int seq;
	private String name;
	private String id;
	private String password;
	private int point;
	private List<UserPoint> userPointList;
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setUserPointList(List<UserPoint> userPointList) {
		this.userPointList = userPointList;
	}
	public List<UserPoint> getUserPointList() {
		return userPointList;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(int point) {
		this.point = point;
	}
	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}
	
	
	
}