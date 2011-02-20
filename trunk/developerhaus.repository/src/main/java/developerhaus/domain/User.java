package developerhaus.domain;

import java.io.Serializable;
import java.util.List;

import developerhaus.repository.jdbc2.DomainTableMapperStrategy;

/**
 * User Domain
 * 
 * @author sunghee, Park
 * 
 */
public class User implements Serializable  {
	
	public User(){
		
	}

	public User(int seq, String name, String id, String password, int point,
			List<UserPoint> userPointList) {
		super();
		this.seq = seq;
		this.name = name;
		this.id = id;
		this.password = password;
		this.point = point;
		this.userPointList = userPointList;
	}

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [seq=").append(seq).append(", name=").append(name)
				.append(", id=").append(id).append(", password=")
				.append(password).append(", point=").append(point)
				.append(", userPointList=").append(userPointList).append("]");
		return builder.toString();
	}
}
