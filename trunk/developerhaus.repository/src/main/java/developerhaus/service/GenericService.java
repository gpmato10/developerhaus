package developerhaus.service;

import java.util.List;

import developerhaus.repository.api.GenericRepository;
import developerhaus.repository.api.criteria.Criteria;

/**
 * Generic Service
 * 
 * @author sunghee, Park
 * @param <D>
 * @param <I>
 * @param <R>
 */
public class GenericService<D, I, R extends GenericRepository> {

	R repository;
	
	public List<D> list(Criteria criteria) {
		return repository.list(criteria);
	}
}
