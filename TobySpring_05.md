

# 정리 #
5장은 지금 까지 만든 DAO에 트랜잭션을 적용하며 스프링의 추상화 방법과 어떻게 일관되게 사용할 수 있게 하는지 살펴보는 것을 목적으로 한다.

## 5.1 사용자 레벨 관리 기능 추가 ##
현재 DAO는 CRUD라 불리는 기초 작업만 가능하다. 여기에 정해진 조건에 따라 사용자의 레벨을 주기적으로 변경하는 간단한 기능을 추가한다.

조건은 책 페이지 320을 참조 한다

### 5.1.1 필드추가 ###
User클래스에 사용자 레벨을 저장할 필드를 추가해야 한다.

```
package springbook.user.domain;

public enum Level {
	BASIC(1), SILVER(2), GOLD(3);

	private final int value;
		
	Level(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}
	
	public static Level valueOf(int value) {
		switch(value) {
		case 1: return BASIC;
		case 2: return SILVER;
		case 3: return GOLD;
		default: throw new AssertionError("Unknown value: " + value);
		}
	}
}

```
여기서 사용한 방법은 이늄(enum)을 이용한 방법이다. - 참조 : http://mwultong.blogspot.com/2006/10/java-enum-enumeration.html

단지 int형의 상수로 정의해 사용 하면 다른종류의 정보를 넣는 실수나 범위를 벗어난 값을 넣었을 경우에 체크를 하지 못하는 단점이 있다. 반대로 enum의 장점이 된다.

이렇게 만든 Level 타입과 로그인 횟수, 추천수를 User.java에 추가한다.

UserDaoTest도 테스트 픽스처로 만든 것에 추가된 필드를 추가해야 한다.
이에 맞게 User.java에 IDE가 지원하는 생성자 자동추가 기능을 사용하여 만든다.

```
package springbook.user.domain;
public class User {

...
Level level;
int login;
int recommend;

...
public User(String id, String name, String password, Level level,
			int login, int recommend) {
	this.id = id;
	this.name = name;
	this.password = password;
	this.level = level;
	this.login = login;
	this.recommend = recommend;
}

...
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public int getLogin() {
		return login;
	}
	public void setLogin(int login) {
		this.login = login;
	}
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

```

UserDaoTest에 checkSameUser() 메소드에 새로운 필드를 비교하는 코드를 추가한다.

```
...
@Before
public void setUp() {
	this.user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
	this.user2 = new User("leegw700", "이길원", "springno2", Level.SILVER, 55, 10);
	this.user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);
}
@Test 
public void andAndGet() {		
	dao.deleteAll();
	assertThat(dao.getCount(), is(0));

	dao.add(user1);
	dao.add(user2);
	assertThat(dao.getCount(), is(2));
		
	User userget1 = dao.get(user1.getId());
	checkSameUser(userget1, user1);
		
	User userget2 = dao.get(user2.getId());
	checkSameUser(userget2, user2);
}

...
private void checkSameUser(User user1, User user2) {
	assertThat(user1.getId(), is(user2.getId()));
	assertThat(user1.getName(), is(user2.getName()));
	assertThat(user1.getPassword(), is(user2.getPassword()));
	assertThat(user1.getLevel(), is(user2.getLevel()));
	assertThat(user1.getLogin(), is(user2.getLogin()));
	assertThat(user1.getRecommend(), is(user2.getRecommend()));
}

```

아직 테스트는 실패한다. UserDaoJdbc를 수정할 차례다.
add() 메소드의 SQL과
User 오브젝트 매핑용 콜백인 userMapper에 추가된 필드를 넣는다.

```

...
private RowMapper<User> userMapper = 
		new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setLevel(Level.valueOf(rs.getInt("level")));
				user.setLogin(rs.getInt("login"));
				user.setRecommend(rs.getInt("recommend"));
				return user;
			}
		};
public void add(User user) {
		this.jdbcTemplate.update(
			"insert into users(id, name, password, level, login, recommend) " +
			"values(?,?,?,?,?,?)", 
			user.getId(), user.getName(), user.getPassword(), 
			user.getLevel().intValue(), user.getLogin(), user.getRecommend());
	}
...
```
책에서는 userMapper 에서 loign 오타를 일부로 넣어 정확한 BadSqlGrammarException 을 발생시켰다.
```
org.springframework.jdbc.BadSqlGrammarException: PreparedStatementCallback;
bad SQL grammar [select * from users where id = ?]; 
nested exception is java.sql.SQLException: Column 'loign' not found.
```
테스트를 돌려보면 이와같이 정확한 에러이유를 보여주는 것을 볼 수 있다. 저는 이부분에서 무심코 login으로 하여 에러가 나지 않았으나 뒷부분인 update()를 만들때 쿼리 문에서 level 을 lebel로 하여 에러가 났었다.
```
org.springframework.jdbc.BadSqlGrammarException: PreparedStatementCallback; 
bad SQL grammar [update users set name = ?,password = ?, lebel = ?, login = ?, recommend = ? where id = ?]; 
nested exception is com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column 'lebel' in 'field list'
```
이와 같은 에러메세지를 통해 어느 부분이 잘못 되었는지 바로 알 수 있다.

