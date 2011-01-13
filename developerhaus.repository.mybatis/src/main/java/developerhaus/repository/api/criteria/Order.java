package developerhaus.repository.api.criteria;

/**
 * 정렬 조건을 의미하는 인터페이스
 * 
 * @author Chanwook, Park
 * 
 */
public interface Order {

	/**
	 * 정렬 대상 프로퍼티 이름 반환
	 * 
	 * @return
	 */
	String getProperty();

	/**
	 * 정렬 대상 프로퍼티 이름 지정
	 * 
	 * @param propertyName
	 */
	void setProperty(String propertyName);

	/**
	 * 정렬 방법 반환
	 * 
	 * @return
	 */
	OrderType getType();

	/**
	 * 정렬 방법 지정
	 * 
	 * @param type
	 */
	void setType(OrderType type);
}
