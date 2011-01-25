package developerhaus.repository.hibernate.criteria;

import java.util.Iterator;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;

/**
 * CriteriaWebArgumentResolver, criteria 로 바인딩
 * 
 * @author sunghee, Park
 * 
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
				Criterion criterion = new DefaultCriterion<String, String, CriterionOperator>(getParamKey(paramName), getParamValue(req, paramName), getOperator(req, paramName));
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

	/**
	 * @param paramName
	 * @return  String
	 */
	private String getParamKey(String paramName) {
		return paramName.split("[.]")[1];
	}

}
