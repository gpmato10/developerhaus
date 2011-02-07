package developerhaus.repository.jdbc;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
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
			throw new SqlBuilderException("where절 추가시 criterion이 하나 이상 존재해야 합니다.");
		}

		sql.append(" WHERE ");
		
		boolean isFirst = true;
		for (Criterion c : criteria.getCriterionList()) {
			if (!isFirst) {
				sql.append(" AND ");
			} else {
				isFirst = false;
			}

			
			createQueryStringByCriteria(c);
		}
		
		
		return this;
	}

//	TODO : Criterion 구현체에 따라 쿼리 생성(현재는 SingleValueCriterion만 구현, Spring JDBC API 생각안함)
	@SuppressWarnings("rawtypes")
	private void createQueryStringByCriteria(Criterion c) {
		
		sql.append(c.getKey());
		sql.append(" ");
		sql.append(c.getOperator());
		sql.append(" '");
		sql.append(c.getValue());
		sql.append("'");
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
