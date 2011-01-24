package developerhaus.repository.mybatis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import developerhaus.domain.User;

public class MybatisRepositoryTest {
	
	@Test
	public void getUser() throws Exception {
		ApplicationContext context = new GenericXmlApplicationContext("/developerhaus/repository/mybatis/applicationContext-mybatis-test.xml");
		
//		UserDao userDao = (UserDao) context.getBean("userDao");
//		User user = userDao.getUser("want813");
//		
//		assertEquals("want813", user.getId());
//		assertEquals("박희희", user.getName());
	}
	
}
