package developerhaus.repository.criteria;

public enum CriterionOperator {

    EQ("="), NOT_EQ("!="), LIKE("LIKE"), LIKE_LEFT("LIKE"), LIKE_RIGHT("LIKE"), NOT_LIKE("NOT LIKE"), GT(">"), GTE(">="), LT(
            "<"), LTE("<="), BETWEEN("BETWEEN"), NOT_BETWEEN("NOT BETWEEN"), IN("IN"), NOT_IN("NOT IN");

    private String operatorText;

    CriterionOperator(String operatorText) {
        this.operatorText = operatorText;
    }

    @Override
    public String toString() {
        return this.operatorText;
    }
	public String getName(){
		return name();
	}
	public static CriterionOperator toOperator(String operatorName) {
		for (CriterionOperator co : values()) {
			if (co.name().equals(operatorName)) {
				return co;
			}
		}
		return null;
	}
//
//    public String getOperatorText() {
//        return operatorText;
//    }
//
//    public static CriterionOperator toOperator(String operatorName) {
//        for (CriterionOperator co : values()) {
//            if (co.name().equals(operatorName)) {
//                return co;
//            }
//        }
//        return null;
//    }
//    
//    public static CriterionOperator getDefaultMultiValueOperator() {
//        return BETWEEN;
//    }
//
//    public static CriterionOperator getDefaultSingleValueOperator() {
//        return EQ;
//    }
//    
//    public String getName(){
//        return name();
//    }
}
