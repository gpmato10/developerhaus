package developerhaus.repository.jdbc.criteria;

import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;

public class DefaultOrder implements Order {
	
	private String property;
	private OrderType type;
	
	public DefaultOrder(String property, OrderType type) {
		this.property = property;
		this.type = type;
	}

	@Override
	public String getProperty() {
		return property;
	}

	@Override
	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public OrderType getType() {
		return type;
	}

	@Override
	public void setType(OrderType type) {
		this.type = type;
	}

}
