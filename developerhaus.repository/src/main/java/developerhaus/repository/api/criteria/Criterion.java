package developerhaus.repository.api.criteria;

/**
 * 조회 조건
 * 
 * @author chanwook, Park
 * 
 * @param <K>
 * @param <T>
 */
public interface Criterion<K, T> {

	K getKey();

	void setKey(K key);

	T getValue();

	void setValue(T value);
}
