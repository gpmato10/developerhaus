package developerhaus.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;

public class DefaultCriteria implements Criteria{
	
	private List<Criterion> criterionList;
	private List<Order> orderList;
	
	public DefaultCriteria(){
		criterionList = new ArrayList<Criterion>(); 
		orderList = new ArrayList<Order>();
	}

	@Override
	public void add(Criterion<?, ?, ?> criterion) {
		this.criterionList.add(criterion);
	}

	@Override
	public void add(Order order) {
		this.orderList.add(order);
	}

	@Override
	public List<Criterion> getCriterionList() {
		return this.criterionList;
	}

	@Override
	public List<Order> getOrderList() {
		return this.orderList;
	}
}
