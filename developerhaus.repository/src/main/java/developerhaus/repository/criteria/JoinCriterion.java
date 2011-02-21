package developerhaus.repository.criteria;

import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.jdbc.RepositoryUtils;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

public class JoinCriterion<O> implements Criterion<String, O, String> {
	

	private String join;
	
	public JoinCriterion(TableStrategyAware leftTableStrategyAware, String leftKey, TableStrategyAware rightTableStrategyAware, String rightKey){
		this.join = RepositoryUtils.addAliasToColumn(leftTableStrategyAware.getTableStrategy().getAliasName(), leftKey)
					+ CriterionOperator.EQ 
					+ RepositoryUtils.addAliasToColumn(rightTableStrategyAware.getTableStrategy().getAliasName(), rightKey)
					;
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
	public O getOperator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setOperator(O operator) {
		throw new UnsupportedOperationException();
	}
}
