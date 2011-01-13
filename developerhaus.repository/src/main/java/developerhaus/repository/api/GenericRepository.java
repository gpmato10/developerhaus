package developerhaus.repository.api;

import java.io.Serializable;
import java.util.List;

import developerhaus.repository.api.criteria.Criteria;

/**
 * 최상위 Repository 인터페이스. 제네릭을 적용하여 해당 타입으로 지정된 Repository 기능을 지원하도록 강제한다.
 * 
 * @author Chanwook, Park
 * 
 * @param <D>
 * @param <I>
 */
public interface GenericRepository<D, I extends Serializable> {

	/**
	 * ID를 기준으로 단건 조회
	 * 
	 * @param id
	 * @return
	 */
	D get(I id);

	/**
	 * {@link Criteria}를 기준으로 조회
	 * 
	 * @param criteria
	 * @return
	 */
	List<D> list(Criteria criteria);

	/**
	 * 도메인 정보 갱신(추가/수정/삭제)
	 * 
	 * @param domain
	 * @return
	 */
	boolean update(D domain);
}
