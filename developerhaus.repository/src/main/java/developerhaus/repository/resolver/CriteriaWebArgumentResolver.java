package developerhaus.repository.resolver;

import java.util.Iterator;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.HibernateCriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.DefaultOrder;
import developerhaus.repository.criteria.SingleValueCriterion;

/**
 * CriteriaWebArgumentResolver, criteria 로 바인딩
 * 
 * @author sunghee, Park
 * 
 * param.{fieldName} = {value}
 * param.op.{fieldName} = {operator}
 * order.{fieldName} = {orderType}
 */
public class CriteriaWebArgumentResolver implements WebArgumentResolver {

	@Override
	public Criteria resolveArgument(MethodParameter mp, NativeWebRequest req)
			throws Exception {
		Criteria criteria = new DefaultCriteria();
		
		Iterator<String> parameterNames = req.getParameterNames();		
		String paramName = null;
		while(parameterNames.hasNext()) {
			paramName = parameterNames.next();
			if(paramName.startsWith("param.") && !paramName.startsWith("param.op")) {
				Criterion<String, HibernateCriterionOperator, String> criterion = new SingleValueCriterion<HibernateCriterionOperator, String>(getParamKey(paramName), getOperator(req, paramName), getParamValue(req, paramName));
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
	 * @return CriterionOperator
	 */
	private HibernateCriterionOperator getOperator(NativeWebRequest req, String paramName) {
		String operatorName = getParamValue(req, "param.op."+getParamKey(paramName));
		HibernateCriterionOperator operator = HibernateCriterionOperator.toOperator(operatorName);
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

	/**
	 * @param paramName
	 * @return  String
	 */
	private String getParamKey(String paramName) {
		return paramName.split("[.]")[1];
	}

}
