package developerhaus.repository.criteria;

public enum HibernateCriterionOperator {
	EQ, NOT_EQ, LIKE, LIKE_LEFT, LIKE_RIGHT, NOT_LIKE, GT, GTE, LT
	, LTE, BETWEEN, NOT_BETWEEN, IN, NOT_IN, OR;

	public static HibernateCriterionOperator toOperator(String operatorName) {
		for (HibernateCriterionOperator co : values()) {
			if (co.name().equals(operatorName)) {
				return co;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return name();
	}

	public String getName(){
		return name();
	}
}