### 5.1.2  사용자 수정 기능 추가 ###
사용자 정보는 ID값을 제외한 나머지는 수정될 수 있다.
기존 테스트에 없던 update() 기능이 필요하다. 먼저 테스트 부터 작성해 본다.

```
@Test
	public void update(){
		dao.deleteAll();
		dao.add(user1);
		
		user1.setName("오민규");
		user1.setPassword("springno6");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
	}
```
UserDao interface와 이를 구체화한 UserDaoJdbc에 update()메소드를 추가해 준다.
```
public interface UserDao {
        ...
	public void update(User user);
}
```
```
//UserDaoJdbc
...
public void update(User user) {
		this.jdbcTemplate.update("update users set name = ?,password = ?, lebel = ?, login = ?, "+
					"recommend = ? where id = ?",user.getName(),user.getPassword(),
					user.getLevel().intValue(),user.getLogin(),user.getRecommend(),user.getId());		
	}
```
이렇게 돌려보면 결과가 잘 나온다. 그러나 꼼꼼한 개발자라면 불만을 가지고 의심스럽게 다시 봐야 한다고 한다. 아무래도 꼼꼼한 개발자가 아닌듯 싶다.

지금 테스트의 허점은 목표로한 것만 바뀐것인지 목표로 하지 않은것도 바뀐것인지 확인할 길이 없는점이다.
확인하는 방법으로 2가지가 있다. 리턴값이 1인지 확인하는 방법과 테스트 자체를 바꿔 2명을 등록하고 하나만 수정하여 두 정보를 비교하는 것이다. 책에서는 두번째 방법을 사용한다.
```
@Test
	public void update(){
		dao.deleteAll();
		dao.add(user1);
		dao.add(user2);		//수정하지 않을 사용자
		
		user1.setName("오민규");
		user1.setPassword("springno6");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);		
	}
```
이렇게 바꾸고 sql문의 조건절을 빼고 돌려보면 에러가 발생한다.

### 5.1.3 UserService.upgradeLebels() ###
로직을 담을 Service가 등장한다. Dao는 데이터를 가져오고 조작하는 곳이다.
지금까지 한대로 높은 응집도와 낮은 결합도를 위해 Service 역시 DI를 적용 해야한다. - UserDao 가 변경되도 UserService는 영향받지 않아야 한다.

우선 UserService클래스를 만들고
```
package springbook.user.service;
...
public class UserService {
	UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
```
스프링 설정파일에 빈을 추가한다.
```
        <bean id="userService" class="springbook.user.service.UserService">
		<property name="userDao" ref="userDao"></property>
	</bean>
	
	<bean id="userDao" class="springbook.user.dao.UserDaoJdbc">
		<property name="dataSource" ref="dataSource" />
	</bean> 
```

이번엔 사용자 관리 로직인 upgradeLevels() 메소드를 만들고 나중에 테스트를 만들자

```
package springbook.user.service;

import java.util.List;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
public class UserService {
	UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void upgradeLevels(){
		List<User> users = userDao.getAll();
		for(User user : users){
			Boolean changed = null;
			if(user.getLevel() == Level.BASIC && user.getLogin() >=50){
				user.setLevel(Level.SILVER);
				changed = true;
			} else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30){
				user.setLevel(Level.GOLD);
				changed = true;
			} else if (user.getLevel() == Level.GOLD){ 
				changed = false; 
			} else {
				changed = false;
			}
			if(changed){
				userDao.update(user);
			}
		}
	}
}
```

