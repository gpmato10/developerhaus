
# 내용정리 #

## 4.1 사라진 SQLException ##

**예외를 처리할때 지켜야 할 핵심 원칙** : 모든 예외는 적절하게 복구되든지 아니면 작업을 중단시키고 운영자 또는 개발자에게 분명하게 통보돼야 한다.
(단순히 try,catch문으로 감싸기만 하고 처리를 안한다던지 무책임하게 throws Exception을 기계적으로 붙여줘서 모든예외를 무조건 던져버리는 나쁜습관은 버리자!)



**예외의 종류와 특징**
  * 자바에서 throw를 통해 발생시킬 수 잇는 예외는 크게 세가지
> - **_Error_** : java.lang.Error 클래스의 서브클래스 들. 시스템에 뭔가 비정상적인 상황이 발생했을 경우에 사용므로 시스템레벨에서 특별한 작업을 하는게 아니라면 애플리케이션에서는 이런 에러에 대한 처리는 신경쓰지 않아도 된다.

> - **_Exception과 체크 예외_** : java.lang.Exception 클래스와 그 서브클래스로 정의되는 예외들.
> > Exception클래스의 서브클래스이면서 RuntimeException 클래스를 상속하지 않은것들은 체크예외(checked exception)로 구분한다.
> > RuntimeException을 상속한 클래스들은 언체크예외(unchecked exception)로 구분한다.


> - **_RuntimeException과 언체크/런타임예외_** : java.lang.RuntimeException 클래스를 상속한 예외들은 명시적은 예외처리를 강제하지 않기때문에 언체크 예외 또는 런타임 예외라고 불린다.


**_[예외클래스 구조]_**

