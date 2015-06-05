

# 내용정리 #

## 2.1 UserDaoTest 다시 보기 ##
  * 1장에서 main메소드로 작성한 테스트 코드의 특징
    1. 자바에서 가장 손쉽게 실행 가능한 main() 메소드 이용한다.
    1. 테스트할 대상인 UserDao의 오브젝트를 가져와 메소드 호출한다.
    1. 테스트에 사용할 입력 값(User 오브젝트)을 직접 코드에서 만들어 넣어준다.
    1. 테스트의 결과를 콘솔에 출력해 준다.
    1. 각 단계의 작업이 에러 없이 끝나면 콘솔에 성공 메시지로 출력해준다.
```
public class UserDaoTest {
	
	private static final String USER_ID = "testId";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId(USER_ID);
		user.setName("진광용");
		user.setPassword("1234");

		dao.add(user);
			
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + " 조회 성공");
		
	}
}
```

**웹을 통한 DAO 테스트 방법의 문제점** : 기존에는 DAO를 테스트 하기 위해 서비스 계층, MVC 프리젠테이션 계층까지 모두 작성 후 서버에 배치 한 뒤, 웹 화면을 띄워 폼을 열고, 값을 입력하고 버튼을 눌러 등록하여 확인하였다. 사실 테스트 하고 싶었던 건 UserDao였는데 다른 계층의 코드와 컴포넌트, 심지어 서버의 설정 상태까지 모두 테스트에 영향을 줄 수 있기 때문에 테스트 하는것은 번거롭고, 오류가 있을때 빠르고 정확하게 대응하기 힘들다.

**작은 단위의 테스트** : 관심사의 분리를 적용하여 테스트 하고자 하는 대상에만 집중해서 테스트 한다. UserDaoTest의 테스트를 수행할 때 웹 인터페이스나, MVC 클래스, 서비스 오브젝트 등이 필요 없고, 서버에 배포할 필요도 없다. 이렇게 작은 단위의 코드에 대해 테스트를 수행한 것을 **단위테스트(unit test)** 라고 하며, 개발자가 설계하고 만든 코드가 원래 의도한 대로 동작하는지 개발자 스스로 확인받기 위해서 수행한다.

**자동수행 테스트 코드** : UserDaoTest는 테스트할 데이터가 코드를 통해 제공되고, 테스트 작업 역시 코드를 통해 자동으로 실행한다. 웹 화면에 폼을 띄우고, 매번 User의 등록값을 개발자 스스로 입력하고 버튼을 누르는 등의 지루한 작업을 하지 않고 짧은시간 내에 테스트 할 수 있어 자주 수행할 수 있다.

**지속적인 개선과 점진적인 개발을 위한 테스트** : 조금 난감하긴 해도 자신있게 만들 수 있는 가장 단순한 방법으로 DAO 코드를 만들자마자 검증해주는 테스트 코드를 만들었기 때문에 자신을 가지고 조금씩 코드를 개선해 나갈 수 있었다. 이렇게 검증해서 만든 코드에 대한 확신을 갖음으로써 조금씩 기능을 더 추가하는 점진적인 개발이 가능해진다.

  * UserDaoTest의 문제점
    1. 수동 확인 작업의 번거로움 : UserDaoTest 수행과정과 입력데이터의 준비를 모두 자동으로 진행하도록 만들어졌지만, 테스트의 결과를 확인하는건 수동적(사람이 눈으로 직접 확인)이다.
    1. 실행 작업의 번거로움 : 만약DAO가 수백개가 되면, 각 DAO의 main()메소드 실행 역시 수백번 수행해야 한다. 그래서 main() 메소드를 이용하는 방법보다 좀더 편리하고 체계적으로 테스트를 실행 및 결과를 확인하는 방법이 필요하다.

## 2.2 UserDaoTest 개선 ##
수동 확인 작업의 번거로움의 문제점을 해결하기 위해 테스트 결과를 검증분을 코드로 만들어보자.
기대한 결과와 달리 실패 했을 경우 "테스트 실패"라는 메시지를 출력하며 모든 확인 작업을 통과하면 "테스트 성공"이라고 출력하도록 만들어보자.

**수정 전 테스트 코드**
```
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId() + " 조회 성공");
```

**수정 후 테스트 코드**
```
		if(!user.getName().equals(user2.getName())){
			System.out.println("테스트 실패(name)");
		} else if(!user.getPassword().equals(user2.getPassword())){
			System.out.println("테스트 실패(password)");
		} else {
			System.out.println("조회 테스트 성공");
		}
```

테스트 성공시 '등록 테스트 성공', '조회 테스트 성공' 메세지가 출력될 것이며, 실패시 '테스트 실패(name)' 메세지가 출력되어 결과를 자동으로 검증하도록 되었다.

