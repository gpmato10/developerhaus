package developerhaus.repository.jdbc;

import developerhaus.domain.Student;
import developerhaus.repository.api.GenericRepository;

public interface StudentRepository extends GenericRepository<Student, String>{
	
	
	/**
	 * 각 학년의 학점 반환 
	 * @param year : 학년
	 * @return
	 */
	float getGrade(int year);
	
	
}
