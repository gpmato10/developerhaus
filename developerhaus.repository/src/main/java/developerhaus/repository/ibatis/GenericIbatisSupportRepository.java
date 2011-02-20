package developerhaus.repository.ibatis;

import java.io.Serializable;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;

import developerhaus.repository.api.GenericRepository;
import developerhaus.repository.api.criteria.Criteria;

public class GenericIbatisSupportRepository<D, I extends Serializable> implements GenericRepository<D, I> {
	
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		sqlMapClientTemplate = new SqlMapClientTemplate(sqlMapClient);
	}

	@Override
	public D get(I id) {
		return (D) sqlMapClientTemplate.queryForObject("get", id);
	}

	@Override
	public List<D> list(Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(D domain) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
