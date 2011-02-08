package developerhaus.repository.jdbc;

import java.util.List;

import static developerhaus.repository.jdbc.RepositoryUtils.*;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.jdbc.criteria.CriterionOperator;
import developerhaus.repository.jdbc.criteria.DefaultCriteria;
import developerhaus.repository.jdbc.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStategyAware;
import developerhaus.repository.jdbc.strategy.TableStrategy;

public class JdbcStudentRepository implements StudentRepository, TableStategyAware{
	
	public final static String TABLE_NAME = "STUDENT";
	public final static String ALIAS = "stu";
	
	// DB 의존성을 제거하기 위해 DB컬럼명 변화에 상관없이 대표되는 컬럼명 정의
	// TODO : 쿼리결과 도메인 속성명과 매핑하기 위한 정책 수립(Spring JDBC 붙인 후 다시 생각)
	public final static String STUDENT_NUMBER = addAliasToColumn(ALIAS,	"sno");  	// 학번
	public final static String STUDENT_NAME = addAliasToColumn(ALIAS, "sname");  	// 이름
	public final static String YEAR = addAliasToColumn(ALIAS, "year");  			// 학년
	public final static String DEPARTMENT  = addAliasToColumn(ALIAS, "dept"); 		// 학과

	@Override
	public Student get(String id) {
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion(
								JdbcStudentRepository.STUDENT_NUMBER, 
								CriterionOperator.EQ, 
								id)
					);
		SqlBuilder sqlBuilder = new SqlBuilder(getTableStrategy(), criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		
		System.out.println("getByID : " + sql);
		
//		스프링의 JDBC 클래스를 이용하여 쿼리 수행
		return null;
	}

	@Override
	public List<Student> list(Criteria criteria) {
		return null;
	}

	@Override
	public boolean update(Student student) {
		return false;
	}

	@Override
	public float getGrade(int year) {
		return 0;
	}

	@Override
	public TableStrategy getTableStrategy() {

		return new DefaultTableStrategy(TABLE_NAME, ALIAS)
					.setAllColumn(STUDENT_NUMBER, STUDENT_NAME, YEAR, DEPARTMENT);
	}
}