upgradeLevels() 테스트를 만들어야 한다. 사용자 레벨이 세가지이고 GOLD는 변하지 않으니 적어도 다섯 가지 경우의 수를 살펴서 만든다.
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	@Autowired 
	UserDao userDao;
	
	List<User> users;	// test fixture
	@Before
	public void setUp() {
// Arrays.asList - 배열을 리스트로
		users = Arrays.asList(
			new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
			new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
			new User("erwins", "신승한", "p3", Level.SILVER, 60, 29),
			new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30),
			new User("green", "오민규", "p5", Level.GOLD, 100, 100)
		);	
	}
	
	@Test
	public void upgradeLevels(){
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);				
	}

	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(),is(expectedLevel));		
	}
}
```

비지니스 로직에서 한가지 빠진 부분이 있다. 최초 가입자는 BASIC 레벨이 되야 한다.
  * Jdbc에 넣는 방법은?
  * User에서 아예 BASIC로 초기하는?
  * 역시 로직은 service에 담겨야 한다.
  * 번외로 DB컬럼에 기본값으로 주면 안되는 것인가?

이번엔 테스트 부터 만들기로 한다.
두종류 케이스를 만들면 되는데 레벨이 미리 정해진 경우와 비어있는 경우 두가지다. 각각모두 add()를 호출하려 결과를 확인한다. 확인하는 방법으로는 UserDao의 get 메소드를 이용하여 DB에 저장된 User정보를 확인하는 방법을 사용해 보자.
```
@Test
	public void add(){
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
```

UserService에 add() 메소드를 추가한다.
```
public void add(User user) {
	if(user.getLevel() == null) user.setLevel(Level.BASIC);
	userDao.add(user);		
}
```

### 5.1.5 코드개선 ###
깔끔한 코드를 추구하는 스프링 사용자 답게 작성된 코드를 살펴보며 지금까지 공부한 것을 다시한번 질문해보자.

  * 코드에 중복된 부분은?
  * 코드가 무엇을 하는지 이해하기 어려운가?(응집도 인듯)
  * 코드가 제자리인가?(응집도와 결합도)
  * 변경에 유연한가?(낮은 결합도)

다시 봐도 아직 익숙치 않다 스프링 사용자 답게 되기에는 ...

자 그럼 5장에서 새로 추가한 코드를 개선해보자 먼저 upgradeLevels() 메소드를 보자.
for문 속에 있는 if/elseif/else들이 보기 힘들다. 성격이 다른 여러가지 로직이 한곳에 섞인 문제다
```
if(user.getLevel() == Level.BASIC && user.getLogin() >=50){
		user.setLevel(Level.SILVER);
		changed = true;
}
...
if(changed){
	userDao.update(user);
}
```
게다가 if조건 블록이 레벨이 추가되면 그만큼 늘어나야 하는 문제가 있다.

그럼 이런 성격이 다른 여러가지 로직이 섞여 있을 경우 어떻게 개선하는지 보자.

  1. 모든 사용자를 가져와 한명씩 업그레이드가 가능한지 확인하고 가능하면 업그레이드 한다.
  1. 업그레이드가 가능한지 확인하는 메소드를 만든다.
  1. 조건이 충족되면 업그레이를 해주는 메소드를 만든다.
```
// 1. 업그레이드 하는 작업의 기본 흐름
public void upgradeLevels(){
		List<User> users = userDao.getAll();
		for(User user : users){
			if(canUpgradeLevel(user)){
				upgradeLevel(user);
			}
		}
	}
// 3. 초건 충족 시 업그레이드 메소드
	private void upgradeLevel(User user) {
		if (user.getLevel() == Level.BASIC)user.setLevel(Level.SILVER);
		else if (user.getLevel() == Level.SILVER)user.setLevel(Level.GOLD);
		userDao.update(user);		
	}
// 2. 업그레이드가 가능한지 확인하는 메소드
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch(currentLevel){
			case BASIC: return(user.getLogin() >=50);
			case SILVER: return(user.getRecommend() >= 30);
			case GOLD : return false;
			default: throw new IllegalArgumentException("Unknown Level:" + currentLevel);
		}		
	}
```

upgradeLevel() 는 여전히 문제가 많다.(if 추가, 예외처리 없음, 업그래이드와 관련없는 작업 등)
다음 단계가 무엇인지는 Level에서 처리한다.
```
public enum Level {
//이늄 선언에 다음 레벨 정보 추가
	GOLD(3,null), SILVER(2,GOLD), BASIC(1,SILVER);

	private final int value;
//다음 단계의 레벨정보를 스스로 갖도록 next 변수 추가
	private final Level next;
		
	Level(int value, Level next) {
		this.value = value;
		this.next = next;
	}

	public int intValue() {
		return value;
	}
	public Level nextLevel(){
		return this.next;
	}
	
	public static Level valueOf(int value) {
		switch(value) {
		case 1: return BASIC;
		case 2: return SILVER;
		case 3: return GOLD;
		default: throw new AssertionError("Unknown value: " + value);
		}
	}
}
```
User 에게 레벨 업그레이드가 필요하니 정보를 변경하라고 요청한다.
```
public void upgradeLevel() {
		Level nextLevel = this.level.nextLevel();	
		if (nextLevel == null) { 								
			throw new IllegalStateException(this.level + "은  업그레이드가 불가능합니다");
		}
		else {
			this.level = nextLevel;
		}	
	}
```
이렇게 추가하면 기존의 복잡한 upgradeLevel() 메소드가 필요한 요청만 하게 되어 간단해 진다.
```
private void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}
```

User.java에 로직이 추가되었다. 테스트를 해야한다.
Level 이늄에 정의된 모든 레벨을 가져와 User에 설정하고 upgradeLevel을 실행 후 레벨이 바뀌는지 확인한다.
User클래스는 스프링이 IoC로 관리해주는 오브젝트가 아니므로 테스트 컨텍스트를 사용하지 않아도 된다.
```
//책과는 다르게 User 클래스가 있는 패키지에 만들었다.
package springbook.user.domain;
...
public class UserTest {
	User user;
	
	@Before
	public void setUp(){
		user = new User();
	}
	
