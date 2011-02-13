package developerhaus.repository.mybatis;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import developerhaus.domain.User;

public class MybatisRepositoryTest {
	
	@Test
	public void getUser() throws Exception {
		ApplicationContext context = new GenericXmlApplicationContext("/developerhaus/repository/mybatis/applicationContext-mybatis-test.xml");
		
		System.out.println("context:"+context);
		
		MybatisUserRepository userRepository = (MybatisUserRepository) context.getBean("mybatisUserRepository");
		
		assertNotNull(userRepository);
		
		System.out.println("userRepository:"+userRepository);
		User user = userRepository.get(2);
		assertNotNull(user);
		System.out.println("user:"+user);
		assertEquals(user.getId(), "want815");
		System.out.println("user.getId:"+user.getId());
//		UserDao userDao = (UserDao) context.getBean("userDao");
//		User user = userDao.getUser("want813");
//		
//		assertEquals("want813", user.getId());
//		assertEquals("박희희", user.getName());
	}
	
}
