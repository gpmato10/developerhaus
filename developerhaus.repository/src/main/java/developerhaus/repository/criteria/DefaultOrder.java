package developerhaus.repository.criteria;

import developerhaus.repository.api.criteria.Order;
import developerhaus.repository.api.criteria.OrderType;
import developerhaus.repository.jdbc.RepositoryUtils;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;

public class DefaultOrder implements Order {
	
	private String property;
	private OrderType type;
	
	private TableStrategyAware tableStrategyAware;
	
	public DefaultOrder(String property, OrderType type) {
		this.property = property;
		this.type = type;
	}
	
	public DefaultOrder(TableStrategyAware tableStrategyAware, String property, OrderType type) {
		this(property, type);
		this.tableStrategyAware = tableStrategyAware;
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

	public TableStrategyAware getTableStrategyAware() {
		return tableStrategyAware;
	}
	
	public String getMappedProperty(){
		
		String mappedProperty = RepositoryUtils.getColumnName(property, tableStrategyAware);
		mappedProperty = RepositoryUtils.addAliasToColumn(tableStrategyAware.getTableStrategy().getAliasName(), mappedProperty);
		
		return mappedProperty;
	}
}
