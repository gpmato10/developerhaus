package developerhaus.repository.jdbc;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import static developerhaus.repository.jdbc.RepositoryUtils.*;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.criteria.CriterionOperator;
import developerhaus.repository.criteria.DefaultCriteria;
import developerhaus.repository.criteria.SingleValueCriterion;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStrategyAware;
import developerhaus.repository.jdbc.strategy.TableStrategy;

public class JdbcStudentRepository implements StudentRepository, TableStrategyAware{
	
	public final static String TABLE_NAME = "STUDENT";
	public final static String ALIAS = "stu";
	
	// DB 의존성을 제거하기 위해 DB컬럼명 변화에 상관없이 대표되는 컬럼명 정의
	public final static String STUDENT_NUMBER = addAliasToColumn(ALIAS,	"num");  	// 학번
	public final static String STUDENT_NAME = addAliasToColumn(ALIAS, "name");  	// 이름
	public final static String YEAR = addAliasToColumn(ALIAS, "year");  			// 학년
	public final static String DEPARTMENT  = addAliasToColumn(ALIAS, "dept"); 		// 학과
	public final static String UNIVERSITY_ID = addAliasToColumn(ALIAS, "univId"); 	// 대학교ID
 
	@Override
	public Student get(String id) {
		
//		SELECT  stu.num, stu.name, stu.year, stu.dept, stu.univId FROM  STUDENT stu  WHERE stu.num = :stu.num, 
		
		Criteria criteria = new DefaultCriteria();
		criteria.add(new SingleValueCriterion<CriterionOperator, String>(
								JdbcStudentRepository.STUDENT_NUMBER, 
								CriterionOperator.EQ, 
								id)
					);
		
		MapSqlParameterSource msps;
		
//		SqlBuilder sqlBuilder = new SqlBuilder(getTableStrategy(), criteria);
		SqlBuilder sqlBuilder = new SqlBuilder(this, criteria);
		String sql = sqlBuilder.selectAll().from().where().build();
		System.out.println(sql);
		
//		이렇게 구현해 주세요.
//		Student student = JDBC구현체.get(sql, Student.class, msps);
		
		
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
					.setAllColumn(STUDENT_NUMBER, STUDENT_NAME, YEAR, DEPARTMENT, UNIVERSITY_ID);
	}

	@Override
	public TableStrategy getTableStrategy(String alias) {
		// TODO Auto-generated method stub
		return null;
	}
}
