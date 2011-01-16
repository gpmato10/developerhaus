package developerhaus.repository.api.criteria;

/**
 * 조회 조건
 * 
 * @author chanwook, Park
 * 
 * @param <K>
 * @param <T>
 */
public interface Criterion<K, T, O> {

	K getKey();

	void setKey(K key);

	T getValue();

	void setValue(T value);
	
	O getOperator();
	
	void setOperator(O operator);
}
