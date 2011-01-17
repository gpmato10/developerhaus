/**========================================================
*파일명         : DefaultOrder.java
*파일용도       : 
*
*마지막변경일자 : 2011. 1. 17.
*마지막변경자   : want
=========================================================*/
package developerhaus.repository.hibernate;

import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;

/**
 * DefaultOrder
 * 
 * @author sunghee, Park
 */
public class DefaultOrder implements Order {

	
	private String property;
	private OrderType orderType;
	
	public DefaultOrder() {
	}
	
	public DefaultOrder(String propertyName, OrderType type) {
		this.property = propertyName;
		this.orderType = type;
	}
	
	
	
	/* (non-Javadoc)
	 * @see developerhaus.repository.api.criteria.Order#getProperty()
	 */
	@Override
	public String getProperty() {
		return property;
	}

	/* (non-Javadoc)
	 * @see developerhaus.repository.api.criteria.Order#setProperty(java.lang.String)
	 */
	@Override
	public void setProperty(String propertyName) {
		this.property = propertyName;
	}

	/* (non-Javadoc)
	 * @see developerhaus.repository.api.criteria.Order#getType()
	 */
	@Override
	public OrderType getType() {
		return orderType;
	}

	/* (non-Javadoc)
	 * @see developerhaus.repository.api.criteria.Order#setType(developerhaus.repository.api.criteria.OrderType)
	 */
	@Override
	public void setType(OrderType type) {
		this.orderType = type;
	}

}