![http://lh4.ggpht.com/_W3BSHAaadiM/TQzu-Y2ajXI/AAAAAAAAABE/zkpSbqKAu4g/ExceptionClass.jpg](http://lh4.ggpht.com/_W3BSHAaadiM/TQzu-Y2ajXI/AAAAAAAAABE/zkpSbqKAu4g/ExceptionClass.jpg)



Excepion 처리여부를 Compiler가 체크하는지 여부에 따라서 확인하면 체크 Exception 하지않을 경우에는 언체크 Exception으로 구분된다. 언체크 Exception 은 주로 프로그램 오류가 있을때 발생하도록 의도된 것들인데 피할 수 있지만 개발자가 부주의해서 발생할 수 있는 경우에 발생하도록 만든것이므로 굳이 예외처리(catch나 throws)를 사용하지 않아도 되도록 만든 것이다.

**예외처리 방법**
  * **예외 복구** : 예외상황을 파악하고 문제를 해결해서 정상상태로 돌려 놓는 것이다. 단순히 예외 메시지를 던져서 처리하는 것이 아니라 기능적으로는 사용자에게 예외 상황으로 비출지 몰라도 애플리케이션에서는 정상적으로 설계된 흐름을 따라 진행 되어야 한다.

  * **예외처리 회피** : 예외처리를 자신이 담당하지 않고 자신을 호출한 쪽으로던져 버린다.
```

        // throws 문으로 선언해서 예외가 발생하면 알아서 던진다
	public void deleteAll() throws SQLException {
		this.jdbcContext.executeSql("delete from users");
	}

        // catch 문으로 일단 예외를 잡은후에 로그를 남기고 다시 예외를 던진다.
	public void deleteAll() throws SQLException {
		try{
			this.jdbcContext.executeSql("delete from users");
		}catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
```

> - 예외를 회피하는 것은 다른 오브젝트에서 책임을 분명히 지게 하거나 자신을 사용하는 쪽에서 예외를 다루는게 최선의 방법이라는 분명한 확신이 있어야 한다.

  * **예외 전환** : 예외 회피와 비슷하지만 발생한 예외를 그대로 넘기는 것이 아니라 적절한 예외로 전환해서 던진다. 보통 두가지 목적으로 사용된다.

> - 첫번째 방법은 내부에서 발생한 예외에 대해서  적절한 의미를 부여하지 못한 경우에 그 의미를 분명하게 할 수있도록 좀더 의미있게 바꿔주기 위해 사용된다.
```
try{
   ...
}catch(SQLException e){

   // ErrorCode 가 MySQL의 "Duplicate Entry(1062)" 이면 구체적으로 예외를 전환한다.
   if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
      throw DuplicateUserIdException(e);
   else
      throw e;
}

```

> - 두번째 방법은 예외를 처리하기 쉽고 단순하게 만들기위해 포장하는 것이다. 의미를 명확하게 하려고 다른예외로 전환하는 것이 아니라 주로 체크 예외를 언체크 예외인 런타임 예외로 바꾸는 경우에 사용된다. 대표적으로 EJBException 을 들 수 있다.
```
/* EJBException 은 런타임Exception 클래스를 상속한 런타임 예외다. 런타임 예외기 때문에 잡아도
딱히 복구 할 방법이 없어서 다른 EJB나 클라이언트에서 일일이 예외를 잡거나 다시 던지는 수고를 할필요가 없다.*/
try{
   ...
}catch(SQLException e){
    throw new EJBException(e);
}catch(NamingException ne){
    throw new EJBException (ne);
}
   ...
```

**예외처리 전략**

  * **add() 메소드의 예외처리**
> - JDBC 코드에서 SQLException이 발생 할 수 있는데 아까 예외전환 첫번째 방법은 아이디 중복일경우 DuplicateUserIdException으로 전환해주고 아니라면 SQLException을 그대로 던진다. 중복 오류같은경우는 충분히 복구가능한 예외이지만 SQLException은 대부분 복구 불가능한 예외이므로 잡아봤자 처리할 것도 없고 throw 를 타고 계속 밖으로 던지느니 차라리 RuntimeException 으로 던져서 신경쓰지 않게 하는편이 낫다.
```
// 별도의 DuplicateUserIdException을 만든다.필요하면 언제든 잡아서 처리하면 되지만 필요하지 않을경우는 신경쓰지 않아도 되도록 RuntimeException을 상속한다.

package springbook.user.dao;

public class DuplicateUserIdException extends RuntimeException{
	public DuplicateUserIdException(Throwable cause){
		super(cause);
	}

}

```
```
public void add(final User user) throws DuplicateUserIdException {
	
	try{
		...
	}catch(SQLException e){
                // 중복일경우 예외전환 
		if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
			throw new DuplicateUserIdException(e);
		else
                // 그 이외의 예외는 언체크예외로 전환
			throw new RuntimeException(e);
		
	}
}
```


  * **_[add()메소드로 중복아이디 생성후 junit 테스트 결과]_**

> ![http://lh5.ggpht.com/_W3BSHAaadiM/TQ3yTAirSGI/AAAAAAAAABU/n1ente--V0M/s800/%EC%A4%91%EB%B3%B5%EC%97%90%EB%9F%AC.jpg](http://lh5.ggpht.com/_W3BSHAaadiM/TQ3yTAirSGI/AAAAAAAAABU/n1ente--V0M/s800/%EC%A4%91%EB%B3%B5%EC%97%90%EB%9F%AC.jpg)

  * **애플리케이션 예외** : 시스템 또는 예외상황이 원인이 아니라 애플리케이션 자체의 로직에 의해 의도적으로 발생시키고, 반드시 catch 해서 무엇인가 조치를 취하도록 요구하는 예외들을 일반적으로 애플리케이션 예외라고 한다.

**SQLException은 어떻게 됐나?**

> - 지금까지 정리된 내용을 바탕으로 jdbcTemplate을 적용하는중에 DAO의 SQLException이 왜 사라졌는지에 대해서 살펴보자. SQLException 예외 같은경우는 99% 코드레벨에서 복구할 방법이 없다. 시스템 예외라면 애플리케이션에서 복구할 방법이 없을 뿐더러 미처 다루지 않았던 범위를 벗어난 값 때문에 발생한 예외라도 복구할 방법이 없다. 따라서 예외처리 전략을 적용해야한다. 스프링의 JdbcTemplate은 이 예외처리 전략을 따르고 있다.

> - _JdbcTemplate 템플릿과 콜백안에서 발생하는 모든 SQLException을 런타임 예외인 DataAccessException으로 포장해서 던져준다._
```
// 이런식으로..
public int queryForInt(String sql)throws DataAccessException{}
```
> - 그래서 JdbcTemplate안의 메소드들이 모두 throws로 선언되어 있긴 하지만 DataAccessException 이 런타임 예외이므로 그것을 사용하는 메소드에서 이를 잡거나 다시 던질의무는 없다. 정 잡고 싶으면 DataAccessException을 잡아서 처리해줘도 된다.


## 4.2 예외 전환 ##

**JDBC 한계**

> - JDBC는 자바를 이용해 DB에 접근하는 방법을 추상화된 API 형태로 정의해 놓고 각 DB 업체가 JDBC 표준을 따라 만들어진 드라이버를 제공하게 해준다. 하지만 현실적으로 DB를 자유롭게 바꾸어 사용 할 수 있는 DB프로그램을 작성하는 데는 두가지 걸림돌이 있다.

  * **첫번째 문제[_비표준SQL_]** :  JDBC코드에서 사용하는 SQL 이다. SQL 이 어느정도 표쥰화된 언어이고 몇가지 표준규약이 잇긴 하지만 각 DB마다 이러한 표준을 따르지 않는 기능을 제공한다. 간단한 쿼리문이라면 상관이 없겠지만 각 DB의 특별한 기능을 사용하거나 성능을 향상시키기위해서 최적화 기법을 SQL에 적용하게 되면 작성된 SQL문은 DAO코드에 들어가게 되고 DAO는 특정 DB에 종속되게 된다. DB가 바뀐다면 적지않은 SQL문을 수정해야 될것이다. 이 문제의 해결책을 생각해보면 DAO를 DB별로 만들어 사용하거나 SQL을 외부에서 독립시켜서 바꿔쓸수 있게 하는것이다.

  * **두번째 문제 [_호환성 없는 SQLException DB 에러정보_]** : SQLException 이 두번째 문제다. DB마다 SQL만 다른것이 아니라 에러의 종류와 원인도 제각각이다. JDBC는 데이터 처리 중에 발생하는 다양한 예외를 SQLException 에 모두 담아 버리고 JDBC API는 이 하나만 던져버린다. 여기서 문제가 발생하는데 이전에 나왔던 예외 처리 구문중에서
```
if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) 
```
> - 여기서 사용한 코드는 Mysql 전용 코드일뿐 다른 DB를 사용한다면 다른 DB에서 중복되어서 나오는 에러코드는 또 다를것이다. 이러한 문제를 조금이나마 해결하기 위해서 SQLException은 각 DB마다 독립적인 에러정보를 얻을 getSQLState()메소드로 상태정보를 가져올 수 있다. 하지만 DB의 JDBC드라이버에서 SQLException을 담을 상태코드를 정확하게 만들어 주지 않아서 상태코드를 가진 SQLException 만으로 DB에 독립적인 유연한 코드를 작성하는건 불가능에 가깝다.

**DB 에러코드 매핑을 통한 전환**

> - SQL 상태코드는 JDBC 드라이버를 만들 때 들어가는 것이므로 같은 DB라고 하더라도 드라이버를 만들 때마다 달라지기도 하지만 DB에러코드는 DB에서 직접 제공해주는 것이니 버전이 올라가더라도 어느정도 일관성이 유지된다. DB마다 에러코드가 제각각 이지만 스프링은 DB별 에러코드를 분류해서 스프링이 정의한 예외 클래스와 매핑해 놓은 에러코드 매핑정보 테이블을 만들어두고 이를 이용한다.

```
<bean id="Oracle" class="org.springframework.jdbc.support.SQLErrorCodes">
<property name="badSqlGrammarCodes">              // 예외 클래스 종류
 <value>900,903,904,917,936,17006</value>         // 매핑되는 DB에러코드.
</property>
...
```
[_에러코드 매핑 클래스 종류는 API 참조_]
http://static.springsource.org/spring/docs/3.0.x/api/org/springframework/jdbc/support/SQLErrorCodes.html

- JdbcTemplate은 DB의 에러코드를 DataAccessException 계층구조의 클래스중 하나로 매핑해 준다. 전환되는 JdbcTemplate에서 던지는 예외는 모두 DataAccessException  서브클래스 타입이다. SQLException을 런타임 예외인 DataAccessException 계층구조의 예외로 포장하기때문에 add 메소드에는 예외 포장을 위한 코드가 필요없으며 DB종류와 상관없이 중복키로 발생한 에러는 DuplicateKeyException으로 매핑되서 던져진다. add 메소드를 사용하는 쪽에서 중복 키 상황에 대해서 대응이 필요한경우 참고할 수 있도록 throws DuplicateKeyException 를 넣어주면 편리하다.
```
public void add(final User user) throws DuplicateKeyException{
	this.jdbcTemplate.update("insert into users(id,name,password) values(?,?,?)",user.getId(), user.getName(), user.getPasswd());
}
```

**DAO 인터페이스와 DataAccessException 계층구조**
> - DataAccessException은 JDBC의 SQLException을 전환하는 용도로만 만들어진 것이 아니라 데이터 액세스 기술에(JDO,Hibernate,JPA)서 발생하는 예외에도 적용된다. DataAccessException은 의미가 같은 예외라면 데이터 액세스 기술의 종류와 상관없이 일관된 예외가 발생하도록 만들어준다.
  * **DAO 인터페이스와 구현의 분리**
> - UserDAO의 인터페이스를 분리해서 기술에 독립적인 인터페이스로 만들려면
```
public interface UserDAO{
  public void add(User user);
}
```
> - 이런식으로 만들어야 할것이다. 하지만 이러한 메소드 선언은 현재 사용할 수 없다. JDBC API를 사용하는 add는 SQLException을 던질텐데 인터페이스 메소드선언에는 없는 예외를 add메소드의 throws에 넣을 수 없기 때문에 인터페이스 메소드에도  public void add(User user) throws SQLException; 같이 선언되어야 한다.
> - 이렇게 정의한 인터페이스는 JDBC가 아닌 데이터 엑세스 기술로 DAO구현을 하게될 경우 각 기술들은 각자 그기술에서 정의된 예외를 던지므로 사용을 할 수 없게된다.
```
public void add(User user) throws PersistentException; // JPA
public void add(User user) throws HibernateException; // Hibernate
public void add(User user) throws JdoException; // JDO
```
> 그렇다고 throws Exception을 날리는 방법은 무책임한 선언이다. 남은방법은 JDBC를 이용한 DAO에서 모든 SQLException을 런타임 예외로 포장해 주기만 한다면 아까 선언한대로 사용이 가능하다.

> - 하지만 이럴 경우 예를들어 중복키 에러처럼 비지니스 로직에서 처리할 수 있는 예외도 런타임예외로 포장하기때문에 무시해 버릴수가 있다. 또한 문제는 액세스 기술이 달라지면 각 기술마다 다른종류의 예외가 던져 지기때문에 결국 단지 인터페이스로 추상화하고 일부 기술에서 발생하는 체크예외를 런타임 예외로 전환하는것만으로는 불충분하다.

  * **데이터 액세스 예외 추상화와 DataAccessException 계층구조**
> - 스프링은 자바의 다양한 데이터 액세스 기술을 사용할 때 발생하는 예외들을 추상화해서 DataAccessException 계층구조 안에 정리해 놓았다. 기술의 종류에 상관없이 같은성격의 예외를 추상화해서 던져주므로 시스템 레벨의 예외 처리작업을 통해 개발자에게 빠르게 통보해 주도록 만들 수 있다.

> - 예를들면 JDO,JPA,하이버네이트 처럼 오브젝트/엔티티 단위로 정보를 업데이트 하는 경우에는 낙관적인 락킹이 발생할 수 있다. [낙관적인 락킹]이라 함은 같은 정보를 두명의 사용자가 동시에 조회하고 순차적으로 업데이트 할 때 뒤늦게 업데이트 한것이 업데이트한 것을 덮어쓰지 않도록 막아주는데 쓸 수 있는 편리한 기능이다. 각 기술마다 다른 종류의 예외를 발생 시킬텐데 스프링의 예외 전환 방법을 적용하면 기술에 상관없이 DataAccessException 의 서브클래스인 ObjectOptimistickLockingFailureException으로 통일 시킬 수 있다. 낙관적인 락킹이 발생했을 때 일관된 방식으로 예외처리를 해주려면 ObjectOptimistickLockingFailureException을 잡도록 만들면 되는 것이다.

**기술에 독립적인 UserDAO 만들기**
  * **인터페이스 적용**
> - 지금까지 만들어서 써왔던 UserDao클래스를 인터페이스와 구현으로 분리해보자.
```
/* UserDao로 인터페이스 생성. setDataSource는 UserDao의 구현방법에 따라 변경될수 있는 메소드고 UserDao를 사용하는 클라이언트가 알고 있을 필요도 없다. */
public interface UserDao {
		public void add(final User user);
		public User get(String id) ;
		public void deleteAll() ;
		public int getCount()  ;
		public List<User> getAll();
}
```
> - UserDao -> UserDAOJdbc 로 변경하고 UserDao 인터페이스를 구현하도록 implements로 선언.
```
public class UserDaoJdbc implements UserDao {
	
...	
```
> - applicationContext.xml 의 빈 클래스 이름 변경. 보통 빈의 이름은 클래스 이름이 아니라 클래스의 구현 인터페이스 이름을 따르는 경우가 일반적이다. 그래야 나중의 구현 클래스를 바꿔도 혼란이 없다.
```
	<bean id="userDao" class="springbook.user.dao.UserDaoJdbc">
		<property name="dataSource" ref="dataSource"/>
	</bean>
```

  * **테스트 보완**
> - @Autowired는 스프링의 컨테이너 내에서 정의된 빈중에서 인스턴스 변수에 주입 가능한 빈을 찾아준다. UserDaoJdbc 오브젝트는 UserDao 타입이기때문에 UserDaoTest의 Dao 변수에 UserDaoJdbc 클래스로 정의된 빈을 넣는데 아무 문제가 없다. 그러므로 굳이 UserDaoTest 코드에서 dao 선언부분을 UserDaoJdbc로 변경하지 않아도 된다.

> - 예외가 발생하면 성공이고 아니면 실패하게 만들어야 하므로 예외를 검증해주는 @Test(expected=DataAccessException.class) 적용하고 테스트해보고 구체적으로 어떤예왼지 확인하기위해 빼보고 테스트
![http://lh5.ggpht.com/_W3BSHAaadiM/TQ4isJmWaAI/AAAAAAAAABc/neFugHJlMUo/s576/%ED%85%8C%EC%8A%A4%ED%8A%B8.jpg](http://lh5.ggpht.com/_W3BSHAaadiM/TQ4isJmWaAI/AAAAAAAAABc/neFugHJlMUo/s576/%ED%85%8C%EC%8A%A4%ED%8A%B8.jpg)

  * **DataAccessException 활용 시 주의사항**
> - DataAccessException은 안타깝게도 아직까지 JDBC를 이용하는 경우에만 발생한다. 데이터 엑세스 기술을 하이버네이트나 JPA를 사용했을 때도 동일한 예외가 발생할 것으로 기대하지만 실제로 다른 예외가 던져 지진다. 그 이유는 SQLException에 담긴 DB의 에러코드를 바로 해석하는 JDBC와는 달리 JPA,하이버네이트,JDO등에서는 각 예외들을 최종적으로 DataAccessException으로 변환하는데 DB의 에러코드와 달리 이런 예외들은 세분화 되어있지 않기 때문이다. DataAccessException이 기술에 상관없이 어느 정도 추상화된 공통 예외로 변환해 주기는 하지만 근본적인 한계때문에 DataAccessException을 잡아서 처리하는 코드를 만들려고 한다면 미리 학습 테스트를 만들어서 실제로 전환되는 예외의 종류를 확인해둘 필요가 있다.

> - SQLException을 직접해석해 DataAccessException으로 변환하는 코드의 사용법

```
public class UserDaoTest {
@Autowired UserDao dao;
@Autowired DataSource dataSource;

...
@Test
public void sqlExceptionTranslate(){
	dao.deleteAll();
	
	try{
		dao.add(user1);
		dao.add(user1);
	}catch (DuplicateKeyException e) {
                // getRootCause() 메소드를 이용하면 중첩되어있는 SQLException을 가져온다.
		SQLException sqlEx = (SQLException)e.getRootCause();
                // dataSource를 이용해 SQLExceptionTranslator  오브젝트를 만든다
		SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
                // translate()메소드를 호출해주면 SQLException을 DataAccessException 예외로 변환해준다.
		assertThat(set.translate(null, null, sqlEx),is(DuplicateKeyException.class));
	}
}
...

```


# 생각하기 #