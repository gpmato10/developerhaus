package developerhaus.repository.jdbc.strategy;


/**
 * Repository에서 테이블의 메타정보를 제공하도록 강제하는 인테페이스
 * @author jin
 *
 */
public interface TableStrategyAware {
	
	TableStrategy getTableStrategy();
	
	TableStrategy getTableStrategy(String alias);
	
}
