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
public class User implements DomainTableMapperStrategy, Serializable  {
	
	public static final String TABLE_NAME = "USERS";

	public static final String KEY = "id";
	public User(int seq2, String id2, String name2, String password2,
			String point2) {
		// TODO Auto-generated constructor stub
	}
	public User(){
		
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
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
	
	
	
	
	
}
