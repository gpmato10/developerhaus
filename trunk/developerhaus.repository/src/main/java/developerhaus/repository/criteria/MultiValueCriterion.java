package developerhaus.repository.criteria;

import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.jdbc.RepositoryUtils;
import developerhaus.repository.jdbc.exception.CriteriaException;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;


/**
 * CriterionOperator의 연산자 중 하나이상의 값을 필요료 하는 BETWEEN, NOT_BETWEEN, IN, NOT_IN을
 * 처리하기 위한 Criterion
 * @author jin
 *
 */
public class MultiValueCriterion<O, T> implements Criterion<String, O, T> {
	
	private String key;
	private O operator;
	private T[] values;
	
	private TableStrategyAware tableStrategyAware;

	// MultiValue를 위해 만든 Criterion으로 생성시 복수의 값을 받도록 처리해야 한다.
	public MultiValueCriterion(String key, O operator, T... values){
		
		if(!(CriterionOperator.BETWEEN.equals(operator) 
				|| CriterionOperator.NOT_BETWEEN.equals(operator) 
				|| CriterionOperator.IN.equals(operator) 
				|| CriterionOperator.NOT_IN.equals(operator)
			)){
			throw new CriteriaException("CriterionOperator : " + operator + System.getProperty("line.separator") 
											+ "CriterionOperator의  BETWEEN, NOT_BETWEEN, IN, NOT_IN 연산자만을 이용하여 생성할 수 있습니다.");
		}
		
		if(CriterionOperator.BETWEEN.equals(operator) || CriterionOperator.NOT_BETWEEN.equals(operator)){
			if(values == null || values.length != 2){
				throw new CriteriaException("values count : " + (values == null ? 0 : values.length) + System.getProperty("line.separator")
											+ "CriterionOperator의  BETWEEN, NOT_BETWEEN 연산자는 2개의 value를 필요로 합니다.");
			}
		}
		
		if(CriterionOperator.IN.equals(operator) || CriterionOperator.NOT_IN.equals(operator)){
			if(values == null || values.length == 0){
				throw new CriteriaException("values count : 0" + System.getProperty("line.separator")
											+ "CriterionOperator의  IN, NOT_IN 연산자는 1개 이상의 value를 필요로 합니다.");
			}
		}
		
		this.key = key;
		this.operator = operator;
		this.values = values;
	}

	public MultiValueCriterion(TableStrategyAware tableStrategyAware, String key, O operator, T... values){

		this(key, operator, values);
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

	
	public T[] getValues() {
		
		return this.values;
	}
	

	@Override
	public T getValue() {
		throw new UnsupportedOperationException("하나 이상의 값을 반환하기 위해 T getValues() 메소드 호출");
	}
	

	@Override
	public void setValue(T value) {
		throw new UnsupportedOperationException("하나 이상의 값을 반환하기 위해 T getValues() 메소드 호출");
		
	}


	@Override
	public O getOperator() {
		return this.operator;
	}


	@Override
	public void setOperator(O operator) {
		this.operator = operator;
	}
	
	public TableStrategyAware getTableStrategyAware() {
		return tableStrategyAware;
	}

	public void setTableStrategyAware(TableStrategyAware tableStrategyAware) {
		this.tableStrategyAware = tableStrategyAware;
	}

	public String getMappedKey(){
		String mappedKey = RepositoryUtils.getColumnName(key, tableStrategyAware);
		mappedKey = RepositoryUtils.addAliasToColumn(tableStrategyAware.getTableStrategy().getAliasName(), mappedKey);
		
		return mappedKey;
	}
}
