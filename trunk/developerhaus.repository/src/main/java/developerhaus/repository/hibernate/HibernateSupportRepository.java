package developerhaus.repository.hibernate;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;


import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import developerhaus.repository.api.GenericRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
/**
 * 하이버네이트 Repository
 * 
 * @author sunghee, Park
 * 
 */
public class HibernateSupportRepository<D, I extends Serializable> {
	
	private HibernateTemplate hibernateTemplate;
	private Class targetClass;
	private Class<D> mappedClass;
	

	public HibernateSupportRepository() {
		this.mappedClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		System.out.println(" this.mappedClass : "+this.mappedClass);
	}
	
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory); 
	}
	public void setTargetClass(Class targetClass) {
		this.targetClass = targetClass;
	}

	public D get(I id) {
		return (D) hibernateTemplate.get(targetClass, id);
	}

	public List<D> list(Criteria criteria) {
		return hibernateTemplate.findByCriteria(getHibernateCriteria(criteria));
	}
	
	public boolean update(D domain) {
		hibernateTemplate.update(domain);
		return true;
	}

	/**
	 * Criteria 를 Hibernate Criteria 로 바꿔서 리턴한다.
	 * @param criteria
	 * @return DetachedCriteria
	 */
	private DetachedCriteria getHibernateCriteria(Criteria criteria) {
		DetachedCriteria hcriteria = DetachedCriteria.forClass(targetClass);
		List<Criterion> criterionList = criteria.getCriterionList();
		for(Criterion<?, ?, CriterionOperator> criterion : criterionList) {
			CriterionOperator operator = criterion.getOperator();
			/*if(operator.equals(CriterionOperator.OR)) {				
				hcriteria.add(Restrictions.or(getHibernateCriterion(criterion.getCriterions()[0]), getHibernateCriterion(criterion.getCriterions()[1])));
			} else {
			*/
			hcriteria.add(getHibernateCriterion(criterion));
			//}
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
	private org.hibernate.criterion.Criterion getHibernateCriterion(Criterion<?, ?, CriterionOperator> criterion) {
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
