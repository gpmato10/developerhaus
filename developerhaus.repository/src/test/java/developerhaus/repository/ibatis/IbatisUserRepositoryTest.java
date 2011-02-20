package developerhaus.repository.ibatis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import developerhaus.domain.User;
 


public class IbatisUserRepositoryTest {
	
	IbatisUserRepository repository;

	@Before
	public void setting() throws Exception {
		ApplicationContext context = new GenericXmlApplicationContext("/developerhaus/repository/ibatis/applicationContext-ibatis-test.xml");
		repository = (IbatisUserRepository) context.getBean("ibatisUserRepository");
	}
	
	@Test
	public void get() throws Exception {	
				
		User user = repository.get(2);	
		
		assertEquals(user.getId(), "want815");	
		assertNotNull(user);
	}
}
