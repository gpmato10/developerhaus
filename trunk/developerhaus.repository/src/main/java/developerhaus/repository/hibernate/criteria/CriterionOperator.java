package developerhaus.repository.hibernate.criteria;

public enum CriterionOperator {
	EQ, NOT_EQ, LIKE, LIKE_LEFT, LIKE_RIGHT, NOT_LIKE, GT, GTE, LT
	, LTE, BETWEEN, NOT_BETWEEN, IN, NOT_IN, OR;

	public static CriterionOperator toOperator(String operatorName) {
		for (CriterionOperator co : values()) {
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
