package developerhaus.repository.jdbc2;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;



import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.jdbc3.criteria.CriterionOperator;
import developerhaus.repository.jdbc3.criteria.GenericCriteria;

public class JdbcRepositoryDao<T> implements RepositoryDao<DomainTableMapperStrategy>{
	
	protected SimpleJdbcTemplate template;
	protected Class<T> mappedClass;
	//DataSource dataSource;

	@Resource
	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
		//this.dataSource = dataSource;
	}
	@Override
	public DomainTableMapperStrategy get (DomainTableMapperStrategy domain, GenericCriteria criteria) {
		String sql = "select * from " +domain.getTableName() + getQuery(criteria);
		return (DomainTableMapperStrategy) queryForObject(sql,criteria);
	}
	public int count(DomainTableMapperStrategy domain ,GenericCriteria criteria) throws SQLException{
		String sql = "select count(*) from " +domain.getTableName() + getQuery(criteria);
		return queryForInt(sql,criteria);
	}
	private String getQuery(Criteria criteria){
		String sql = "";
		List criterionList = criteria.getCriterionList();
		
		for(Iterator iter = criterionList.iterator();iter.hasNext();) {
			sql += " where ";
			Criterion<String, String, CriterionOperator> criterion = (Criterion) iter.next();
			if(criterion.getOperator().equals(CriterionOperator.EQ)) {
				sql += criterion.getKey() + CriterionOperator.EQ.toString() +" :"+criterion.getKey();
			} else if(criterion.getOperator().equals(CriterionOperator.LIKE)) {
				sql += criterion.getKey()+ " "+ CriterionOperator.LIKE.toString() + " :"+criterion.getKey();
			} else if(criterion.getOperator().equals(CriterionOperator.LIKE_LEFT)) {
				sql += criterion.getKey() + CriterionOperator.LIKE_LEFT.toString() + " :"+criterion.getKey();
			} else if(criterion.getOperator().equals(CriterionOperator.LIKE_RIGHT)) {
				sql += criterion.getKey() + CriterionOperator.LIKE_RIGHT.toString() +" :"+ criterion.getKey();;
			}
		}
		return sql;
	}
	private T queryForObject(String sql, GenericCriteria criteria){
		T result = null;
		try {
			result = template.queryForObject(sql, new BeanPropertyRowMapper<T>(mappedClass), criteria.toSqlParameterSource());
		} catch (EmptyResultDataAccessException e) {
			System.out.println("��ȸ ��� �������� �ʽ��ϴ�.");
		}
		return result;
	}
	private int queryForInt(String sql,GenericCriteria criteria){
		return template.queryForInt(sql, criteria.toSqlParameterSource());
	}
	public int add(DomainTableMapperStrategy domain ,GenericCriteria criteria) {
		return insert(domain,criteria);
		
	}
	private int insert(DomainTableMapperStrategy domain,GenericCriteria criteria){
		SimpleJdbcInsert insertBuilder = new SimpleJdbcInsert((JdbcTemplate) template.getJdbcOperations())
		.withTableName(domain.getTableName());
		insertBuilder.compile();
		return insertBuilder.execute(criteria.toSqlParameterSource());
	}
	public boolean update(DomainTableMapperStrategy domain, GenericCriteria criteria) {
		String sql = "update "+domain.getTableName()+" set "+getQuery(criteria);
		int result = template.update(sql, criteria.toSqlParameterSource());
		if (result>0){
			return true;
		}else{
			return false;
		}
	}
	public boolean update(){
		return false;
	}
	public int delete(DomainTableMapperStrategy domain,GenericCriteria criteria) {
		String sql = "delete from " +domain.getTableName()+getQuery(criteria);
		return template.update(sql, criteria.toSqlParameterSource());
	}
	public List<T> list(DomainTableMapperStrategy domain,GenericCriteria criteria){
		String sql = "select * from " +domain.getTableName() + getQuery(criteria);
		return template.query(sql, new BeanPropertyRowMapper<T>(mappedClass), criteria.toSqlParameterSource());
		//return (List)template.queryForList(sql,criteria.toSqlParameterSource());
	}
}
