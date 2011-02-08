package developerhaus.repository.jdbc;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.jdbc.criteria.CriterionOperator;
import developerhaus.repository.jdbc.criteria.JoinCriterion;
import developerhaus.repository.jdbc.criteria.MultiValueCriterion;
import developerhaus.repository.jdbc.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.exception.CriteriaException;
import developerhaus.repository.jdbc.exception.SqlBuilderException;
import developerhaus.repository.jdbc.strategy.TableStrategy;

/**
 * 
 * @author jin
 *
 */
public class SqlBuilder {
	
	private StringBuilder sql;

	/**
	 * 기준이되는 테이블의 메타정보를 갖는 테이블전략객체
	 * 단일 테이블은 물론, 여러 테이블과 조인등에 항상 기준이 된다. 
	 */
	private TableStrategy defaultTableStrategy;
	private Criteria criteria;
	
	public SqlBuilder(TableStrategy tableStrategy) {
		this.sql = new StringBuilder();
		this.defaultTableStrategy = tableStrategy;
	}
	

	public SqlBuilder(TableStrategy tableStrategy, Criteria criteria) {
		this(tableStrategy);
		this.criteria = criteria;
	}


	/**
	 * 구성된 쿼리 반환
	 * @return
	 */
	public String build() {
		return sql.toString();
	}
	
	public SqlBuilder selectAll() {
		
		String[] allColumn = defaultTableStrategy.getAllColumn();
		//		TODO : 컬럼이 없을 경우 '*' 표현하는것이 맞는지 검토 필요(모든 컬럼의 명을 꼭 알고 있어야 한다면 사용불가)
		if(allColumn == null){
			this.select("*");
			
		} else {
			this.select(RepositoryUtils.toColumn(allColumn).toString());
		}
		
		return this;
	}

	public SqlBuilder select(String selectFragment) {
		sql.append(" SELECT ");
		sql.append(selectFragment);

		return this;
	}

	public SqlBuilder from() {
		sql.append(" FROM ");
		sql.append(" ");
		sql.append(defaultTableStrategy.getTableName());
		sql.append(" ");
		sql.append(defaultTableStrategy.getAliasName());
		
		return this;
	}

	@SuppressWarnings("rawtypes")
	public SqlBuilder where() {

		if(criteria == null || criteria.getCriterionList().size() == 0){
			throw new CriteriaException("where절 추가시 Criteria의 criterion이 하나 이상 존재해야 합니다.");
		}

		sql.append(" WHERE ");
		
		boolean isFirst = true;
		for (Criterion criterion : criteria.getCriterionList()) {
			if (!isFirst) {
				sql.append(" AND ");
			} else {
				isFirst = false;
			}

			
			createQueryStringByCriteria(criterion);
		}
		
		
		return this;
	}

//	TODO : Criterion 구현체에 따라 쿼리 생성(현재는 SingleValueCriterion, JoinCriterion만 구현 / Spring JDBC API 생각안함)
	@SuppressWarnings("rawtypes")
	private void createQueryStringByCriteria(Criterion criterion) {
//			to_char(sysdate, 'YYYYMMDD') between vaild_start_date and vaild_end_date
//			cpn_id in ('T00001', 'T00004', 'T00005', 'T00006');
		
		// Operation 종류 체크 및 value 개수 체크는 생성자 호출시 검증
		if(criterion instanceof MultiValueCriterion){
			
			Object[] values = (Object[]) criterion.getValue();
			
			
			if(CriterionOperator.BETWEEN.equals(criterion.getOperator()) || CriterionOperator.NOT_BETWEEN.equals(criterion.getOperator())){
				
				sql.append(criterion.getKey());
				sql.append(" ");
				sql.append(criterion.getOperator());
				sql.append(" ");
				sql.append(this.wrapSingleQuote(values[0]));
				sql.append(" AND ");
				sql.append(this.wrapSingleQuote(values[1]));
			} else if( (CriterionOperator.IN.equals(criterion.getOperator()) || CriterionOperator.NOT_IN.equals(criterion.getOperator()))){ 
				
				sql.append(criterion.getKey());
				sql.append(" ");
				sql.append(criterion.getOperator());
				sql.append(" ");
				sql.append("(");
				
				for (int i = 0; i < values.length; i++) {
					if(i > 0){
						sql.append(",");
					}
					
					sql.append(" ");
					sql.append(this.wrapSingleQuote(values[i]));
				}
				
				sql.append(")");
			}
		} else if(criterion instanceof SingleValueCriterion){
			
			// 값이 숫자타입일경우 ''를 감싸지 않는다.
			boolean isNumberValue = criterion.getValue() instanceof Number;
		
			
			sql.append(criterion.getKey());
			sql.append(" ");
			sql.append(criterion.getOperator());
			sql.append(" ");
			if(!isNumberValue){
				sql.append("'");
			}
			
			if(CriterionOperator.LIKE.equals(criterion.getOperator())){
				sql.append("%");
				sql.append(criterion.getValue());
				sql.append("%");
			} else if(CriterionOperator.LIKE_LEFT.equals(criterion.getOperator())){
				sql.append("%");
				sql.append(criterion.getValue());
			} else if(CriterionOperator.LIKE_RIGHT.equals(criterion.getOperator())){
				sql.append(criterion.getValue());
				sql.append("%");
			} else {
				sql.append(criterion.getValue());
			}
			
			if(!isNumberValue){
				sql.append("'");
			}
			
		} else if(criterion instanceof JoinCriterion){
			sql.append(criterion.getValue());
		}
		
	}
	
	/**
	 * 숫자타입이 아닐 경우 '를 좌우로 감싼다.
	 * @param value
	 */
	private StringBuffer wrapSingleQuote(Object value){
		
		StringBuffer buffer = new StringBuffer();
		boolean isNumberValue = value instanceof Number;
		
		if(!isNumberValue){
			buffer.append("'");
		}
		
		buffer.append(value);
		
		if(!isNumberValue){
			buffer.append("'");
		}
		
		return buffer;
	}

	public SqlBuilder order() {
		
		if(criteria == null || criteria.getOrderList().size() == 0){
			throw new CriteriaException("order절 추가시 Criteria의 Order가 하나 이상 존재해야 합니다.");
		}
		
		sql.append(" ORDER BY ");
		boolean isFirst = true;
		for (Order order : criteria.getOrderList()) {
			if (!isFirst) {
				sql.append(", ");
			} else {
				isFirst = false;
			}
			sql.append(order.getProperty());
			sql.append(" ");
			sql.append(order.getType());
		}
		
		
		return this;
	}

	public SqlBuilder insert(Student student) {
		sql.append(" INSERT INTO ");
		sql.append(defaultTableStrategy.getTableName());
		sql.append(" ( ");
		
		String[] allColumn = defaultTableStrategy.getAllColumn();
		if(allColumn == null){
			throw new SqlBuilderException("도메인정보로 insert시 도메인과 매핑된 컬럼명이 모두 정의되어 있어야 합니다.");
			
		} else {
			sql.append(RepositoryUtils.toColumn(allColumn));
		}
		
		sql.append(" ) ");
		sql.append(" VALUES( ");
		sql.append(RepositoryUtils.toMappedColumn(allColumn));
		sql.append(" ) ");
		
		return this;
	}
}