main() 메소드로 테스트를 수행하는 실행 작업의 번거로움을 해결하기 위해 자바 테스팅 프레임워크라 불리는 JUnit을 사용해 보자.

**JUnit 테스트로 전환** : JUnit은 프레임워크로 제어의 역전을 통해 주도적으로 애플리케이션의 흐름을 제어한다. 따라서 main() 메소드도 필요 없고, 오브젝트로 코드를 만들 필요도 없다.

**테스트 메소드 전환** : 새로 만들 테스트 메소드는 JUnit 프레임워크가 요구하는 조건은 두가지를 따라야 한다.
  1. 메소드가 public으로 선언 돼야 함
  1. 메소드에 @Test라는 애노테이션을 붙여주는 것
```
import org.junit.Test;
...
public class UserDaoTest { 
	

	@Test //JUnit에게 테스트용 메소드임을 알려준다.
	public // Junit 테스트 메소드는 반드시 public으로 선언돼야 한다. 
		void addAndGet() throws ClassNotFoundException, SQLException {
		
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		...
	}
}
```

**검증 코드 전환** : 테스트 결과를 검증하는 if/else 문장을 JUnit이 제공하는 방법을 이용해 전환해보자.
```
   // 기존
   if(!user.getName().equals(user2.getName())){...}
  
   // JUnit
   assertThat(userget1.getName(), is(user1.getName()));
```
assertThat() 메소드는 첫번째 파라미터 값을 뒤에 나오는 매처(matcher)라고 불리는 조건과 비교해서 일치하면 다음으로 넘어가고, 아니면 테스트가 실패하도록 만들어준다.is()는 매처의 일종으로 equals()로 비교해 주는 기능을 가졌다.
JUnit은 assertThat() 등의 테스트 메소드에서 실행이 완료되면 테스트가 성공했다고 인식한다. 굳이 "테스트 성공" 이라는 메시지를 출력할 필요는 없다. JUnit이 테스트를 실행하고 나면 테스트 결과를 다양한 방법으로 알려주기 때문이다.

**JUnit을 적용한 UserDaoTest**
```
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

...
public class UserDaoTest {
	
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		User user = new User();
		user.setId("testId");
		user.setName("진광용");
		user.setPassword("1234");
		
		dao.add(user);
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
}
```

**JUnit 테스트 실행** : JUnit 프레임워크도 자바 코드로 만들어진 프로그램이므로 어디선가 한번은 JUnit 프레임워크를 시작시켜줘야 한다.
```
import org.junit.runner.JUnitCore;

...
	public static void main(String[] args) {
		JUnitCore.main("springbook.user.dao.UserDaoTest");
	}
}
```
성공시 출력 메시지 : 테스트를 실행하는데 걸리는 시간과 테스트 결과, 몇개의 테스트가 실행됐는지 알려준다.

  * JUnit Version 4.7
  * Time : 0.578
  * OK (1 test)

실패시 출력 메시지 : OK대신 FAILURES!!라는 내용이 출력되고, 총수행한 테스트 중 몇개의 테스트가 실패했는지 보여준다. 실패한 테스트는 UserDaoTest 클래스의 addAndGet()메소드이고, 실패한 이유는 "진광용"이라는 값을 원했는데 실행해보니 null이 나왔다는 것임을 알수 있다.

  * Time : 1.094
  * There was 1 failure:
  * 1) addAndGet(springbook.dao.UserDaoTest)
  * java.lang.AssertionError:
  * Expected: is "진광용"
  * got: null
  * ...
  * at springbook.dao.UserDaoTest.main(UserDaoTest.java:36)
  * FAILURES!!!
  * Tests run: 1, Failures: 1
테스트 수행중에 일반예외가 발생한 경우에도 마찬가지로 테스트 수행은 중단되고 테스트는 실패한다.

## 2.3 개발자를 위한 테스팅 프레임워크 JUnit ##
JUnit은 사실상 자바의 표준 테스팅 프레임워크라고 불릴 만큼 폭넓게 사용되고 있다. 스프링 프레임워크 자체도 JUnit 프레임워크를 이용해 테스트를 만들어가며 개발됐다. 스프링 핵심 기능 중 하나인 스프링 테스트 모듈도 JUnit을 이용하므로, 스프링 기능을 익이기 위해서라도 JUnit은 꼭 사용할 줄 알아야 한다.

**JUnit 테스트 실행 방법**
  * IDE**: 사실상의 표준 자바 IDE인 이클립스는 오래전부터 JUnit테스트를 지원하는 기능을 제공하고있다. @Test가 들어있는 테스트 클래스를 선택한 뒤에, 이클립스 run메뉴의 Run As 항목 중에서 JUnit Test를 선택하면 자동으로 실행된다. JUnitCore처럼 main()메소드를 만들지 않아도 된다.**

