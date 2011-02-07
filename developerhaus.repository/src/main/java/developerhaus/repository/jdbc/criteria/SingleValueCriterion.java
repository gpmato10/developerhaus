package developerhaus.repository.jdbc.criteria;

import developerhaus.repository.api.criteria.Criterion;


//<String, Object, CriterionOperator>

public class SingleValueCriterion implements Criterion<String, Object, CriterionOperator>{

	private String key;
	private Object value;
	private CriterionOperator operator;
	
	public SingleValueCriterion(String key, Object value,
			CriterionOperator operator) {
		super();
		this.key = key;
		this.value = value;
		this.operator = operator;
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
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public CriterionOperator getOperator() {
		return operator;
	}

	@Override
	public void setOperator(CriterionOperator operator) {
		this.operator = operator;
	}
}
