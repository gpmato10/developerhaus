package developerhaus.repository.criteria;

import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.jdbc.RepositoryUtils;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

public class JoinCriterion<O> implements Criterion<String, O, String> {
	

	private String join;
	
	private TableStrategyAware leftTableStrategyAware;
	private String leftKey;
	private TableStrategyAware rightTableStrategyAware;
	private String rightKey;
	
	public JoinCriterion(TableStrategyAware leftTableStrategyAware, String leftKey, TableStrategyAware rightTableStrategyAware, String rightKey){
		
		
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		
		leftKey =  RepositoryUtils.getColumnName(leftKey, leftTableStrategyAware);
		rightKey = RepositoryUtils.getColumnName(rightKey, rightTableStrategyAware);
		
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

	
	
	
	
	public TableStrategyAware getLeftTableStrategyAware() {
		return leftTableStrategyAware;
	}

	public void setLeftTableStrategyAware(TableStrategyAware leftTableStrategyAware) {
		this.leftTableStrategyAware = leftTableStrategyAware;
	}

	public String getLeftKey() {
		return leftKey;
	}

	public void setLeftKey(String leftKey) {
		this.leftKey = leftKey;
	}

	public TableStrategyAware getRightTableStrategyAware() {
		return rightTableStrategyAware;
	}

	public void setRightTableStrategyAware(
			TableStrategyAware rightTableStrategyAware) {
		this.rightTableStrategyAware = rightTableStrategyAware;
	}

	public String getRightKey() {
		return rightKey;
	}

	public void setRightKey(String rightKey) {
		this.rightKey = rightKey;
	}
}
