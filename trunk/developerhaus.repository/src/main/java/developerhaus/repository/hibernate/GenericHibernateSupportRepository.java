package developerhaus.repository.hibernate;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.SQLException;
import java.util.List;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import developerhaus.domain.User;
import developerhaus.repository.api.GenericRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.HibernateCriterionOperator;
import developerhaus.repository.hibernate.criteria.HibernateCriteriaUtils;
/**
 * 하이버네이트 Repository
 * 
 * @author sunghee, Park
 * 
 */
public class GenericHibernateSupportRepository<D, I extends Serializable> implements GenericRepository<D, I> {
	
	protected HibernateTemplate hibernateTemplate;
	protected Class<D> targetClass;
	

	public GenericHibernateSupportRepository() {
		this.targetClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory); 
	}
	@Override
	public D get(I id) {
		return (D) hibernateTemplate.get(targetClass, id);
	}
	@Override
	public List<D> list(Criteria criteria) {
		return hibernateTemplate.findByCriteria(HibernateCriteriaUtils.getHibernateCriteria(targetClass, criteria));
	}
	@Override
	public boolean update(D domain) {
		hibernateTemplate.update(domain);
		return true;
	}
	
	public int count(Criteria criteria) {
		return list(criteria).size();
	}	
	public List<D> page(Criteria criteria, int firstResult, int maxResult) {
		return hibernateTemplate.findByCriteria(HibernateCriteriaUtils.getHibernateCriteria(targetClass, criteria), firstResult, maxResult);
	}

}
