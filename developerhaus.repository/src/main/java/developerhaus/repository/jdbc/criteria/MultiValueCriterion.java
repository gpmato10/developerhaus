package developerhaus.repository.jdbc.criteria;

import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.jdbc.exception.CriteriaException;


/**
 * CriterionOperator의 연산자 중 하나이상의 값을 필요료 하는 BETWEEN, NOT_BETWEEN, IN, NOT_IN을
 * 처리하기 위한 Criterion
 * @author jin
 *
 */
public class MultiValueCriterion implements Criterion<String, Object, CriterionOperator> {
	
	private String key;
	private Object[] value;
	private CriterionOperator operator;
	
	public MultiValueCriterion(String key, CriterionOperator operator, Object... values){
		
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
		this.value = values;
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
		this.value = (Object[]) value;
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
