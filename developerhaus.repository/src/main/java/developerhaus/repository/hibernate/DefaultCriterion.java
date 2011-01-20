package developerhaus.repository.hibernate;

import developerhaus.repository.api.criteria.Criterion;
/**
 * DefaultCriterion
 * 
 * @author sunghee, Park
 */
public class DefaultCriterion<K, T, O> implements Criterion<K, T, O> {

	private K key;
	private T value;
	private O operator;
	
	public DefaultCriterion(K key, T value, O operator) {
		this.key = key;
		this.value = value;
		this.operator = operator;
	}
	
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public void setKey(K key) {
		this.key = key;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}
	@Override
	public void setOperator(O operator) {
		this.operator = operator;
	}
	@Override
	public O getOperator() {
		return operator;
	}

}
