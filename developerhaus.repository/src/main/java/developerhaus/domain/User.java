package developerhaus.domain;

import java.io.Serializable;
import java.util.List;

import developerhaus.repository.UserRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.DefaultOrder;
import developerhaus.repository.criteria.JoinCriterion;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

/**
 * User Domain
 * 
 * @author sunghee, Park
 * 
 */
public class User implements Serializable, TableStrategyAware {
	
	private UserRepository userRepository;
	
	public final static String TABLE_NAME = "USERS";
	public final static String ALIAS = "user";
	// DB 의존성을 제거하기 위해 DB컬럼명 변화에 상관없이 대표되는 컬럼명 정의
	// TODO : 쿼리결과 도메인 속성명과 매핑하기 위한 정책 수립(Spring JDBC 붙인 후 다시 생각)
	
	public final static String SEQ = "seq";  		// 시퀀스
	public final static String ID =  "id";  				// 아이디 
	public final static String NAME = 	"name";  			// 이름
	public final static String PASSWORD = "password";	// 비밀번호 
	public final static String POINT =  "point";  			// 포인트
	
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public TableStrategy getTableStrategy() {
		return this.getTableStrategy(ALIAS);
	}
	

	@Override
	public TableStrategy getTableStrategy(String alias) {
		return new DefaultTableStrategy(TABLE_NAME, alias)
		.setAllColumn(SEQ, ID, NAME,PASSWORD, POINT);
	}
	
	
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
		if(this.userPointList == null) {			
			this.userPointList = userRepository.getUserPointList(this);
		}
		return this.userPointList;
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
