package developerhaus.repository.jdbc;

import java.lang.reflect.Field;

import org.apache.ibatis.ognl.MapElementsAccessor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.JoinCriterion;
import developerhaus.repository.criteria.MultiValueCriterion;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.exception.CriteriaException;
import developerhaus.repository.jdbc.exception.SqlBuilderException;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;
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
	private TableStrategy[] tableStrategys;
	
	private int parameterOrder;
	
	private Class mappedClass;
	
	public SqlBuilder(TableStrategyAware tableStrategyAware) {
		this.sql = new StringBuilder();
		this.defaultTableStrategy = tableStrategyAware.getTableStrategy();
		this.parameterOrder = 1;
		mappedClass=tableStrategyAware.getClass();
	}
	

	public SqlBuilder(TableStrategyAware tableStrategyAware, Criteria criteria) {
		this(tableStrategyAware);
		this.criteria = criteria;
	}


	public SqlBuilder(TableStrategyAware tableStrategyAware, Criteria criteria, TableStrategy... tableStrategys) {
		this(tableStrategyAware, criteria);
		this.tableStrategys = tableStrategys;
	}
	

	/**
	 * 구성된 쿼리 반환
	 * @return
	 */
	public String build() {
		return sql.toString();
	}
	
	public SqlBuilder selectAll() {
		
		StringBuffer selectBuffer = new StringBuffer();
		
		String[] allColumn = defaultTableStrategy.getAllColumn();
		if(allColumn != null){
			selectBuffer.append(RepositoryUtils.toColumn(allColumn));
			
			if(tableStrategys != null){
				for(int i = 0; i < tableStrategys.length; i++){
					
					allColumn = tableStrategys[i].getAllColumn();
					if(allColumn != null){
						selectBuffer.append(" ,");
						selectBuffer.append(RepositoryUtils.toColumn(allColumn));
					}
				}
			}
			
			this.select(selectBuffer.toString());
		} else {
			this.select(" * ");
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
		sql.append(" ");
		
		if(tableStrategys != null){
			for(int i = 0; i < tableStrategys.length; i++){
				sql.append(", ");
				sql.append(tableStrategys[i].getTableName());
				sql.append(" ");
				sql.append(tableStrategys[i].getAliasName());
			}
		}
		
		return this;
	}

	@SuppressWarnings("rawtypes")
	public SqlBuilder where(){
		
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
			try {
				createQueryStringByCriteria(criterion);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return this;
	}

//	TODO : Criterion 구현체에 따라 쿼리 생성(현재는 SingleValueCriterion, JoinCriterion만 구현 / Spring JDBC API 생각안함)
	@SuppressWarnings("rawtypes")
	private void createQueryStringByCriteria(Criterion criterion) throws Exception {
		
		// Operation 종류 체크 및 value 개수 체크는 생성자 호출시 검증
		if(criterion instanceof MultiValueCriterion){
			
			String mappedKey = ((MultiValueCriterion)criterion).getKey();
			Object[] values = (Object[]) criterion.getValue();
			
			if(CriterionOperator.BETWEEN.equals(criterion.getOperator()) || CriterionOperator.NOT_BETWEEN.equals(criterion.getOperator())){
				
				sql.append(criterion.getKey());
				sql.append(" ");
				sql.append(criterion.getOperator());
				sql.append(" ");
				sql.append( RepositoryUtils.toSqlParameterSource(mappedKey + "_" + (parameterOrder++)) );
				sql.append(" AND ");
				sql.append( RepositoryUtils.toSqlParameterSource(mappedKey + "_" + (parameterOrder++)) );
				
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
					sql.append( RepositoryUtils.toSqlParameterSource(mappedKey + "_" + (parameterOrder++)) );
				}
				
				sql.append(")");
			}
			
		} else if(criterion instanceof SingleValueCriterion){
			
			
//			JdbcUserRepository r = new JdbcUserRepository();
//			Class c = JdbcUserRepository.class;
//			
//			Field f = c.getField("name".toUpperCase());
//			System.out.println(f.get(r));
			
			String mappedKey = 	(String) criterion.getKey();
			Field f = mappedClass.getField(mappedKey.toUpperCase());
			String alaisMappedKey = (String) f.get(defaultTableStrategy);
			
//			sql.append(criterion.getKey());
			sql.append(alaisMappedKey);
			sql.append(" ");
			sql.append(criterion.getOperator());
			sql.append(" ");
			
			sql.append( RepositoryUtils.toSqlParameterSource(mappedKey + "_" + (parameterOrder++)) );
			
		} else if(criterion instanceof JoinCriterion){
			sql.append(criterion.getValue());
		}
		
	}
	
	
	public MapSqlParameterSource getMapSqlParameterSource(){
		
		MapSqlParameterSource msps = new MapSqlParameterSource();
		int parameterOrder = 1;
		
		for (Criterion criterion : criteria.getCriterionList()) {
			
			if(criterion instanceof MultiValueCriterion){
				
				String mappedKey = ((MultiValueCriterion)criterion).getKey();
				Object[] values = (Object[]) criterion.getValue();
				
				for(int i = 0; i < values.length; i++){
					msps.addValue(mappedKey + "_" + (parameterOrder++), values[i]);
				}
					
			} else if(criterion instanceof SingleValueCriterion){
				
				String mappedKey = (String)criterion.getKey();
				
				if(CriterionOperator.LIKE.equals(criterion.getOperator())){
					msps.addValue(mappedKey + "_" + (parameterOrder++), "%" + criterion.getValue() + "%");
				} else if(CriterionOperator.LIKE_LEFT.equals(criterion.getOperator())){
					msps.addValue(mappedKey + "_" + (parameterOrder++), "%" + criterion.getValue());
				} else if(CriterionOperator.LIKE_RIGHT.equals(criterion.getOperator())){
					msps.addValue(mappedKey + "_" + (parameterOrder++), criterion.getValue() + "%");
				} else {
					msps.addValue(mappedKey + "_" + (parameterOrder++), criterion.getValue());
				}
				
			} else if(criterion instanceof JoinCriterion){
				
			}
		}
		return msps;
	}
	

	public SqlBuilder order() {
		
		if(criteria != null && criteria.getOrderList().size() > 0){
		
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
		sql.append(RepositoryUtils.toSqlParameterSource(allColumn));
		sql.append(" ) ");
		
		return this;
	}
}
