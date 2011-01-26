package developerhaus.repository.jdbc;

import java.util.List;

import developerhaus.domain.Student;
import developerhaus.repository.api.criteria.Criteria;
import developerhaus.repository.jdbc.strategy.DefaultTableStrategy;
import developerhaus.repository.jdbc.strategy.TableStategyAware;
import developerhaus.repository.jdbc.strategy.TableStrategy;

public class JdbcStudentRepository implements StudentRepository, TableStategyAware{

	@Override
	public Student get(String id) {
		
		String sql =  new SqlBuilder(getTableStrategy())
						.selectAll()
						.from()
						.build();
		
//		스프링의 JDBC 클래스를 이용하여 쿼리 수행
		
		return null;
	}

	@Override
	public List<Student> list(Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Student domain) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getGrade(int year) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
//	TODO : 요건처리1의 데이터베이시의 의존성을 제거해야 한다.(일단 테이블명, 컬럼명을 직접 매핑하여 설정) 
	public TableStrategy getTableStrategy() {

		return new DefaultTableStrategy("STUDENT")
					.setAllColumn("SNO", "SNAME", "YEAR", "DEPT");
	}


}
