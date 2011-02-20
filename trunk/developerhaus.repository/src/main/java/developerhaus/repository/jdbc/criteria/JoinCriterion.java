package developerhaus.repository.jdbc.criteria;

import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.criteria.CriterionOperator;

public class JoinCriterion implements Criterion<String, String, CriterionOperator> {
	

	private String join;
	
	public JoinCriterion(String leftKey, String rightKey){
		this.join = leftKey + CriterionOperator.EQ + rightKey;
	}

	@Override
	public String getValue() {
		return this.join;
	}
	
	@Override
	public String getKey() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setKey(String key) {
		throw new UnsupportedOperationException();
	}


	@Override
	public void setValue(String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CriterionOperator getOperator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setOperator(CriterionOperator operator) {
		throw new UnsupportedOperationException();
	}
}
