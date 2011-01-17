package developerhaus.repository.hibernate;

import java.io.Serializable;

import java.util.Iterator;
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
public class HibernateRepository<D, I extends Serializable> implements GenericRepository<D, I> {
	
	protected HibernateTemplate hibernateTemplate;
	private Class targetClass;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory); 
	}
	public void setTargetClass(Class targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public D get(I id) {
		return (D) hibernateTemplate.get(targetClass, id);
	}

	@Override
	public List<D> list(Criteria criteria) {
		return hibernateTemplate.findByCriteria(getHibernateCriteria(criteria));
	}
	
	@Override
	public boolean update(D domain) {
		hibernateTemplate.update(domain);
		return true;
	}

	/**
	 * @param criteria
	 * @return DetachedCriteria
	 */
	private DetachedCriteria getHibernateCriteria(Criteria criteria) {
		DetachedCriteria hcriteria = DetachedCriteria.forClass(targetClass);
		List criterionList = criteria.getCriterionList();
		for(Iterator iter = criterionList.iterator();iter.hasNext();) {
			Criterion<String, String, CriterionOperator> criterion = (Criterion) iter.next();
			if(criterion.getOperator().equals(CriterionOperator.EQ)) {
				hcriteria.add(Restrictions.eq(criterion.getKey(), criterion.getValue()));
			} else if(criterion.getOperator().equals(CriterionOperator.LIKE)) {
				hcriteria.add(Restrictions.ilike(criterion.getKey(), "%"+criterion.getValue()+"%"));
			} else if(criterion.getOperator().equals(CriterionOperator.LIKE_LEFT)) {
				hcriteria.add(Restrictions.ilike(criterion.getKey(), "%"+criterion.getValue()));
			} else if(criterion.getOperator().equals(CriterionOperator.LIKE_RIGHT)) {
				hcriteria.add(Restrictions.ilike(criterion.getKey(), criterion.getValue()+"%"));
			}
		}
		List orderList = criteria.getOrderList();
		for(Iterator iter = orderList.iterator();iter.hasNext();) {
			Order order = (Order) iter.next();
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
	

}
