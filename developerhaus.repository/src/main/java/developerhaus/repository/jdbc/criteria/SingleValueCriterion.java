package developerhaus.repository.jdbc.criteria;

import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.criteria.CriterionOperator;


//<String, Object, CriterionOperator>

public class SingleValueCriterion<String, O, T> implements Criterion<String, O, T>{
	
	private String key;
	private O operator;
	private T value;
	
	public SingleValueCriterion(String key, O operator, T value) {
		this.key = key;
		this.operator = operator;
		this.value = value;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(String key) {
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
	public O getOperator() {
		return operator;
	}

	@Override
	public void setOperator(O operator) {
		this.operator = operator;
	}
}
