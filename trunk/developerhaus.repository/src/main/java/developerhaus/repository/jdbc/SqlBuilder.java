package developerhaus.repository.jdbc;

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
	
	public SqlBuilder(TableStrategy tableStrategy) {
		this.sql = new StringBuilder();
		this.defaultTableStrategy = tableStrategy;
	}

	/**
	 * 구성된 쿼리 반환
	 * @return
	 */
	public String build() {
		return sql.toString();
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
		return this;
	}

	public SqlBuilder where(String whereFragment) {
		sql.append(" WHERE ");
		return this;
	}

	public SqlBuilder selectAll() {
		sql.append(" SELECT ");
		
		String[] allColumn = defaultTableStrategy.getAllColumn();
		//		TODO : 컬럼이 없을 경우 '*' 표현하는것이 맞는지 검토 필요(모든 컬럼의 명을 꼭 알고 있어야 한다면 사용불가)
		if(allColumn == null){
			sql.append(" * ");
			
		} else {
			sql.append(RepositoryUtils.toColumn(allColumn));
		}
		
		return this;
	}
}
