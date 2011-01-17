package developerhaus.repository.ibatis;

import java.io.Serializable;
import java.util.List;

import developerhaus.repository.api.GenericRepository;
import developerhaus.repository.api.criteria.Criteria;

public class IbatisRepository <D, I extends Serializable> implements GenericRepository<D, I>{

	@Override
	public D get(I id) {
		// TODO Auto-generated method stub
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
