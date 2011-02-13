package developerhaus.repository.mybatis;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import developerhaus.repository.api.GenericRepository;
import developerhaus.repository.api.criteria.Criteria;

public class GenericMybatisSupportRepository<D, I extends Serializable> implements
		GenericRepository<D, I> {
	
	@Override
	//@Select("SELECT SEQ, ID, NAME, PASSWORD FROM USERS WHERE ID = #{id}")
	public D get(@Param("id") I id) {
		return null;
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