![http://developerhaus.googlecode.com/files/success.jpg](http://developerhaus.googlecode.com/files/success.jpg)
> 테스트의 총 수행시간, 실행한 테스트 수, 테스트 에러 수, 테스트 실패의 수를 확인할 수 있다. 또한 어떤 클래스를 실행했는지 알 수 있고, 각 테스트 메소드와 클래스의 테스트 수행에 걸린 시간도 보여준다.

![http://developerhaus.googlecode.com/files/fail.jpg](http://developerhaus.googlecode.com/files/fail.jpg)
> 한개의 테스트 메소드가 실패했음을 알 수 있다. 실패한 이유는 뷰의 아래 Failure Trace 항목에 자세히 나와있다. 테스트 코드에서 검증에 실패한 assertTaht()의 위치도 나와있다.

이클립스의 소스트리에서 특정 패키지를 선택하고 컨텍스트 메뉴의 [Run AS ](.md) -> [JUnit Test ](.md)를 실행하면 해당 패키지 아래에 있는 모든 JUnit테스트를 한번에 실행할 수 도 있다.

이렇게 JUnit테스트의 실행과 결과를 확인하는 방법이 매우 간단하고 직관적이며 소스와 긴밀하게 연동돼서 결과를 볼 수 있기 때문에, 개발중에 테스트를 실행하고자 할 때는 이클립스 같은 IDE 지원을 받는것이 가장 편리하다.

  * 빌드 툴**: 빌드를 위해 ANT나 메이븐(Maven) 같은 빌트 툴과 스크립트를 사용하고 있다면, JUnit 플러그인이나 태스크를 이용해 JUnit 테스트를 실행할 수 있다. 테스트 실행 결과는 옵션에 따라서 HTML이나 텍스트 파일의 형태로 보기좋게 만들어진다.**

### 테스트 결과의 일관성 ###
> 테스트가 외부 상태에 따라 성공하기도 하고 실패하기도 하면 않된다. 코드에 변경사항이 없다면 테스트는 항상 동일한 결과를 내야 한다.
> 지금까지 테스트를 실행하면서 매번 UserDaoTest 테스트를 실행하기 전에 DB의 USER 테이블 데이터를 모두 삭제해줘야 하지만, 깜빡 잊고 그냥 테스트 하면 이전 테스트를 실행했을 때 등록했던 사용자 정보와 기본키가 중복되면서 add() 메소드 실행 중 에러가 발생할 것이다. UserDaoTest의 addAndGet() 테스트를 마치고 나면 테스트가 등록한 사용자 정보를 삭제해서, 테스트를 수행하기 이전 상태로 만들어 줘야 한다.

**deleteAll()과 getCount() 추가**
  * deleteAll() : USER 테이블의 모든 레코드를 삭제 해주는 기능
```
	public void deleteAll() throws SQLException{
		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement("delete from users");
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
```

  * getCount() : USER 테이블의 레코드 개수를 돌려준다.
```
	public int getCount() throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("select count(*) from users");
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		rs.close();
		ps.close();
		c.close();
		
		return count;
	}
```

**deleteAll()과 getCount()의 테스트**
deleteAll()과 getCount() 메소드의 기능은 add()와 get()처럼 독립적으로 자동 실행되는 테스트를 만들기가 좀 애매하다. 기존 테스트메소드인 addAndGet() 테스트의 불편한점(USER 테이블 수동 삭제)을 보안하는 방법으로 테스트를 수행해보자.
```
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
	        ...		
		
                dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		User user = new User();
		user.setId("testId");
		user.setName("진광용");
		user.setPassword("1234");
		
		dao.add(user);
		assertThat(dao.getCount(), is(1));
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
```

getCount()에 대해 좀더 꼼꼼한 테스트를 만들어보자. 여러개의 User를 등록해 가면서 getCount()의 결과를 매번 확인해보겠다.

  * 테스트 시나리오
    1. USER 테이블의 데이터를 모두 지우고 getCount()로 레코드 개수가 0임을 확인한다.
    1. 3개의 사용자 정보를 하나씩 추가하면서 매번 getCount()의 결과가 하나씩 증가하는지 확인한다.

테스트를 만들 기 전 User클래스에 한번에 모든 정보를 넣을 수 있도록 초기화가 가능한 생성자를 추가한다.
```
	public User(String id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	public User() {
	}
```

테스트 코드를 수정한 뒤에는 잊지 말고 테스트를 다시 실행해본다. 모든 코드의 수정 후에는 그 수정에 영향을 받을 만한 테스트를 실행하는것을 잊지 말자.
```
        @Test
	public void count() throws Exception {
		
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		User user1 = new User("gyumee", "박성철", "springno1");
		User user2 = new User("leegw700", "이길원", "springno2");
		User user3 = new User("bumjin", "박범진", "springno3");
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
```

![http://developerhaus.googlecode.com/files/test.jpg](http://developerhaus.googlecode.com/files/test.jpg)

위 처럼 두개의 테스트가 실행됐지만 어떤 순서로 실행 될지는 알 수 없다. JUnit은 특정한 테스트 메소드의 실행 순서를 보장해주지 않는다. 모든 테스트는 실행 순서에 상관없이 독립적으로 항상 동일한 결과를 낼 수 있도록 해야 한다.

**addAndGet() 테스트 보완** : get()이 파라미터로 주어진 id에 해당하는 사용자를 가져온 것인지, 그냥 아무거나 가져온것인지 테스트에서 검증하지 못했다. 그래서 User를 하나 더 추가해서 두개의 User를 add()하고, 각 User의 id를 파라미터로 전달해서 get()을 실행하도록 만들어보자.
```
   	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		...
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		User user1 = new User("gyumee", "박성철", "springno1");
		User user2 = new User("leegw700", "이길원", "springno2");
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		// 첫번째 User의 id로 get()을 실행하면서 첫번째 User의 값을 가진 오브젝트를 돌려주는지 확인한다.
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
	
		// 두번째 User에 대해서도 같은 방법으로 검증한다.
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));

	}
```

**get() 예외조건에 대한 테스트** : get()메소드에 전달된 id값에 해당하는 사용자 정보가 없다면 스프링에서 미리 정의한 데이터 액세스 예외 클래스인 EmptyResultDataAccessException예외를 던지는 것으로 처리해 보자. 이번 경우에는 **코드를 만들기 전에 어떻게 테스트 코드로 만들지** 생각해보자. 테스트 진행 중에 특정 예외가 던져지면 테스트가 성공한 것이고, 예외가 던져지지 않고 정상적으로 작업을 마치면 테스트가 실패했다고 판단한다. 즉 assertThat() 메소드로 검증이 불가능하다.
```
        @Test(expected=EmptyResultDataAccessException.class) //-> 테스트 중에 발생할 것으로 기대하는 예외클래스를 지정해준다.
	public void getUserFailure() throws Exception {
		
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id");
	}
```

이 테스트를 실행시키면 어떻게 될까? 당연히 실패한다. get()메소드에서 쿼리 결과의 첫번째 로우(row)를 가져오게 하는 rs.next()를 실행할 때 가져 올 로우가 없다는 SQLException이 발생할 것이다.

**테스를 성공시키기 위한 코드의 수정** : 주어진 id에 해당하는 데이터가 없으면 EmptyResultDataAccessException을 던지는 get()메소드를 만들어 냈다.
```
	public User get(String id) throws ClassNotFoundException, SQLException {
		...

		ResultSet rs = ps.executeQuery();
	
		// User는 null 상태로 초기화해놓는다.
		User user = null; 
		if(rs.next()){
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}

		rs.close();
		ps.close();
		c.close();
		
		//결과가 없으면 User는 null 상태 그대로 일 것이다. 
		//이를 확인해서 예외를 던져준다.
		if(user == null){
			throw new EmptyResultDataAccessException(1);
		}

		return user;
	}
```

새로 만든 getUserFailure() 테스트 뿐만 아니라 기존에 get()으로 사용자 정보를 가져오는 addAndGet() 테스트도 성공했다. 따라서 get()으로 정상적인 결과를 가져오는 경우와 예외적인 경우에 대해 모두 테스트가 성공한 셈이다.최종적으로 모든 테스트가 성공하면, 새로 추가한 기능도 정상적으로 동작하고 기존의 기능에도 영향을 주지 않았다는 확신을 얻을 수 있다.

**포괄적인 테스트** : DAO의 메소드에 대한 포괄적인 테스트를 만들어 두면, 평소에 정상적으로 잘 동작하는 것처럼 보이지만 막상 특별한 상황이 되면 엉뚱하게 동작하는 코드를 만들었을때 효과적으로 대응할 수 있다.

### 테스트가 이끄는 개발 ###
기존 테스트를 만드는 작업 순서는 새로운 기능을 넣기 위해 UserDao 코드를 수정하고, 그런 다음 수정한 코드를 검증하기 위해 테스트를 만드는 순서로 진행했다. 하지만 get() 메소드의 예외 테스트시 테스트를 먼저 만들고 테스트가 실패한 것을 보고 나서 UserDao의 코드에 손을 대기 시작했다. 이런 순서로 개발을 진행하는 구체적인 개발 전략이 실제로 존재하며, 많은 전문적인 개발자가 이런 개발 방법을 적극적으로 사용하고 있다.

**테스트 주도 개발**

만들고자 하는 기능의 내용을 담고 있으면서 코드를 검증도 해줄 수 있도록 테스트 코드를 먼저 만들고, 테스트를 성공하게 해주는 코드를 작성하는 방식의 개발 방법을 테스트 주도 개발(TDD, Test Driven Development)이라고 한다. 테스트보다 코드를 먼저 작성한다고 해서 테스트 우선 개발(TFD, Test First Development)이라고도 한다. "실패한 테스트를 성공시키기 위한 목적이 아닌 코드는 만들지 않는다"는 TDD 기본 원칙이므로, 모든 코드는 빠짐없이 테스트로 검증된 것이라고 볼 수 있다. TDD에서는 테스트를 작성하고 이를 성공시키는 코드를 만드는 작업의 주기를 가능한 짧게 가져가도록 권장한다. 그래서 오류가 발생했을때 쉽게 대응이 가능하도록 한다.

혹시 테스트를 만들고 자주 실행하면 개발이 지연되지 않을까 염려되지만, 그렇지 않다. 테스트는 애플리케이션 코드보다 상대적으로 작성하기 쉬운데다 각 테스트가 독립적이기 때문에, 코드의 양에 비해 작성하는 시간은 얼마 걸리지 않는다. 게다다 테스트 덕분에 오류를 빨리 잡아낼 수 있어서 **전체적인 개발 속도는 오히려 빨라진다.**

### 테스트 코드 개선 ###
UserDaoTest 코드에 다음과 같이 스프링의 애플리케이션 컨텍스트를 만드는 부분과 컨텍스트에서 UserDao를 가져오는 부분이 중복된다. 이 부분을 JUnit이 제공하는 기능을 활용해 메소드로 추출해보겠다.
```
                ApplicationContext context
                   = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
```

**@Before** : JUnit에서 제공하는 애노테이션으로 @Test메소드가 실행되기전에 먼저 실행돼야 하는 메소드를 정의한다. 중복됐던 코드를 넣을 setUp()이라는 메소드를 만들고 테스트 메소드에서 제거한 코드를 넣는다.
```

public class UserDaoTest {
	
	// setUp()메소드에서 만드는 오브젝트를 테스트 메소드에서 사용할 수 있도록 인스턴스 변수로 선언한다.
	private UserDao dao;
	
	@Before
	public void setUp(){
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		this.dao = context.getBean("userDao", UserDao.class);
	}
        ...
}
```

이전과 마찬가지로 테스트가 모두 성공한다. 이를 이해하려면 JUnit프레임워크가 테스트 메소드를 실행하는 과정을 알아야 한다.

  * JUnit이 하나의 테스트 클래스를 가져와 테스트를 수행하는 방식
    1. 테스트 클래스에서 @Test가 붙은 public이고 void형이며 파라미터가 없는 테스트 메소드를 모두 찾는다.
    1. 테스트 클래스의 오브젝트를 하나 만든다.
    1. @Before가 붙은 메소드가 있으면 실행합니다.
    1. @Test가 붙은 메소드를 하나 호출하고 테스트 결과를 저장해 둔다.
    1. @After가 붙은 메소드가 있으면 실행한다.
    1. 나머지 테스트 메소드에 대해 2~5번을 반복한다.
    1. 모든 테스트의 결과를 종합해서 돌려준다.

꼭 한가지 기억해야 할 사항은 각 테스트 메소드를 실행할 때마다 테스트 클래스의 오브젝트를 새로 만든다는 점이다. 테스트 클래스 마다 하나의 오브젝트만 만들어 놓고 사용하는 편이 성능면으로 좋을수 있겠지만, **각 테스트가 서로 영향을 주지 않고 독립적으로 실행됨을 보장해주기 위해** 매번 새로운 오브젝트를 만들게 했다.

**픽스처** : 테스트를 수행하는데 필요한 정보나 오브젝트를 픽스처(fixture)라고 한다. 일반적으로 여러 테스트에서 반복적으로 사용되기 때문에 @Befroe 메소드를 이용해 생성해두면 편리하다.
```
public class UserDaoTest {
	
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp(){
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		this.dao = context.getBean("userDao", UserDao.class);
		
		this.user1 = new User("gyumee", "박성철", "springno1");
		this.user2 = new User("leegw700", "이길원", "springno2");
		this.user3 = new User("bumjin", "박범진", "springno3");
		
	}
      ...
}
```

## 2.4 스프링 테스트 적용 ##
@Before 메소드가 테스트 메소드 개수 만큼 반복되기 때문에 애플리케이션이 테스트 메소드 만큼 만들어 진다. 테스트는 가능한 독립적으로 매번 새로운 오브젝트를 만들어서 사용하는 것이 원칙이지만 애플리케이션 컨텍스트처럼 생성에 많은 시간과 자원이 소모되는 경우에는 테스트 전체가 공유하는 오브젝트를 만들기도 한다. JUnit 테스트 클래스에서 전체에 딱 한번만 실행되는 @BeforeClass 스태틱 메소드를 지원하지만 스프링에서 제공하는 애플리케이션 컨텍스트 테스트 지원 기능을 하용하는 것이 편리하다.

### 테스트를 위한 애플리케이션 컨텍스트 관리 ###
스프링은 JUnit을 이용하는 테스트 컨텍스트 플레임워크를 제공한다. 간단한 애노테이션 설정만으로 테스트에서 필요로 하는 애플리케이션 컨텍스트를 만들어서 모든 테스트가 공유하게 할수 있다.

**스프링 테스트 컨텍스트 프레임워크 적용**

@Before메소드에서 애플리케이션 컨텍스트를 제거 후 ApplicationContext 타입의 인스턴스 변수를 선언하고, 스프링이 제공하는 @Autowired 애노테이션을 붙여준다. 클래스 레벨에 @RunWith와 @ContextConfiguration 애노테이션을 아래와 같이 추가해 준다.

```
@RunWith(SpringJUnit4ClassRunner.class) //스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장 기능 지정
@ContextConfiguration(locations="/applicationContext.xml") //테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
public class UserDaoTest {
	
	@Autowired
	private ApplicationContext context; // 테스트 오브젝트가 만들어지고 나면 스프링 테스트 컨텍스트에 의해 자동으로 값이 주입된다
	
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp(){
		this.dao = context.getBean("userDao", UserDao.class);
		
		...
	}
        ...
}
```

**테스트 메소드의 컨텍스트 공유** : setUp() 메소드에서 다음 두줄을 추가하고 테스트를 다시 실행해보자.
```
        @Before
	public void setUp(){
		System.out.println(this.context);
		System.out.println(this);
```


setUp() 메소드는 @Before가 붙어 있으니 매 테스트 메소드가 실행되기 전에 한번씩 실행된다.
실행 결과는 다음과 같다.
```
org.springframework.context.support.GenericApplicationContext@1e4457d:
sample.UserDaoTest@fcfa52
org.springframework.context.support.GenericApplicationContext@1e4457d:
sample.UserDaoTest@14e8cee
org.springframework.context.support.GenericApplicationContext@1e4457d:
sample.UserDaoTest@cf66b
```

출력된 context와 this의 오브젝트 값을 살펴보면, context는 세번 모두 동일하다. 따라서 하나의 애플리케이션 컨텍스트가 만들어져 모든 테스트 메소드에서 사용되고 있음을 알수 있다. 반면 UserDaoTest의 오브젝트는 매번 주소값이 다르다. 실행할 때마다 새로운 테스트 오브젝트를 만들기 때문이다.

**테스트 클래스의 컨텍스트 공유** : 여러개의 테스트 클래스가 있는데 모두 같은 설정파일을 가진 애플리케이션 컨택스트를 사용한다면, 스프링은 테스트 클래스 사이에서도 애플리케이션 컨텍스트를 공유하게 해준다.
```
  @RunWith(SpringJUnit4ClassRunner.class)
  @ContextConfiguration(locations="/applicationContext.xml")
  public class UserDaoTest {...}

  @RunWith(SpringJUnit4ClassRunner.class)
  @ContextConfiguration(locations="/applicationContext.xml")
  public class GroupDaoTest {...}
```

**@Autowired** : @Autowired가 붙은 인스턴스 변수가 있으면, 테스트 컨텍스트 프레임워크는 변수타입과 일치하는 빈을 찾고, 타입이 일치하는 빈이 없거나 두개이상 존재하면 이름으로 찾는다. 이때 찾을 수 없으면 예외가 발생한다.

스프링 애플리케이션 컨텍스트는 초기화 할때 자기 자신도 빈으로 등록해 applicationContext.xml 파일에 정의되지 않았어도 DI할수 있다.
@Autowired를 이용해 애플리케이션 컨텍스트를 갖오 있는 빈을 DI 받을 수 있다면 컨텍스트를 가져와 getBean()을 사용할 필요 없어 UserDao 빈을 직접 DI 받을 수도 있을 것이다.
```
    public class UserDaoTest {
	
	@Autowired
	private UserDao dao;
```

**테스트 코드에 대한 DI** : UserDao는 DI 컨테이너가 의존관계 주입을 위한 수정자 메소드를 만들었다. 테스트를 위한 DataSource를 사용할 때 스프링 DI에서가 아니라 직접 만들어 개발할 수 있다.
```
		// 테스트메소드에서 애플리케이션 컨텍스트의 구성이나 상태를 변경한다는 것을
@DirtiesContext // 테스트 컨텍스트 프레임워크에 알려준다.
public class UserDaoTest {
	
	@Autowired
	private UserDao dao;
	
	@Before
	public void setUp(){
		...
		
		// 테스트에서 UserDao가 사용할 DataSource 오브젝트를 직접 생성한다.
		DataSource dataSource = new SingleConnectionDataSource(
					"jdbc:mysql://localhost/testdb","spring", "book", true);
```

XML 설정파일을 수정하지 않고 테스트 코드를 통해 오브젝트를 재구성 할 수 있었지만, 애플리케이션 컨텍스트 정보를 강제로 변경해 다른 부분까지 영향을 미친다. 그래서 @DirtiesContext라는 애노테이션을 추가하여 새로운 애플리케이션 컨텍스트를 만들어서 다른 테스트에 영향을 미치지 않게 했다.

  * 메소드 레벨의 @DirtiesContext : @DirtiesContext를 메소드에 붙여주면 해당 메소드의 실행이 끝나고 나면 이후에 진행되는 테스트를 위한 변경된 애플리케이션 컨텍스트는 폐기되고 새로운 애플리케이션 컨텍스트가 만들어진다.


**테스트를 위한 별도의 DI 설정**
테스트를 위한 별도의 설정파일을 만들어 사용할수 있다. 기존의 applicationContext.xml을 복사해서 test-applicationContext.xml이라고 만들어 아래와 같이 테스트DB 설정을 해서 사용할 수 있따.
```
	<bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
		<property name="driverClass" value="com.mysql.jdbc.Driver"/>
		<property name="url" value="jdbc:mysql://localhost/testdb"/>
		<property name="username" value="spring"/>
		<property name="password" value="book"/>
	</bean>
```

UserDaoTest의 @ContextConfiguration애노테이션의 locations 엘리먼트를 아래와 같이 변경한다.
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")

public class UserDaoTest {
```

**컨테이너 없는 DI 테스트 :** UserDao나 DataSource 구현 클래스 어디에도 스프링의 API를 사용하거나 애플리케이션 컨텍스트에 의존하는 코드는 존재하지 않는다. 원한다면 테스트코드에서 직접 오브젝트를 만들고 DI해서 사용해도 된다.
```
public class UserDaoTestNoneSpring {
	
	UserDao dao; // @Autowired가 없다.
        ...	

	@Before
	public void setUp(){

		this.dao = new UserDao();
	
		// 오브젝트생성, 관계설정 등을 모두 직접 해준다.
		DataSource dataSource
			= new SingleConnectionDataSource("jdbc:mysql://localhost/testdb"
											,"spring", "book", true);
		dao.setDataSource(dataSource);
         ...
      }
```

  * 침투적 기술과 비침투적 기술 : 침투적 기술은 기술을 적용했을 때 애플리케이션 코드에 기술 관련 API가 등장하거나, 특정 인터페이스나 클래스를 사용하도록 강제하는 기술을 말한다. 침투적 기술을 사용하면 애플리케이션 코드가 해당 기술에 종속되는 결과를 가져온다. 반면에 비침투적인 기술은 애플리케이션 로직을 담을 코드에 아무런 영향을 주지 않고 적용이 가능하다. 따라서 기술에 종속적이지 않은 순수한 코드를 유지할수 있게 해준다. 스프링은 이런 비침투적인 기술의 대표적인 예다. 그래서 스프링 컨테이너 없는 DI 테스트도 가능한 것이다.

## 2.5 학습 테스트로 배우는 스프링 ##
자신이 만들지 않은 프레임워크나 다른 개발팀에서 만들어서 제공한 라이브러리 등에 대해서도 테스트를 작성해야 하는 것을 **학습 테스트(learning test)**라고 한다. 테스트를 만들려고 하는 기술이나 기능에 대해 얼마나 제대로 이해하고 있는지, 그 사용 방법을 검증하려는게 목적이다.

### 학습 테스트의 장점 ###
  * 다양한 조건에 따른 기능을 손쉽게 확인 해 볼수 있다.
> > : 자동화된 테스트 코드로 만들어지기 때문에 다양한 조건에 따라 기능이 어떻게 동작하는지 빠르게 확인할 수 이다.

  * 학습 테스트 코드를 개발 중에 참고할 수 있다.
> > : 다양한 기능과 조건에 대한 테스트 코드를 개별적으로 만들고 남겨둠으로써, 아직 익숙하지 않은 기술을 사용해야 하는 개발자에게 좋은 참고 자료가 된다.

  * 프레임워크나 제품을 업그레이드 할 때 호환성 검증을 도와준다.
> > : 새로운 버전으로 업그레이드를 할 때 API 사용법에 미묘한 변화가 생긴다거나, 문제가 있을때 기존 학습테스트에 새로운 버전을 적용함으로써 호환성 검증을 할 수 있다.

  * 테스트 작성에 대한 좋은 훈련이 된다.
> > : 학습 테스트는 한두가지 간단한 기능에마나 초점을 맞춰 대체로 단순해 애플리케이션 개발 중에 작성 하는 테스트보다는 한결 작성하기가 수월하여 부담이 적기 때문에 학습테스트를 테스트 작성의 훈련 기회로 삼는 것도 좋다

  * 새로운 기술을 공부하는 과정이 즐거워진다.
> > : 책이나 래퍼런스 문서 등을 그저 읽기만 하는 공부는 쉽게 지루해진다. 그에 비해 테스트 코드를 만들면서 하는 학습은 결과를 바로바로 확인할 수 있어, 흥미롭고 재미있다.

### 학습 테스트 예제 ###

**JUnit 테스트 오브젝트 테스트** : 테스트 메소드를 수행할 때마다 새로운 오브젝트를 만드는 JUnit에 대한 학습 테스트를 만들어 보자.
  * 테스트 방법
    1. 새로운 테스트 클래스를 만들고 적당한 이름으로 세 개의 테스트 메소드 추가
    1. 테스트 클래스 자신의 타입을 스태틱 변수를 하나 선언
    1. 매 테스트 메소드에서 현재 스태틱 변수에 담긴 오브젝트와 자신을 비교해서 같지 않다는 사실을 확인
    1. 현재 오브젝트를 그 스태틱 변수에 저장

```
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JUnitTest {
	
	static JUnitTest testObject;
	
	@Test public void test1(){
		assertThat(this, is(not(sameInstance(testObject))));
		testObject = this;
	}
	
	@Test public void test2(){
		assertThat(this, is(not(sameInstance(testObject))));
		testObject = this;
	}

	@Test public void test3(){
		assertThat(this, is(not(sameInstance(testObject))));
		testObject = this;
	}
}
```

> assertThat()에 사용하는 몇가지 매처가 추가됐다. not()은 뒤에 나오는 결과를 부정하는 매처다. is()는 equals() 비교를 해서 같은명 성공이지만 is(not())은 반대로 같지 않아야 성공한다. sameInstance()는 실제로 같은 오브젝트인지 비교한다.

첫번째와 세번째 오브젝트가 같은 경우 검증이 않되므로 방법을 조금 개선해 보자.
```
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class JUnitTest {
	
	static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
	
	@Test public void test1(){
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
	}
	
	@Test public void test2(){
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
	}

	@Test public void test3(){
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
	}
}
```

컬렉션의 원소인지 검사하는 hassItem() 매처를 사용해 오브젝트 중복 여부를 확인했다.

이 학습은 JUnit의 사용 방법을 익히기 위해 코드를 만든 것은 아니었다. 하지만 JUnit의 특성을 분명히 이해할 수 있게 됐고, 또 테스트를 만드는 방법에 대한 공부가 되니 일석이조다.

**스프링 테스트 컨텍스트 테스트**
> 스프링의 테스트용 애플리케이션 컨텍스트는 테스트 개수에 상관없이 한개만 만들어진다. 정말 그런지 검증하는 학습 테스트를 만들어보자.

```
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.either;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("junit.xml")
public class JUnitTest {
         // 테스트 컨텍스트가 매번 주입해주는 애플리케이션 컨텍스트는 항상 같은 오브젝트인지 테스트로 확인해본다.
	@Autowired ApplicationContext context;
	
	static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
	static ApplicationContext contextObject = null;
	
	@Test public void test1() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertThat(contextObject == null || contextObject == this.context, is(true));
		contextObject = this.context;
	}
	
	@Test public void test2() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	
	@Test public void test3() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertThat(contextObject, either(is(nullValue())).or(is(this.contextObject)));
		contextObject = this.context;
	}
}
```

먼저 context를 저장해둘 스태틱 변수인 contextObject가 null인지 확인한다.(첫번째 테스트 일 경우) 다음부터는 저장된 contextObject가 현재의 context와 같은지 비교한다. 한번이라도 다른 오브젝트가 나오면 테스트는 실패한다. 각 테스트 메소드마다 다른코드로 검증 하지만 모두 같은 내용을 검증한다.

### 버그 테스트 ###
> 버그 테스트란 코드에 오류가 있을 때 그 오류를 가장 잘 드러내줄 수 있는 테스트를 말한다. 버그 테스트는 일단 실패하도록 만들어야 한다. 버그가 원인이 되서 테스트가 실패하는 코드를 만들어 버그 테스트가 성공할 수 있도록 애플리케이션 코드를 수정한다. 테스트가 성공하면 버그는 해결 된 것이다.

  * 버그 테스트 필요성과 장점
    1. 테스트와 완성도를 높여준다.
> > > : 기존 테스트에서 검증하지 못했기 때문에 오류가 발생한 것으로 이에 대해 테스트를 만들면 불충분했던 테스트를 보완해 준다.
    1. 버그의 내용을 명확하게 분석하게 해준다.
> > > : 버그가 있을 때 그것을 테스트로 만들어서 실패하게 하려면 어떤 이유 때문에 문제가 생겼는지 명확히 알아야 한다. 따라서 버그를 좀 더 효과적으로 분석할 수 있다.
    1. 기술적인 문제를 해결하는 데 도움이 된다.
> > > : 버그의 원인을 파악하기 힘들 때 버그가 발생하는 가장 단순한 코드와 그에 대한 버그테스트를 만들어보면 도움이 된다.