package developerhaus.repository.jdbc;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import developerhaus.domain.Student;


public class JdbcStudentRepositoryTest {
	
	@Test
	public void interfaceTest() throws Exception {

		StudentRepository repository = new JdbcStudentRepository();
		
		List<Student> studentList = repository.list(null);
		assertNull(studentList);
	}
}
