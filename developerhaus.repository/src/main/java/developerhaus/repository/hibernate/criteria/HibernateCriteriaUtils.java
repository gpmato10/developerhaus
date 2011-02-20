
package developerhaus.repository.hibernate.criteria;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.HibernateCriterionOperator;

/**
 * HibernateCriteriaUtils
 * Criteria 를 하이버네이트 용 Criteria 로 변경한다.
 * 
 * @author sunghee, Park
 * 
 */
public class HibernateCriteriaUtils {
	
	private HibernateCriteriaUtils() {
    }
	
	/**
	 * Criteria 를 Hibernate Criteria 로 바꿔서 리턴한다.
	 * @param criteria
	 * @return DetachedCriteria
	 */
	public static DetachedCriteria getHibernateCriteria(Class targetClass, Criteria criteria) {
		DetachedCriteria hcriteria = DetachedCriteria.forClass(targetClass);
		List<Criterion> criterionList = criteria.getCriterionList();
		for(Criterion<String, HibernateCriterionOperator, ?> criterion : criterionList) {
			hcriteria.add(getHibernateCriterion(targetClass, criterion));
		}
		List<Order> orderList = criteria.getOrderList();
		for(Order order : orderList) {
			org.hibernate.criterion.Order horder = null;
			if(order.getType().equals(OrderType.ASC)) {
				horder = org.hibernate.criterion.Order.asc(order.getProperty());
			} else {
				horder = org.hibernate.criterion.Order.desc(order.getProperty());
			}			
			hcriteria.addOrder(horder);
		}
		return hcriteria;
	}
	
	/**
	 * Criterion 를 Hibernate Criterion 로 바꿔서 리턴한다.
	 * @param Criterion
	 * @return org.hibernate.criterion.Criterion
	 */
	private static org.hibernate.criterion.Criterion getHibernateCriterion(Class targetClass, Criterion<String, HibernateCriterionOperator, ?> criterion) {
		org.hibernate.criterion.Criterion hCriterion = null;
		HibernateCriterionOperator operator = criterion.getOperator();
		if(operator.equals(HibernateCriterionOperator.EQ)) {
			hCriterion = Restrictions.eq(criterion.getKey(), criterion.getValue());
		} else if(operator.equals(HibernateCriterionOperator.LIKE)) {
			hCriterion = Restrictions.ilike(criterion.getKey(), "%"+criterion.getValue()+"%");
		} else if(operator.equals(HibernateCriterionOperator.LIKE_RIGHT)) {
			hCriterion = Restrictions.ilike(criterion.getKey(), "%"+criterion.getValue());
		} else if(operator.equals(HibernateCriterionOperator.LIKE_LEFT)) {
			hCriterion = Restrictions.ilike(criterion.getKey(), criterion.getValue()+"%");
		}
		return hCriterion;
	}
}
