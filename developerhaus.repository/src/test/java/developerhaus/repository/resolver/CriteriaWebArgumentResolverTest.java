/**========================================================
*파일명         : CriteriaWebArgumentResolverTest.java
*파일용도       : 
*
*마지막변경일자 : 2011. 1. 25.
*마지막변경자   : want
=========================================================*/
package developerhaus.repository.resolver;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;

import developerhaus.domain.User;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.HibernateCriterionOperator;
import developerhaus.repository.criteria.JoinCriterion;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.resolver.CriteriaWebArgumentResolver;
import developerhaus.user.UserController;

/**
 * @author want
 *
 */
public class CriteriaWebArgumentResolverTest {
	
	CriteriaWebArgumentResolver resolver = null;
	MethodParameter mp = null;
	
	@Before
	public void setUp() throws Exception {
		resolver = new CriteriaWebArgumentResolver();
		mp = new MethodParameter(UserController.class.getMethod("list", Criteria.class), 0);
	}
	
	@Test
	public void listFromWeb() throws Exception {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setMethod("GET");
		
		req.addParameter("param.name", "박");
		req.addParameter("param.op.name", CriterionOperator.LIKE.getName());		
		req.addParameter("param.dm.name", "user");
		
		req.addParameter("param.id", "want813");
		req.addParameter("param.op.id", CriterionOperator.EQ.getName());

		req.addParameter("order.seq", "DESC");		
		
		req.addParameter("join.user.seq", "userPoint.userSeq");
		
		Criteria cr = (DefaultCriteria) resolver.resolveArgument(mp, new ServletWebRequest(req));
		
		assertNotNull(cr);
		
		List<Criterion> criterionList = cr.getCriterionList();
		assertEquals(criterionList.size(), 3);
		List<Order> orderList = cr.getOrderList();
		assertEquals(orderList.size(), 1);
		
		Criterion<String, String, CriterionOperator> criterion = criterionList.get(0);
		assertEquals(criterion.getKey(), "name");
		assertEquals(criterion.getValue(), "박");
		assertEquals(criterion.getOperator(), CriterionOperator.LIKE);
		assertEquals(criterion instanceof SingleValueCriterion, true);
		
		SingleValueCriterion sCriterion = (SingleValueCriterion) criterion;
		assertTrue(sCriterion.getTableStrategyAware() instanceof User);
		System.out.println("sCriterion.getTableStrategyAware():"+sCriterion.getTableStrategyAware());
		
		criterion = criterionList.get(1);
		assertEquals(criterion.getKey(), "id");
		assertEquals(criterion.getValue(), "want813");
		assertEquals(criterion.getOperator(), CriterionOperator.EQ);
		
		criterion = criterionList.get(2);
		System.out.println(criterion instanceof JoinCriterion);
		assertEquals(criterion instanceof JoinCriterion, true);
		
		
		
		Order order = orderList.get(0);
		assertEquals(order.getProperty(), "seq");
		assertEquals(order.getType(), OrderType.DESC);
		
	}
}
