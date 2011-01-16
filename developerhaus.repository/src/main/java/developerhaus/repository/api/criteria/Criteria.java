package developerhaus.repository.api.criteria;

import java.util.List;

/**
 * 조회 조건인 {@link Criterion}과 정렬 조건인 {@link Order}를 유지하는 컨테이너 역할을 함.
 * 
 * @author chanwook, Park
 * 
 */
public interface Criteria {

	/**
	 * 조회 조건 추가
	 * 
	 * @param criterion
	 */
	void add(Criterion<?, ?, ?> criterion);

	/**
	 * 정렬 조건 추가
	 * 
	 * @param order
	 */
	void add(Order order);
	
	/*
	 * 조회 조건 검색
	 */
	List<Criterion> getCriterionList();
	
	/*
	 * 정렬 조건 검색
	 */
	List<Order> getOrderList();
	
}
