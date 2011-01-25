
package developerhaus.repository.hibernate.criteria;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;

/**
 * HibernateCriteriaUtils
 * Criteria 를 하이버네이트 용 Criteria 로 변경한다.
 * 
 * @author sunghee, Park
 * 
 */
public class HibernateCriteriaUtils {
	/**
	 * @param targetClass
	 */
	private static Class targetClass;
	public HibernateCriteriaUtils(Class targetClass) {
		this.targetClass = targetClass;
	}

	/**
	 * Criteria 를 Hibernate Criteria 로 바꿔서 리턴한다.
	 * @param criteria
	 * @return DetachedCriteria
	 */
	public static DetachedCriteria getHibernateCriteria(Criteria criteria) {
		DetachedCriteria hcriteria = DetachedCriteria.forClass(targetClass);
		List<Criterion> criterionList = criteria.getCriterionList();
		for(Criterion<?, ?, CriterionOperator> criterion : criterionList) {
			hcriteria.add(getHibernateCriterion(criterion));
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
	private static org.hibernate.criterion.Criterion getHibernateCriterion(Criterion<?, ?, CriterionOperator> criterion) {
		org.hibernate.criterion.Criterion hCriterion = null;
		CriterionOperator operator = criterion.getOperator();
		if(operator.equals(CriterionOperator.EQ)) {
			hCriterion = Restrictions.eq((String)criterion.getKey(), criterion.getValue());
		} else if(operator.equals(CriterionOperator.LIKE)) {
			hCriterion = Restrictions.ilike((String)criterion.getKey(), "%"+criterion.getValue()+"%");
		} else if(operator.equals(CriterionOperator.LIKE_RIGHT)) {
			hCriterion = Restrictions.ilike((String)criterion.getKey(), "%"+criterion.getValue());
		} else if(operator.equals(CriterionOperator.LIKE_LEFT)) {
			hCriterion = Restrictions.ilike((String)criterion.getKey(), criterion.getValue()+"%");
		}
		return hCriterion;
	}
}
