package developerhaus.repository.resolver;

import java.util.Iterator;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.DefaultOrder;
import developerhaus.repository.criteria.JoinCriterion;
import developerhaus.repository.criteria.MultiValueCriterion;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

/**
 * CriteriaWebArgumentResolver, criteria 로 바인딩
 * 
 * @author sunghee, Park
 * 
 * param.{fieldName} = {value}
 * param.op.{fieldName} = {operator}
 * param.dm.{fieldName} = {domainName}
 * order.{fieldName} = {orderType}
 * join.{domainName}.{fieldName} = {domainName}.{fieldName}
 */
public class CriteriaWebArgumentResolver implements WebArgumentResolver {
	
	private final static String BASE_DOMAIN_PACKAGE = "developerhaus.domain";

	@Override
	public Criteria resolveArgument(MethodParameter mp, NativeWebRequest req)
			throws Exception {
		Criteria criteria = new DefaultCriteria();
		
		Iterator<String> parameterNames = req.getParameterNames();		
		String paramName = null;
		while(parameterNames.hasNext()) {
			paramName = parameterNames.next();
			if(paramName.startsWith("param.") && !paramName.startsWith("param.op") && !paramName.startsWith("param.dm")) {
				// domain 체크해서 생성자 분기하기...
				Criterion<String, CriterionOperator, String> criterion = null;
				if(getOperator(req, paramName).equals(CriterionOperator.IN)
						|| getOperator(req, paramName).equals(CriterionOperator.NOT_IN)
						|| getOperator(req, paramName).equals(CriterionOperator.BETWEEN)
						|| getOperator(req, paramName).equals(CriterionOperator.NOT_BETWEEN)) {
					// MultiValueCriterion
					if(hasDomainName(req, paramName)) {
						criterion = new MultiValueCriterion<CriterionOperator, String>(getTableStrategy(req, paramName), getParamKey(paramName), getOperator(req, paramName), getParamValues(req, paramName));
					} else {			
						criterion = new MultiValueCriterion<CriterionOperator, String>(getParamKey(paramName), getOperator(req, paramName), getParamValues(req, paramName));
					}
				} else {
					// SingleValueCriterion
					if(hasDomainName(req, paramName)) {
						criterion = new SingleValueCriterion<CriterionOperator, String>(getTableStrategy(req, paramName), getParamKey(paramName), getOperator(req, paramName), getParamValue(req, paramName));
					} else {			
						criterion = new SingleValueCriterion<CriterionOperator, String>(getParamKey(paramName), getOperator(req, paramName), getParamValue(req, paramName));
					}
				}
				criteria.add(criterion);
			} else if(paramName.startsWith("join")) {
				Criterion criterion = new JoinCriterion<CriterionOperator>(getLeftDomain(paramName), getLeftKey(paramName), getRightDomain(req, paramName), getRightKey(req, paramName));
				criteria.add(criterion);
			} else if(paramName.startsWith("order")) {
				Order order = new DefaultOrder(getParamKey(paramName), getOrderType(req, paramName));
				criteria.add(order);
			}			
		}
		return criteria;
	}
	

	/**
	 * @param req
	 * @param paramName
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	private TableStrategyAware getTableStrategy(NativeWebRequest req, String paramName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String domainName = getParamValue(req, "param.dm."+getParamKey(paramName));
		return getDomainObject(toPascalCase(domainName));
	}

	/**
	 * param.dm.{fieldName} 이 있는지 확인한다.
	 * @param req
	 * @param paramName
	 * @return
	 */
	private boolean hasDomainName(NativeWebRequest req, String paramName) {
		return getParamValue(req, "param.dm."+getParamKey(paramName)) == null ? false : true;
	}

	/**
	 * @param req
	 * @param paramName
	 * @return
	 */
	private String getRightKey(NativeWebRequest req, String paramName) {
		String paramValue = getParamValue(req, paramName);
		//paramValue.split("[.]")[1];
		return paramValue.split("[.]")[1];
	}

	/**
	 * userPoint.userSeq -> userPoint -> new UserPoint()
	 * @param paramName
	 * @return
	 */
	private String getLeftKey(String paramName) {
		return paramName.split("[.]")[2];
	}

	/**
	 * @param req
	 * @param paramName
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	private TableStrategyAware getRightDomain(NativeWebRequest req,
			String paramName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// userPoint.userSeq
		String paramKey = getParamValue(req, paramName);
		String domainName = paramKey.split("[.]")[0];
		return getDomainObject(domainName);
	}


	/**
	 * @param paramName
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	private TableStrategyAware getLeftDomain(String paramName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return getDomainObject(getParamKey(paramName));
	}


	/**
	 * @param req
	 * @param paramName
	 * @return CriterionOperator
	 */
	private CriterionOperator getOperator(NativeWebRequest req, String paramName) {
		String operatorName = getParamValue(req, "param.op."+getParamKey(paramName));
		CriterionOperator operator = CriterionOperator.toOperator(operatorName);
		return operator;
	}

	/**
	 * @param req
	 * @param paramName
	 * @return OrderType
	 */
	private OrderType getOrderType(NativeWebRequest req, String paramName) {
		OrderType orderType = OrderType.ASC;
		if(getParamValue(req, paramName).equals("DESC")) {
			orderType = OrderType.DESC;
		}
		return orderType;
	}

	/**
	 * @param req
	 * @param paramName
	 * @return String
	 */
	private String getParamValue(NativeWebRequest req, String paramName) {
		return req.getParameter(paramName);
	}
	private String[] getParamValues(NativeWebRequest req, String paramName) {
		return req.getParameterValues(paramName);
	}

	/**
	 * @param paramName
	 * @return  String
	 */
	private String getParamKey(String paramName) {
		return paramName.split("[.]")[1];
	}
	

	/**
	 * 도메인 클래스 이름으로 객체를 리턴한다.
	 * @param paramValue
	 * @return 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private TableStrategyAware getDomainObject(String domainName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> clazz = Class.forName(BASE_DOMAIN_PACKAGE+"."+toPascalCaseWithPackage(domainName));
		return (TableStrategyAware) clazz.newInstance();
	}
	/**
	 * 첫 번째 문자를 대문자로 변경한다.
	 * @param string
	 * @return
	 */
	private String toPascalCase(String str) {
		char firstChar = str.charAt(0);
		if (Character.isLowerCase(firstChar)) {
			str = ""+Character.toUpperCase(firstChar) + str.substring(1);
		}
		return str;
	}
	/*
	 * 패키지경로가 있을 경우
	 * @param string
	 * @return
	 */
	private String toPascalCaseWithPackage(String domainName) {
		String str = domainName;
		if(domainName.indexOf(".") > -1) {
			str = domainName.split("[.]")[0]+"."+toPascalCase(domainName.split("[.]")[1]);
		} else {
			str = toPascalCase(domainName);
		}
		return str;
	}


}
