package developerhaus.repository.hibernate;

import java.io.Serializable;

import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import developerhaus.domain.User;
import developerhaus.repository.api.GenericRepository;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
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
		D user = (D) hibernateTemplate.get(targetClass, id);
		return user;
	}

	@Override
	public List<D> list(Criteria criteria) {
		// 리팩토링 필요, sql 가져오는... Criteria interface 에 getSql 만들어서 각 기술별 getSql 구현하게?
		// 아님 하이버네이트의 findByCriteria 를 이용하게...?
		// 하드코딩되어 잇음....
		StringBuffer sql = new StringBuffer("from ").append(targetClass.getSimpleName());
		String[] params = null;
		List criterionList = criteria.getCriterionList();
		if(criterionList!=null) {
			sql.append(" where ");
			params = new String[criterionList.size()];
			for(Iterator iter = criterionList.iterator();iter.hasNext();) {
				Criterion criterion = (DefaultCriterion) iter.next();
				if(criterion.getOperator().equals(CriterionOperator.EQ)) {
					sql.append(criterion.getKey()).append(" ").append(CriterionOperator.EQ).append(" ?");
				}
				params[0] = (String) criterion.getValue();
			}
		}
		List<D> list = (List<D>) hibernateTemplate.find(sql.toString(), params);
		return list;
	}

	@Override
	public boolean update(D domain) {
		hibernateTemplate.update(domain);
		return true;
	}

}
