
package developerhaus.repository.hibernate.criteria;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.JoinCriterion;
import developerhaus.repository.criteria.SingleValueCriterion;

/**
 * HibernateCriteriaUtils
 * Criteria 를 하이버네이트 용 Criteria 로 변경한다. >> 공용으로 가야 하나보다........................
 * refactoring 은 나중에 하겠음......................................................
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
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static DetachedCriteria getHibernateCriteria(Class targetClass, Criteria criteria) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Map<String, DetachedCriteria> jCriteriaMap = new HashMap<String, DetachedCriteria>(); // join 이 되는 criteria 를 담는다.
		
		DetachedCriteria hcriteria = DetachedCriteria.forClass(targetClass);
		List<Criterion> criterionList = criteria.getCriterionList();
		for(Criterion<String, CriterionOperator, ?> criterion : criterionList) {
			if(criterion instanceof JoinCriterion<?>) {
				// new User(), seq, new UserPoint(), userSeq
				JoinCriterion<CriterionOperator> joinCriterion = (JoinCriterion<CriterionOperator>) criterion;
				// mappedUser 는 어떻게 얻어낼 것인가? 상수로 관리?? map ?? 가져다 쓸것을 생각하면 userPoint 만으로 해야 하는데...
				// TableStrategyAware 로 넘어오는 문제.. 캐스팅... userPoint 는 어떻게 알아낼 것인가
				String associationPath = getMappedName(joinCriterion.getRightKey()); // "mappedUserSeq";
//				System.out.println(" 1 associationPath : "+associationPath);
				// class 정보로 하려면... 같은 테이블을 2번 조인할 수 없다...
				DetachedCriteria jcriteria = hcriteria.createCriteria(associationPath);
				jCriteriaMap.put(associationPath, jcriteria);
			} else {
				SingleValueCriterion<CriterionOperator, String> sCriterion = (SingleValueCriterion) criterion;
				if(sCriterion.getTableStrategyAware()!=null) { 
					// domain 정보가 있다면 해당 joinCriteria 에 담아야 하고
					// 그 criteria 에 criterion 도 만들어야 한다.
					Class clazz = Class.forName("developerhaus.domain."+toPascalCase(sCriterion.getTableStrategyAware().getTableStrategy().getAliasName()));
					Field fields[] = targetClass.getDeclaredFields();
					String associationPath = "";
					for(int i=0;i<fields.length;i++) {
						if(fields[i].getType().getName().equals(clazz.getName())) {
							associationPath = fields[i].getName();
						}
					}
//					System.out.println(" 2 associationPath : "+associationPath);
					if(jCriteriaMap.containsKey(associationPath)) { // userPoint 만으로 어떻게 알아낸담...
						jCriteriaMap.get(associationPath).add(getHibernateCriterion(sCriterion.getTableStrategyAware().getClass(), criterion));
					} else { // 무조건 true 가 나와야 하지만..
						hcriteria.add(getHibernateCriterion(sCriterion.getTableStrategyAware().getClass(), criterion));
					}
				} else {
					hcriteria.add(getHibernateCriterion(targetClass, criterion));
				}
				
			}
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
	 * @param rightKey
	 * @return
	 */
	private static String getMappedName(String rightKey) {
		System.out.println("rightKey:"+rightKey);
		return "mapped"+toPascalCase(rightKey);
	}

	/**
	 * Criterion 를 Hibernate Criterion 로 바꿔서 리턴한다.
	 * @param Criterion
	 * @return org.hibernate.criterion.Criterion
	 */
	private static org.hibernate.criterion.Criterion getHibernateCriterion(Class targetClass, Criterion<String, CriterionOperator, ?> criterion) {
		org.hibernate.criterion.Criterion hCriterion = null;
		CriterionOperator operator = criterion.getOperator();
		if(operator.equals(CriterionOperator.EQ)) {
			hCriterion = Restrictions.eq(criterion.getKey(), criterion.getValue());
		} else if(operator.equals(CriterionOperator.LIKE)) {
			hCriterion = Restrictions.ilike(criterion.getKey(), "%"+criterion.getValue()+"%");
		} else if(operator.equals(CriterionOperator.LIKE_RIGHT)) {
			hCriterion = Restrictions.ilike(criterion.getKey(), "%"+criterion.getValue());
		} else if(operator.equals(CriterionOperator.LIKE_LEFT)) {
			hCriterion = Restrictions.ilike(criterion.getKey(), criterion.getValue()+"%");
		}
		return hCriterion;
	}
	
	/**
	 * 첫 번째 문자를 대문자로 변경한다.
	 * @param string
	 * @return
	 */
	private static String toPascalCase(String str) {
		char firstChar = str.charAt(0);
		if (Character.isLowerCase(firstChar)) {
			str = ""+Character.toUpperCase(firstChar) + str.substring(1);
		}
		return str;
	}
}
