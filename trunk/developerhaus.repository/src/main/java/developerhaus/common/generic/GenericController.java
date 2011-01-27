package developerhaus.common.generic;

import java.util.List;

import org.springframework.web.portlet.bind.annotation.RenderMapping;

import developerhaus.repository.api.criteria.Criteria;

/**
 * Generic Controller
 * 
 * @author sunghee, Park
 * @param <D>
 * @param <I>
 * @param <S>
 */
public class GenericController<D, I, S extends GenericService> {

	S service;
	
	@RenderMapping("/list")
	public List<D> list(Criteria criteria) {
		return service.list(criteria);
	}
}
