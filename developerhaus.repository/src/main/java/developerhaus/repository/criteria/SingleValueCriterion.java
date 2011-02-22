package developerhaus.repository.criteria;

import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.jdbc.RepositoryUtils;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;


//<String, Object, CriterionOperator>

public class SingleValueCriterion<O, T> implements Criterion<String, O, T>{
	
	private String key;
	private O operator;
	private T value;
	
	// Criterion의 주체가 되는 테이블 정보를 유지
	private TableStrategyAware tableStrategyAware;
	
	// hibernate 에서 필요
	public SingleValueCriterion(String key, O operator, T value) {
		this.key = key;
		this.operator = operator;
		this.value = value;
	}
	
	public SingleValueCriterion(TableStrategyAware tableStrategyAware, String key, O operator, T value) {
//		this(key, operator, value);
		
		key = RepositoryUtils.getColumnName(key, tableStrategyAware);
		key = RepositoryUtils.addAliasToColumn(tableStrategyAware.getTableStrategy().getAliasName(), key);
		
		this.key = key;
		this.operator = operator;
		this.value = value;
		this.tableStrategyAware = tableStrategyAware;
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
