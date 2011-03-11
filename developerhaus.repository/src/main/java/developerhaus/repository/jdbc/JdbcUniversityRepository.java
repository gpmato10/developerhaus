package developerhaus.repository.jdbc;

import static developerhaus.repository.jdbc.RepositoryUtils.addAliasToColumn;

import java.util.List;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;
import developerhaus.repository.jdbc.strategy.TableStrategy;

public class JdbcUniversityRepository implements UniversityRepository, TableStrategyAware {
	
	public final static String TABLE_NAME = "UNIVERSITY";
	public final static String ALIAS = "univ";
	
	public final static String UNIVERSITY_ID = addAliasToColumn(ALIAS, "id"); 		// 대학교ID
	public final static String UNIVERSITY_NAME = addAliasToColumn(ALIAS, "name"); 	// 대학교명
	
	
	@Override
	public UniversityRepository get(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<UniversityRepository> list(Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean update(UniversityRepository domain) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TableStrategy getTableStrategy() {
	
		return new DefaultTableStrategy(TABLE_NAME, ALIAS)
					.setAllColumn(UNIVERSITY_ID, UNIVERSITY_NAME);
	}
	@Override
	public TableStrategy getTableStrategy(String alias) {
		// TODO Auto-generated method stub
		return null;
	}

}
