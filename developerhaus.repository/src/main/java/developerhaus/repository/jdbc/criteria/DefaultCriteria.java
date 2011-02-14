package developerhaus.repository.jdbc.criteria;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.api.criteria.Criterion;
import developerhaus.repository.api.criteria.Order;

public class DefaultCriteria implements Criteria{
	
	private List<Criterion> criterionList = new ArrayList<Criterion>();
	private List<Order> orderList = new ArrayList<Order>();

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
	
	public MapSqlParameterSource toSqlParameterSource() {
		MapSqlParameterSource msps = new MapSqlParameterSource();

		for (Criterion c : criterionList) {
				if (CriterionOperator.EQ.equals(c.getOperator())) {
					msps.addValue((String) c.getKey(), c.getValue());
					System.out.println("EQ");
				} else if (CriterionOperator.LIKE.equals(c.getOperator())) {
					msps.addValue((String) c.getKey(), "%" + c.getValue()+"%");
				}  else if (CriterionOperator.LIKE_LEFT.equals(c.getOperator())) {
					msps.addValue((String) c.getKey(), "%" + c.getValue());
				} else if (CriterionOperator.LIKE_RIGHT.equals(c.getOperator())) {
					msps.addValue((String) c.getKey(), c.getValue() + "%");
				//} else if (CriterionOperator.NONE.equals(c.getOperator())) {
				//	msps.addValue((String) c.getKey(), c.getValue());
				} else {
					msps.addValue((String) c.getKey(), c.getValue());
				}
				//System.out.println(msps.getValue("name")+"====value");
		}

		return msps;
	}
}
