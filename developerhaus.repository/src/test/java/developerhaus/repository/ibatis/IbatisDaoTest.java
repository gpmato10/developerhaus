package developerhaus.repository.ibatis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import developerhaus.domain.User;
import developerhaus.repository.ibatis.dao.UserDao;

public class IbatisDaoTest {
	
	@Test
	public void getUser() throws Exception {
		ApplicationContext context = new GenericXmlApplicationContext("/developerhaus/repository/ibatis/applicationContext-ibatis-test.xml");
		
		UserDao userDao = (UserDao) context.getBean("userDao");
		User user = userDao.getUser("want813");
		
		assertEquals("want813", user.getId());
		assertEquals("박희희", user.getName());
	}
	
}