	@Test()
	public void upgradeLevel(){
		Level[] levels = Level.values();
		for(Level level : levels){
			if(level.nextLevel() == null)continue;
			user.setLevel(level);
			user.upgradeLevel();
			assertThat(user.getLevel(),is(level.nextLevel()));
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void cannotUpgradeLevel() {
		Level[] levels = Level.values();
		for(Level level : levels){
			if(level.nextLevel() != null)continue;
			user.setLevel(level);
			user.upgradeLevel();
		}
	}

}
```

UserServiceTest도 개선해보자.
레벨이 추가되거나 변경되면 테스트도 수정해야 하니 번거롭다.
```
	@Test
	public void upgradeLevels(){
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevelUpgrad(users.get(0), false);
		checkLevelUpgrad(users.get(1), true);
		checkLevelUpgrad(users.get(2), false);
		checkLevelUpgrad(users.get(3), true);
		checkLevelUpgrad(users.get(4), false);					
	}
//어떤레벨이 아닌 업그레이드 되는지 아닌지 지정
	private void checkLevelUpgrad(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if(upgraded){
//다음레벨이 무엇인지 Level에 묻는다
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}else{
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
		
	}
```

여전히 중복값이 남아있다. 레벨업을 하는 조건부분이다.
UserService 와 UserServiceTest에 있는 레벨업 조건을 상수처리 해보자.
조건이 변할 경우 선언된 상수 값만 수정하면 된다.
```
// UserService 
public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch(currentLevel){
			case BASIC: return(user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER );
			case SILVER: return(user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
			case GOLD : return false;
			default: throw new IllegalArgumentException("Unknown Level:" + currentLevel);
		}		
	}
```
테스트도 수정한다.
```
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;
...

@Before
	public void setUp() {
		users = Arrays.asList(
			new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
			new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
			new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
			new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
		);	
	}
```

UserLevelUpgradePolicy는 만드는대로 업데이트 하겠습니다.



## 5.2 트랜잭션 서비스 추상화 ##

### 5.2.1 모 아니면 도 ###

**사용자 레벨 관리 작업 중 네트워크가 끊기거나 서버에 장애가 생겨서 작업을 완료할 수 없는 상황이 생긴다면??**

현재까지 작업한 코드가 위와 같이 예외 상황에 어떻게 동작할지 테스트를 해보자.

  * 테스트용 UserService를 상속한 클래스 생성
    * 테스트에서만 사용할 클래스는 테스트 클래스 내부에 스태틱 클래스로 생성하는 것이 간편하다.
    * 테스트용 UserService는 UserService의 일부 기능을 오버라이딩해서 특정 시점에 강제로 예외가 발생하도록 만들 것이다.
    * 현재 접근제한자를 private로 사용중이다.
    * 테스트를 위해 코드를 수정하는것은 피하는 것이 좋지만 이번은 예외로 한다.
    * UserService의 upgradeLevel() 메소드 접근권한 protected로 변경해서 오버라이딩 가능하도록 한다.
```
protected void upgradeLevel(User user) {
	user.upgradeLevel();
	userDao.update(user);
}
```

  * UserService를 상속해서 upgradeLevel() 메소드를 오버라이딩한 UserService 대역을 맡을 클래스를 UserServiceTest 에 추가한다.
```
public class TestUserService extends UserService {
	private String id;

	public TestUserService(String id) {
		this.id = id;
	}

	// upgradeLevel() 메소드는 UserService의 메소드의 기능을 그대로 수행하지만 미리 지정된 id가 발견되면예외를 던진다.
	protected void upgradeLevel(User user) {
		if (user.getId().equals(this.id)) {
			throw new TestUserServiceException();
		}
		super.upgradeLevel(user);
	}
}
```

  * TestUserServiceException은 TestUserService와 마찬가지로 최상위 클래스로 정의하는 대신 테스트 클래스 내에 스태틱 맴버 클래스로 만들어도 된다.
```
static class TestUserServiceException extends RuntimeException {

}
```

  * 테스트를 만들어본다.
```
	@Test
	public void upgradeAllOrNothing(){
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		
		userDao.deleteAll();
		for (User user : users) {
			userDao.add(user);
		}
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceExctption expected");
		} catch (TestUserServiceException e) {
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
```

  * 테스트를 실행하면 에러를 확인 할 수 있다.
![http://dl.dropbox.com/u/176648/testFail.png](http://dl.dropbox.com/u/176648/testFail.png)
  * 위의 에러는 두 번째 사용자 레벨이 바뀐 것이 네 번째 사용자 처리 중 예외가 발생해도 유지되고 있는 것이다.

  * 테스트 실패의 원인
    * **트랜잭션!!!** 이 문제다.
      * upgradeLevels() 메소드가 하나의 트랜잭션 안에서 동작하지 않았기 때문
      * 트랜잭션 : 더 이상 나눌 수 없는 단위 작업


### 5.2.2 트랜잭션 경계설정 ###

  * 트랜잭션 커밋(transaction commit) : 모든 작업이 성공적으로 마무리 되면 모든 작업을 확정시킨다.
  * 트랜잭션 롤백(transaction rollback) : 작업 중 문제가 발생했을 경우 앞서 처리된 작업들도 취소시키는 작업

  * DBC 트랜잭션의 트랜잭션 경계설정
```
Connection c = dataSource.getConnection();

// 트랜잭션 시작
c.setAutoCommit(false);
try{
	PreparedStatement st1 = c.prepareStatement("update users ...");
	st1.executeUpdate();

	PreparedStatement st2 = c.prepareStatement("delete users ...");
	st2.executeUpdate();
	// 트랜잭션 커밋
	c.commit();
}catch(Exception e){
	// 트랜잭션 롤백
	c.rollback();
}

c.close();
```

  * upgradeLevels의 트랜잭션 경계설정 구조
```
public void upgradeLevels() throws Exception {
	// (1) DB Connection 생성
	// (2) 트랜잭션 시작
	try {
		// (3) DAO 메소드 호출
		// (4) 트랜잭션 커밋
	} catch(Exception e){
		// (5) 트랜잭션 롤백
		throw e;
	} finally {
		// (6) DB Connection 종료
	}
}
```

  * 같은 트랜잭션으로 동작하도록 하기 위해서는 같은 Connection 오브젝트를 사용해야 한다.
  * 같은 Connection 오브젝트를 이용하기 위해서는 UserDao를 호출할때마다 Connection을 파라미터로 전달해야 한다.
```
public interface UserDao {
	public void add(Connection c, User user);
	public User get(Connection c, String id);
	...
	public void update(Connection c, User user1);
}
```
  * UserService의 upgradeLevels()는 UserDao의 update()를 직접 호출하지 않고, upgradeLevel() 메소드를 통해 사용자 별로 upgrade를 진행하기 때문에 UserService 메소드에서도 같은 Connection 오브젝트를 사용하도록 파라미터로 전달해줘야 한다.

#### UserService 트랜잭션 경계설정의 문제점 ####
  1. JdbcTemplate을 더 이상 활용할 수 없다.
  1. UserService의 메소드에 Connection 파라미터가 추가되야 한다.
  1. Connection 파라미터가 UserDao 인터페이스 메소드에 추가되면 UserDao는 데이터 액세스 기술에 독립적일 수 없다.
  1. DAO 메소드에 Connection 파라미터를 받게 하면 테스트 코드에도 영향을 미친다.


### 5.2.3 트랜잭션 동기화 ###

  * 트랜잭션 동기화(transaction synchronization) : 트랜잭션을 시작하기 위해 만든 Connection 오브젝트를 특별한 저장소에 보관해두고 이후 호출되는 메소드에서 저장된 Connection을 가져다 사용하고, 트랜잭션이 종료되면 동기화를 마친다.
    * 작업 스레드 마다 독립적으로 Connection 오브젝트를 저장하고 관리하기 때문에 다중 사용자를 처리하는 멀티스레드 환경에서도 충돌나지 않는다.
    * 파라미터를 통해 일일이 Connection 오브젝트를 전달할 필요가 없다.

```
// Connection을 생성할때 사용할 DataSource를 DI 받도록 한다.
private Datasource dataSource;

public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
}

public void upgradeLevels() throws Exception {
	// 스프링이 제공하는 트랜잭션 동기화 관리 클래스로 동기화 작업을 초기화한다.
	TransactionSynchronizationManager.initSynchronization();
	// DB 커넥션을 생성하고 트랜잭션을 시작한다.
	Connection c = DataSourceUtils.getConnection(dataSource);
	c.setAutoCommit(false);

	try {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
		// 정상적으로 마치면 트랜잭션 커밋
		c.commit();
	// 예외가 발생하면 롤백
	} catch (Exception e) {
		c.rollback();
		throw e;
	} finally {
		// 스프링 유틸리티 메소드를 이용해 DB 커넥션을 안전하게 닫는다.
		DataSourceUtils.releaseConnection(c, dataSource);
		// 동기화 작업 종료 및 정리
		TransactionSynchronizationManager.unbindResource(dataSource);
		TransactionSynchronizationManager.clearSynchronization();
	}
}
```

  * 동기화가 적용된 UserService에 따라 테스트도 수정되야 한다.
```
@Autowired
private DataSource dataSource;

@Test
public void upgradeAllOrNothing() throws Exception{
	UserService testUserService = new TestUserService(users.get(3).getId());
	testUserService.setUserDao(this.userDao);
	testUserService.setDataSource(this.dataSource);
	...	
}
```
  * userService 빈 설정에도 dataSource 프로퍼티를 추가한다.
```
<bean id="userService" class="springbook.user.service.UserService">
	<property name="userDao" ref="userDao" />
	<property name="dataSource" ref="dataSource" />
</bean>
```

### 5.2.4 트랜잭션 서비스 추상화 ###
  * 여러 개의 DB를 사용하는 경우에는 지금까지 작성한 코드를 사용할 수 없다.
    * 로컬 트랜잭션(JDBC의 Connection을 이용한 트랜잭션)은 하나의 DB 커넥션에 종속된다.
    * 별도의 트랜잭션 관리자를 통해 관리하는 글로벌 트랜잭션(global transaction) 방식을 이용해야 한다.

  * JTA(Java Transaction API) : XA 리소스(예, 데이터베이스) 간의 분산 트랜잭션을 처리하는 자바 API이다.
  * JTA를 이용한 트랜잭션 처리 코드 구조
```
InitialContext ctx = new InitialContext();
UserTransaction tx = (UserTransaction)ctx.lookup(USER_TX_JNDI_NAME);

tx.begin();
try {
	// 데이터 액세스 코드
	tx.commit();
} catch (Exception e) {
	tx.rollback();
	throw e;
} finally {
	c.close();
}
```
  * 문제점
    * JDBC 로컬 트랜잭션을 JTA를 이용하는 글로벌 트랜잭션으로 바꾸려면 UserService 코드를 수정해야한다.
    * 하이버네이트를 이용한 트랜잭션도 JDBC 로컬 트랜잭션과 JTA를 이용하는 글로벌 트랜잭션 코드와 다르다.
  * 해결방법
    * 공통적인 트랜잭션 경계 설정 방법을 모아서 추상화된 트랜잭션 관리 계층을 만든다.

  * 스프링의 트랜잭션 추상화 방법을 UserService에 upgradeLevels()에 적용해본다.
```
public void upgradeLevels() throws Exception {
	PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
	
	TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

	try {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
		transactionManager.commit(status);
	} catch (Exception e) {
		transactionManager.rollback(status);
		throw e;
	}
}
```

  * 트랜잭션 추상화 API를 적용한 UserService 코드를 JTA를 이용하는 글로벌 트랜잭션으로 변경하려면?
    * PlatformTransactionManager txManager = **new JTATransactionManager()**;
  * 하이버네이트로 구현했다면
    * PlatformTransactionManager txManager = **new HibernateTransactionManager()**;
  * JPA로 적용했다면
    * PlatformTransactionManager txManager = **new JPATransactionManager()**;

스프링의 DI를 적용해 컨테이너를 통해 외부에서 제공받도록 변경한다.
```
// DataSource 제거
private PlatformTransactionManager transactionManager;

public void setTransactionManager(
		PlatformTransactionManager transactionManager) {
	this.transactionManager = transactionManager;
}

public void upgradeLevels() throws Exception {
	TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

	try {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
		this.transactionManager.commit(status);
	} catch (Exception e) {
		this.transactionManager.rollback(status);
		throw e;
	}
}
```

스프링 설정파일도 변경해 준다.
```
<bean id="userService" class="springbook.user.service.UserService">
	<property name="userDao" ref="userDao" />
	<property name="transactionManager" ref="transactionManager" />
</bean>

<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource" />
</bean>
```

테스트도 변경해 준다.
```
@Autowired
PlatformTransactionManager transactionManager;

@Test
public void upgradeAllOrNothing() throws Exception {
	UserService testUserService = new TestUserService(users.get(3).getId());
	testUserService.setUserDao(userDao);
	testUserService.setTransactionManager(transactionManager);

	userDao.deleteAll();
	for (User user : users)
		userDao.add(user);
	
	try {
		testUserService.upgradeLevels();
		fail("TestUserServiceException expected");
	} catch (TestUserServiceException e) {
	}

	checkLevelUpgraded(users.get(1), false);
}
```

이제는 트랜잭션 기술에서 독립적인 코드가 됬다.
**설정파일의 transactionManager 클래스만 변경하면 하이버네이트나 JPA, JDO 등을 코드 변경 없이 사용할 수 있다.**



## 5.3 서비스 추상화와 단일 책임 원칙 ##

  * 단일 책임 원칙(Single Responsibility Principal)
    * 하나의 모듈은 한 가지 책임을 가져야 한다.
    * 하나의 모듈이 바뀌는 이유는 한 가지 이다.
  * 단일 책임 원칙의 장점
    * 수정 대상이 명확하다.
    * 의존관계가 많을 수록 수정사항이 있을 경우 수정해야 하는 코드도 많아지고, 그만큼 실수도 많아지고 버그가 생길 수도 있다.

단일 책임 원책을 잘 지키는 코드는 인터페이스를 도입하고 DI 로 연결해야 하며, 그 결과로 단일책임원칙과 개방 폐쇄 원칙도 잘 지키고, 모듈간 결합도가 낮아서 변경에 따른 영향도 주지 않고, 응집도가 높은 코드가 나온다.
또한 다양한 패턴이 자연스럽게 적용되기도 하고, 테스트도 편리하게 할 수 있다.



## 5.4 메일 서비스 추상화 ##

**사용자 레벨이 업그레이드 되면 해당 고객에게 메일을 발송하라.**

User 테이블에 email 필드를 추가하고, UserService의 upgradeLevel() 메소드에 메일 발송 기능을 추가한다.

  * upgradeLevel()에서 메일발송 메소드를 호출한다.
```
protected void upgradeLevel(User user) {
	user.upgradeLevel();
	userDao.update(user);
	sendUpgradeEMail(user);
}
```
  * JavaMail API를 사용하는 sendUpgradeEMail() 메소드를 추가한다.
```
private void sendUpgradeEMail(User user) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.ksug.org");
		Session s = Session.getInstance(props, null);
		
		MimeMessage message = new MimeMessage(s);
		try {
			message.setFrom(new InternetAddress("useradmin@ksug.org"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			message.setSubject("Upgrade 안내");
			message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");
			
			Transport.send(message);
		} catch (AddressException e) {
			throw new RuntimeException();
		} catch (MessagingException e) {
			throw new RuntimeException();
		/* 현재 try에서 던져질 예외가 없으므로 주석처리 한다.
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();*/
		}
	}
```

메일서버가 준비되어 있지 않기 때문에 MessagingException에서 예외가 발생한다.

![http://dl.dropbox.com/u/176648/mailExcepiton.png](http://dl.dropbox.com/u/176648/mailExcepiton.png)

JavaMail은 많은 사람이 사용하는 검증된 모듈이다. 메일 발송이라는 큰 부하를 발생시키지 않고 테스트용 메일 서버를 이용해 발송 테스트를 한다.

#### 테스트의 문제점 ####
  * JavaMail API에서는 DataSource 처럼 인터페이스로 만들어져서 구현을 바꿀 수 있는게 없다.
  * Session 오브젝트를 만들어야 메일 메시지를 생성하고 전송할 수 있다.
  * Session은 상속이 불가능한 **final** 클래스 이고, 생성자도 모두 private로 되어 있다.
  * 메일 메시지를 작성하는 MailMessage도 전송 기능을 하는 Transport도 마찬가지이다.

#### 해결 방법 ####
  * 서비스 추상화를 적용한다.

  * JavaMail의 서비스 추상화 인터페이스
```
package org.springframework.mail;
...
public interface MailSender {
	void send(SimpleMailMessage simpleMessage) throws MailException;
	void send(SimpleMailMessage[] simpleMessages) throws MailException;
}
```
  * 스프링의 MailSender를 이용한 메일 발송 메소드
```
private void sendUpgradeEMail(User user) {
	// MailSender 구현 클래스의 오브젝트를 생성한다.
	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	mailSender.setHost("mail.server.com");
	
	//  MailMessage 인터페이스의 구현 클래스 오브젝트를 만들어 메일 내용을 작성한다.
	SimpleMailMessage mailMessage = new SimpleMailMessage();
	mailMessage.setTo(user.getEmail());
	mailMessage.setFrom("useradmin@ksug.org");
	mailMessage.setSubject("Upgrade 안내");
	mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 변경되었습니다.");
	
	mailSender.send(mailMessage);
}
```
    * try/catch 블록이 사라졌다.
      * 스프링의 예외 처리 원칙에 따라 MailException 이라는 런타임 예외로 포장해서 던져준다.
  * 스프링의 DI를 적용한다.
    * 구체적인 메일 전송 구현을 담은 클래스 정보는 코드에서 제거한다.(호스트를 설정하는 코드도 제거)
    * 메일 전송 기능을 가진 오브젝트를 DI 받도록 수정한다.
```
public class UserService {
	...
	private MailSender mailSender;
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	private void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 변경되었습니다.");
		
		this.mailSender.send(mailMessage);
	}
}
```
```
<bean id="userService" class="springbook.user.service.UserService">
	<property name="userDao" ref="userDao" />
	<property name="transactionManager" ref="transactionManager" />
	<property name="mailSender" ref="mailSender" />
</bean>

<bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
	<property name="host" value="mail.server.com" />
</bean>
```

  * MailSender 인터페이스를 구현해 테스트용 메일 전송 클래스를 만든다.
```
package springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender{

	@Override
	public void send(SimpleMailMessage arg0) throws MailException {
	}

	@Override
	public void send(SimpleMailMessage[] arg0) throws MailException {
	}

}
```
  * mailSender의 빈 클래스를DummyMailSender로 변경한다.
```
<bean id="mailSender" class="springbook.user.service.DummyMailSender" />
```
  * upgradeAllOrNothing()메소드에도 mailSender를 추가해 준다.
```
public class UserServiceTest {
	@Autowired
	MailSender mailSender;
	@Test
	public void upgradeAllOrNothing() throws Exception {
		...
		testUserService.setMailSender(mailSender);
		...
}
```

  * JavaMail이 아닌 다른 메시징 서버의 API를 사용할때도 해당 API를 이용하는 MailSender 구현 클래스를 만들어 DI 해주면 된다.
  * 메일을 바로 전송하지 않고 작업 큐에 담아뒀다가 정해진 시간에 메일을 발송하는 기능을 만드는 것도 어렵지 않다.

  * 메일 발송 트랜잭션 처리
    * 업그레이드할 사용자 목록을 저장해두고 레벨 업그레이드가 모두 성공적으로 끝났을 때 한 번에 메일을 전송한다.(메일 저장용 리스트 등을 파라미터로 갖고 다녀야 한다.)
    * MailSender를 확장해서 메일 전송에 트랜잭션 개념을 적용한다.
      1. MailSender를 구현한 트랜잭션 기능이 있는 메일 전송용 클래스를 만든다.
      1. 이 오브젝트에 업그레이드 작업 이전에 새로운 메일 전송 작업 시작을 알려주고, mailSender.send() 메소드를 호출해도 실제로 메일을 발송하지 않고 저장해둔다.
      1. 업그레이드 작업이 끝나면 트랜잭션 기능을 가진 MailSender에 저장된 메일을 모두 발송하고, 예외가 발생하면 모두 취소한다.

  * 서비스 추상화는 원활한 테스트를 위해서도 충분히 가치가 있다.
  * 확장이 불가능하게 설계된  API를 사용할 경우나 외부의 리소스와 연동하는 작업은 추상화의 대상이 될 수 있다.


#### DummyMailSender ####
  * 아무런 기능도 없지만, UserService가 반드시 이용해야 하는 의존 오브젝트의 역할을 해주면서 원활하게 테스트 중에 UserService 의 코드가 실행되게 해준다.


**테스트 대역(test double)** : 테스트 환경을 만들어주기 위해, 테스트 대상이 되는 오브젝트의 기능에만 충실하게 수행하면서 빠르게, 자주 테스트를 실행할 수 있도록 사용하는 오브젝트

**테스트 스텁(test stub)** : 테스트 대상 오브젝트의 의존객체로서 존재하면서 테스트 동안에 코드가 정상적으로 수행할 수 있도록 돕는 것(일반적으로 파라미터와 달리 테스트 코드 내부에서 간접적으로 사용됨)

**목 오브젝트(mock object)** : 테스트 대상의 간접적인 출력 결과를 검증하고, 테스트 대상 오브젝트와 의존 오브젝트 사이에서 일어나는 일을 검증할 수 있도록 특별히 설계된 오브젝트.
스텁처럼 테스트 오브젝트가 정상적으로 실행되도록 도와주면서, 테스트 오브제그와 자신의 사이에서 일어나는 커뮤니케이션 내용을 저장해뒀다가 테스트 결과를 검증하는 데 활용할 수 있게 해준다.

#### 목 오브젝트를 이용한 테스트 ####
  * 새로 생성하는 MockMailSender 클래스에 메일 발송하는 기능은 없지만 UserService의 코드가 정상적으로 수행되도록 하고, 테스트 대상의 출력값을 보관해둔다.
```
public class MockMailSender implements MailSender {

	private List<String> requests = new ArrayList<String>();

	public List<String> getRequests() {
		return requests;
	}

	@Override
	public void send(SimpleMailMessage mailMessage) throws MailException {
		requests.add(mailMessage.getTo()[0]);
	}

	@Override
	public void send(SimpleMailMessage[] mailMessage) throws MailException {
	}

}
```
  * MockMailSender도 메일 발송하는 기능은 없지만 테스트 대상인 UserService가 send() 메소드를 통해 자신을 불러서 메일 전송 요청을 보냈을 때 관련 정보를 저장해두는 기능을 한다.
```
@Test
@DirtiesContext // 컨텍스트의 DI 설정을 변경하는 테스트라는 것을 알려준다.
public void upgradeLevels() throws Exception {
	userDao.deleteAll();
	for (User user : users) {
		userDao.add(user);
	}
	
	// 메일 발송 결과를 테스트할 수 있도록 목 오브젝트를 만들어 userService의 의존 오브젝트로 주입한다.
	MockMailSender mockMailSender = new MockMailSender();
	userService.setMailSender(mockMailSender);

	// 메일 발송이 일어나면 MockMailSender 오브젝트의 리스트에 그 결과가 저장된다.
	userService.upgradeLevels();

	checkLevelUpgraded(users.get(0), false);
	checkLevelUpgraded(users.get(1), true);
	checkLevelUpgraded(users.get(2), false);
	checkLevelUpgraded(users.get(3), true);
	checkLevelUpgraded(users.get(4), false);
	
	// 목 오브젝트에 저장된 메일 수신자 목록을 가져와 업그레이드 대상과 일치하는지 확인한다.
	List<String> request = mockMailSender.getRequests();
	assertThat(request.size(), is(2));
	assertThat(request.get(0), is(users.get(1).getEmail()));
	assertThat(request.get(1), is(users.get(3).getEmail()));
}
```
  * DummyMailSender를 대신할 목 오브젝트인 MockMailSender를 만들고 UserService 오브젝트의 수정자를 이용해 수동 DI 해준다.
  * 테스트 대상의 메소드를 호출하고 업그레이드 결과를 검증한다.
  * userService에 DI 해줬던 목 오브젝트로부터 getRequests() 를 호출해서 메일 주소가 저장된 리스트를 가져온 후 그 결과를 검증한다.


# 생각하기 #
  * 트랜잭션 처리를 한 후 테스트를 실행했을 때 자꾸 오류가 발생해서 이것저것 해보면서 확인을 했는데, 결국 테이블의 엔진 타입을 바꿔주고 해결했다.(이것 때문에 버린 시간이...흑..ㅜㅜ)
```
ALTER TABLE USERS ENGINE = InnoDB;
```
  * 참고 http://dev.mysql.com/doc/refman/5.0/en/connector-j-usagenotes-j2ee.html#connector-j-usagenotes-spring-config-transactional