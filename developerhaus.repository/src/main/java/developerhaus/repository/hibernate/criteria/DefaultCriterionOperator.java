package developerhaus.repository.hibernate.criteria;

public enum DefaultCriterionOperator {
	EQ("="), NOT_EQ("!="), LIKE("LIKE"), LIKE_LEFT("LIKE"), LIKE_RIGHT("LIKE"), NOT_LIKE("NOT LIKE"), GT(">"), GTE(">="), LT(
	"<"), LTE("<="), BETWEEN("BETWEEN"), NOT_BETWEEN("NOT BETWEEN"), IN("IN"), NOT_IN("NOT IN"), OR("OR");

	private String operatorText;

	DefaultCriterionOperator(String operatorText) {
		this.operatorText = operatorText;
	}

	@Override
	public String toString() {
		return this.operatorText;
	}

	public String getOperatorText() {
		return operatorText;
	}

	public static DefaultCriterionOperator toOperator(String operatorName) {
		for (DefaultCriterionOperator co : values()) {
			if (co.name().equals(operatorName)) {
				return co;
			}
		}
		return null;
	}

	public static DefaultCriterionOperator getDefaultMultiValueOperator() {
		return BETWEEN;
	}

	public static DefaultCriterionOperator getDefaultSingleValueOperator() {
		return EQ;
	}

	public String getName(){
		return name();
	}
}
