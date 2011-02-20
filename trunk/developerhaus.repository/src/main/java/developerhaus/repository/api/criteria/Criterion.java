package developerhaus.repository.api.criteria;

/**
 * 조회 조건
 * 
 * @author chanwook, Park
 * 
 * @param <K>
 * @param <T>
 */
public interface Criterion<K, O, T> {

	K getKey();

	void setKey(K key);

	T getValue();

	void setValue(T value);
	
	/**
	 * 오퍼레이션 검색
	 * 
	 * @author shughee, Park
	 */
	O getOperator();
	
	/**
	 * 오퍼레이션 세팅
	 * 
	 * @author shughee, Park
	 */
	void setOperator(O operator);
}
