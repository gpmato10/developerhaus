

# 내용정리 #

## 1.1 초난감 DAO ##

여기서 보이는 UserDao 의 문제점은 중복된 코드, DB 접근정보를 UserDao 가 직접 알고 있다는 것 등이 될 것이다.
이를 객체지향 설계원칙에 맞게 수정하고 스프링을 사용함으로서 좀 더 가치있는(?) UserDao 로 만들어본다.


## 1.2 DAO의 분리 ##

**관심사의 분리 Separation of Concerns**
: 관심이 같은 것끼리는 하나의 객체 안으로 또는 친한 객체로 모이게 하고, 관심이 다른 것은 가능한 따로 떨어져서 서로 영향을 주지 않도록 분리하는 것

중복된 코드가 존재하는 UserDao 에 있는 2개의 메소드에서 중복 코드를 새로운 메소드로 추출해 보지만 (alt+shift+m이 떠오른다ㅋ)

```
public void add(User user) throws ClassNotFoundException, SQLException {
   Connection c = getConnection();
   ....
}

public void get(String id) throws ClassNotFoundException, SQLException {
   Connection c = getConnection();
   ....
}

private Connection getConnection() throws ClassNotFoundException, SQLException { /* 공통 메소드로 추출 */
   Class.forName("com.mysql.jdbc.Driver");
   Connection c = DriverManager.getConnection(....);
   return c;
}
```

여전히 UserDao 에서 디비 접근 방법에 대한 한정적인 정보를 가지고 있음이 문제가 되었다.

그래서 UserDao 를 추상클래스, 추상메소드로 변경하고 이를 상속한 각각의 Dao 가 getConnection 메소드를 오버라이딩 할 수 있게 하였다.
```
public abstract class UserDao {
    public void add(...) { .......... }
    public void get(...) { .......... }
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}
```

```
public class NUserDao extends UserDao {
   public Connection getConnection() throw ClassNotFoundException, SQLException {
      // n사 connection 생성코드
   }
}
```

```
public class DUserDao extends UserDao {
   public Connection getConnection() throw ClassNotFoundException, SQLException {
      // d사 connection 생성코드
   }
}
```

이 과정에서 두 가지 디자인 패턴이 사용되었다.

**[템플릿 메소드 패턴](http://underclub.tistory.com/124)** :
하위 클래스에서 어떤 구현을 하더라도 상위 클래스에서는 정해진대로 큰 흐름만을 처리한다.

**[팩토리 메소드 패턴](http://underclub.tistory.com/125)** :
상위 클래스에서 훅 메소드 혹은 추상 메소드를 오버라이딩하여 구현하며 슈퍼클래스의 기본 코드에서 독립시킨다.


하지만 상하위 클래스의 밀접한 관계를 가지는 상속의 단점을 그대로 안고 있는 UserDao 는 다시 리팩토링 되어야 할 것이다. 다중상속이 되지 않아 확장이 어려우며, 밀접한 연결 관계는 변화에 대해 개방적이기 때문이다.


## 1.3 DAO의 확장 ##

상속의 단점인 확장성을 보완하고 독립을 시키기 위해 getConnection() 을 클래스(SimpleConnectionMaker.java)로 뺀다. 이 때 UserDao 코드를 확인해 보면 생성자에서 SimpleConnectionMaker 를 직접 호출하고 있다.

```
public Class UserDao {
   private SimpleConnectionMaker simpleConnectionMaker;
   public UserDao() {
      simpleConnectoinMaker = new SimpleConnectionMaker();
   }
}
```

이는 N사와 D사가 각자 확장해서 가져갔던 기능을 다시 사용하지 못하게 되었다. UserDao 의 코드가 SimpleConnectionMaker 라는 특정 클래스에 종속되어 있기 때문이다.

**인터페이스의 도입**

클래스를 분리하면서 확장성까지 보완할 수 있는 방법으로 (드디어) 인터페이스가 나왔다. 서로 긴밀한 연결을 갖지 않고 중간에 추상적인 느슨한 연결고리를 만들어 줄 수 있게 되었다.

이렇게 되면 UserDao 는 인터페이스의 메소드를 통해 알 수 있는 기능에만 관심을 가지고, 어떻게 그 기능이 구현되었는지는 알 필요가 없어졌다.

```
public interface ConnectionMaker { /* 인터페이스 */
   public Connection makeConnectoin() throws ClassNotFoundException, SQLException;
}
```

```
public class DConnectionMaker implements ConnectionMaker {
   ...
   public Connection makeConnection() throws ClassNotFoundException, SQLException {
      // d사 connection 생성코드
   }
}
```

```
public class UserDao {
   private ConnectionMaker connectionMaker;
   public UserDao() {
      connectionMaker = new DConnectionMaker();
   }
   .....
}
```

휴- 아직도 갈 길이 멀다. UserDao 생성자에 아직도 클래스 이름이 들어가고 있다. DConnectionMaker 라는 클래스를 알아버려서 불 필요한 의존관계가 생겨나버렸다.

**관계설정 책임의 분리**

우리는 이 쓸데없는 관심을 분리해서 클라이언트에게 "너가 던져줘!" 라고 해줄 것이다. UserDao 에서는 파라미터로 ConnectionMaker 를 구현한 클래스를 받아서 직접적인 연결을 사라지게 할 것이다.

```
public class UserDao {
   private ConnectionMaker connectionMaker;
   public UserDao(ConnectionMaker connectionMaker) {
      this.connectionMaker = connectionMaker;
   }
   .....
}
```

```
public class UserDaoTest {
   public static void main(String[] args) throws ClassNotFoundException, SQLException {
      ConnectionMaker connectionMaker = new DConnectionMaker();
      UserDao dao = new UserDao(connectionMaker);
   }
}
```

UserDaoTest 는 UserDao 와 ConnectionMaker 와의 런타임 오브젝트 의존관계를 설정하는 책임을 담당하게 되었다. 이렇게 인터페이스 도입과 클라이언트의 도움을 받는 방법으로 상속을 사용했을 때 보다 훨씬 유연해졌다. ConnectoinMaker 라는 인터페이스를 구현한 클래스라면 그대로 적용할 수 있게 되었다.

이 과정에서 우리는 다음과 같은 원칙과 패턴을 발견할 수 있었다.

**개방폐쇄원칙 Open-Closed Principle** : 클래스나 모듈은 확장에는 열려 있어야 하고 변경에는 닫혀 있어야 한다.

**응집도** : 하나의 클래스가 하나의 기능(책임)을 온전히 순도 높게 담당하고 있는 정도

**결합도** : 클래스 간의 서로 다른 책임들이 얽혀 있는 상호 의존도의 정도

**높은 응집도** : 하나의 몰, 클래스가 하나의 책임 또는 관심사에만 집중되어 있는 것

**낮은 결합도** : 책임과 관심사가 다른 오브젝트 또는 모듈이 느슨하게 연결된 형태를 유지하는 것

높은 응집도와 낮은 결합도 이미지 : http://patterns.springnote.com/pages/6297307/attachments/3836645

## 1.4 제어의 역전(IoC) ##

클라이언트인 UserDaoTest 는 UserDao 가 잘 돌아가는지 확인하기 위해서 만들어졌는데 어떤 클래스를 사용할지 결정하는 책임을 맡기고 나니 이것도 분리를 해야겠다.

그것을 담당할 DaoFactory 클래스를 만들도록 한다. (팩토리 : 객체의 생성방법을 결정하고 만들어진 오브젝트를 돌려주는 역할을 담당한다.) Dao 가 10개 존재한다면 ConnectionMaker 를 생성하는 부분도 10개가 되니 이 부분은 공통 메소드로 추출한다.

```
public class DaoFactory {
   public UserDao userDao() {
      UserDao userDao = new UserDao(connectionMaker());
      return userDao;
   }

   public ConnectionMaker connectionMaker() { /* 공통메소드로 추출 */
      return new DConnectionMaker();
   }
}
```

그럼 이젠 UserDaoTest 에선 아래와 같이 호출해서 사용만 하면 된다.

```
public class UserDaoTest {
   public static void main(String[] args) throws ClassNotFoundException, SQLException {
      UserDao dao = new DaoFactory().userDao();
      .....
   }
}
```


**제어의 역전**

처음 작성되었던 UserDao 를 생각해보자. UserDao 가 객체를 생성하거나 흐름을 결정할 때 능동적으로 참여해왔다. 모든 오브젝트가 자신이 사용할 클래스를 결정하고 언제 어떻게 그 오브젝트를 만들지를 스스로 관장한다. 모든 종류의 작업을 사용하는 쪽에서 제어하는 구조였다.

이런 제어 흐름의 개념을 거꾸로 뒤집은 것이 제어의 역전, IoC 이다.

UserDao 는 어떤 클래스를 만들고 사용할지 흐름에 있어서 수동적인 입장이 되었다. 자기 자신도 팩토리에 의해 만들어지고 사용되어야 한다.

이렇게 관심을 분리하고 책임을 나누고 유연하게 확장 가능한 구조로 만들기 위해 DaoFactory 를 도입했던 과정이 IoC 를 적용하는 과정이었다.


## 1.5 스프링의 IoC ##

지금까지 DaoFactory, 즉 팩토리 클래스를 만들어서 특정 오브젝트의 생성과 관계설정을 할 수 있도록 하였다. 하지만 클라이언트가 특정 오브젝트를 가져오려면 어떤 팩토리 클래스를 사용해야 할지 알아야 하고 필요할 때마다 팩토리 클래스를 생성해야 하는 번거로움이 생겼다.

그래서! 이 팩토리 클래스인 DaoFactory 를 스프링의 applicationContext 정보로 사용하도록 애노테이션을 추가해 본다.

```
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
...
@Configuration /* 설정을 담당하는 클래스 */
public class DaoFactory {
   @Bean /* 오브젝트를 생성한다 */
   public UserDao userDao() {
      UserDao userDao = new UserDao(connectionMaker());
      return userDao;
   }
   @Bean /* 오브젝트를 생성한다 */
   public ConnectionMaker connectionMaker() {
      return new DConnectionMaker();
   }
}
```

@Configuration 이 붙은 자바코드를 설정정보로 사용하려면 AnnotationConfigApplicationContext 를 사용한다.

```
public class UserDaoTest {
   public static void main(String[] args) throws ClassNotFoundException, SQLException {
      ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
      UserDao dao = context.getBean("userDao", UserDao.class); /* Object 로 리턴하므로 제네릭메소드 방식 이용 */
   }
}

```

위 방식은 기존 DaoFactory 와 별 차이가 없는 듯 하지만 클라이언트가 구체적인 팩토리 클래스를 알 필요가 없게 되었다.
그리고 applicationContext 가 되면서 생성, 관계 설정 뿐만 아니라 다양한 기능이 제공된다. (추후에 알게 되겠죠??? 음..)
또 한 getBean() 외에 빈을 검색하는 다양한 방법을 제공한다.

**빈 Bean, 빈 오브젝트** : 스프링이 IoC 방식으로 관리하는 오브젝트, 스프링이 생성과 제어를 담당하는 오브젝트

**빈 팩토리** : 빈을 등록하고 생성하고 조회하고 돌려주는 등의 부가적인 빈을 관리 하는 기능을 하며, 이를 확장한 ContextApplication 을 보통 이용한다.

**애플리케이션 컨텍스트 applicationContext** : 빈 팩토리를 확장한 IoC 컨테이너로 빈 팩토리의 기본적인 기능에 스프링에 제공하는 각종 부가 서비스를 추가로 제공한다.

**설정정보, 설정 메타정보** : applicationContext 나 빈 팩토리가 IoC 를 적용하기 위해 사용하는 메타정보, 혹은 구성정보, 형상정보다.

**컨테이너, IoC 컨테이너** : IoC 방식으로 빈을 관리한다는 의미에서 애플리케이션 컨텍스트나 빈 팩토리를 뜻한다.


## 1.6 싱글톤 레지스트리와 오브젝트 스코프 ##

스프링의 applicationContext 와 팩토리클래스(오브젝트팩토리)의 차이점을 싱글톤 레지스트리라는 개념에서 또 발견할 수 있다. 그 전에 오브젝트의 동일성과 동등성에 대해 알아본다.

**오브젝트의 동일성** : 두 개의 오브젝트가 완전히 같다. 총 1 개의 오브젝트가 존재하고 2 개의 오브젝트 레퍼런스 변수를 갖고 있다.

**오브젝트의 동등성** : 두 개의 오브젝트가 동일한 정보를 담고 있다. 총 2 개의 오브젝트가 메모리 상에 존재하는 것이다.

DaoFactory 에서 가져오는 UserDao 는 매번 새로운 다른 오브젝트이다. 하지만 스프링의 applicationContext 에서 가져오는 UserDao 는 동일한 오브젝트가 된다.

스프링은 기본적으로 생성되는 빈 오브젝트를 모두 싱글톤으로 만든다. applicationContext 는 싱글톤을 관리하는 싱글톤 레지스트리 이기도 하다.

매번 클리아언트에서 요청이 올 때 마다 오브젝트를 새로 만들어서 사용하지 않아 서버의 부하를 줄이는 효과를 볼 수 있다. 이런 서비스 오브젝트의 개념은 서블릿에서 사용되며 사용자의 요청을 담당하는 여러 스레드에서 하나의 오브젝트를 공유해 동시에 사용하게 된다.

하지만 디자인패턴에서 보는 싱글톤 패턴 구현에는 사용의 한계가 있다.

생성자는 private 으로 만들어서 밖에서는 오브젝트를 생성할 수가 없게 한다. 이로 인해 상속을 할 수 가 없게 되었다. 객체지향의 장점인 상속과, 다형성을 적용할 수 없게 되었고 테스트도 어렵게 되었다.

서버 환경에서는 JVM 의 분산 설치 등 하나 이상의 오브젝트가 생성 될 수 있으므로 하나만 만들어 지는 것을 보장할 수 없다.

그리고 싱글톤은 전역상태로 사용될 수 있기 때문에 적당하지 못하다.


스프링에서는 싱글톤 형태의 오브젝트를 만들고 관리하는 **싱글톤 레지스트리\*라는 기능을 제공한다.**

이는 private 생성자와 static 메소드를 사용하지 않아도 되는 등의 객체지향적인 설계 방식에 아무런 제약이 없게 된다.

하지만 이런 싱글톤 레지스트를 사용함으로서 가져야 할 주의사항이 있다. 멀티스레드 환경에서 여러 스레드가 동시에 접근해서 사용을 하게 되므로 해당 객체는 상태정보를 내부에 갖고 있지 않은 무상태 방식으로 만들어 져야 한다.

```
public class UserDao {
   private ConnectionMaker connectionMaker; /* 초기 설정 후 바뀌지 않는 읽기 전용 */
   private Connection c; /* 매번 새로운 값으로 바뀌는 정보를 담은 인스턴스 변수 */
   private User user; /* 매번 새로운 값으로 바뀌는 정보를 담은 인스턴스 변수 */

   public User get(String id) throws ClassNotFoundException, SQLException {
      ....
   }
}
```

위 코드에서 connectionMaker 는 한번 설정하고 나면 바뀌지 않는 읽기 전용 인스턴스 변수 이므로 상관없지만
매번 새로운 값으로 바뀌는 정보를 담는 c 와 user 는 로컬변수로 정의되거나 파라미터로 주고 받아야 한다.

스프링에서 빈의 스코프는 기본적으로 싱글톤 외에도 매번 새로 생성되는 prototype 등의 스코프, http 요청이 생길때마다 생성되는 요청request스코프나 세션 스코프가 있다.

**빈의 스코프 scope** : 스프링이 관리하는 빈의 생성, 적용 등의 범위


## 1.7 의존관계 주입(DI) ##

**제어의 역전(IoC)과 의존관계 주입(DI)**

스프링은 IoC 의 대표적인 동작원리인 의존관계 주입이라는 의도가 명확히 드러날 수 있게 DI 라고 불린다.
DI의 핵심은 오브젝트 레퍼런스를 외부로부터 주입받고 이를 통해 다른 오브젝트와 다이내믹하게 의존관계가 만들어 지는 것이다.

**의존관계**

의존관계는 방향성이 있다. A가 B에 의존하고 있지만, 반대로 B는 A에 의존하지 않아 B는 A의 변화에 영향을 받지 않는다.


처음에 작업했던 UserDao 는 ConnectionMaker 에 의존하고 있었지만 인터페이스의 도입으로 인터페이스 구현 클래스와 관계가 느슨해지며서 변화에 영향을 덜 받는, 결합도가 낮아진 상태가 되었다.

UML 에서 말하는 설계 모델의 관점에서의 의존관계는 모델이나 코드에서 클래스와 인터페이스를 통해 드러나는 의존관계이다.

해당 의존관계는 이와 성격이 다른 런타임 의존관계는 개발자나 운영자가 사전에 어떤 클래스의 오브젝트를 쓸지 미리 정해놓을 수는 있지만 그 것이 UserDao나 ConnectionMaker 등의 설계와 코드 속에서는 드러나지 않는다는 것이다.

의존관계 주입은 이렇게 구체적인 의존 오브젝트와 그것을 사용할 주체, 보통 클라이언트라고 부르는 오브젝트를 런타임 시에 연결해주는 작업을 말한다. 이는 다음 세가지 조건을 충족하는 작업을 말한다.

**모델이나 코드에는 런타임시점의 의존관계가 드러나지 않는다. 인터페이스에만 의존하고 있어야 한다.**

**런타임 시점의 의존관계는 컨테이너나 팩토리 같은 제3의 존재가 결정한다.**

**의존관계는 사용할 오브젝트에 대한 레퍼런스를 외부에서 주입해줌으로써 만들어진다.**


UserDao에서 DConnectionMaker 를 직접 생성했을 때는 런타임시의 의존관계가 코드속에서 미리 결정되어 있었다.

그래서 IoC 방식을 써서 해당 코드를 제거하고 DaoFactory 를 도입해서 런타임시에 UserDao 가 사용할 ConnectionMaker 인터페이스를 구현한 오브젝트를 결정하고 이를 생성한 후 UserDao 의 생성자 파라미터로 주입해서 UserDao 와 DConnectionMaker 가 런타임 의존관계를 맺게 해준다.
이는 DaoFactory (제3의 존재) 를 만든 시점에 의존관계 주입을 이용한 것이다.

```
public class UserDao {
   private ConnectionMaker connectionMaker; /* 의존관계를 갖는 오브젝트는 인스턴스 변수에 담는다. */
   public UserDao(ConnectionMaker connectionMaker) {
      this.connectionMaker = connectionMaker;
   }
   .....
}
```

**의존관계 검색(DL)과 주입(DI)**

의존관계 검색은 자신이 필요로 하는 의존 오브젝트를 능동적으로 찾는다.

```
public UserDao() {
   DaoFactory daoFactory = new DaoFactory();
   this.connectionMaker = daoFactory.connectionMaker();
}
```

위 코드는 IoC 의 개념은 따르고 있지만 외부로부터의 주입이 아니라 스스로 IoC 컨테이너인 DaoFactory 에게 요청을 하고 있다.

```
public UserDao() {
   ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
   this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);
}
```

의존관계 검색으로 바꿔본 위 코드는 getBean() 이라는 메소드로 정해놓은 이름을 전달해서 그 이름에 해당하는 오브젝트를 찾게 되므로 일종의 검색이라고 볼 수 있다.

의존관계 검색은 IoC의 원칙도 맞고 주입의 장접도 가지고 있으나 코드 안에 오브젝트 팩토리 클래스나 스프링API 가 나타나기 때문에 의존관계 주입방식을 사용하는 편이 낫다.

하지만 UserDaoTest 처럼 적어도 한번은 의존관계 검색 방식을 사용해서 오브젝트를 가져와야 하는데 이런 서블릿은 스프링이 미리 만들어서 제공하고 있다.

의존관계 검색과 의존관계 주입에는 중요한 차이점이 있다.
의존관계 검색은 검색하는 오브젝트는 자신이 스프링의 빈일 필요가 없지만
의존관계 주입은 자기 자신도 컨테이너가 관리하는 스프링의 빈 오브젝트이어야 한다는 것이다. (UserDao)

**메소드를 이용한 의존관계 주입**

지금까지는 생성자를 이용해서 의존관계를 주입했지만 수정자 메소드를 이용한 주입, 일반 메소드를 이용한 주입도 가능하다.

다음은 수정자 메소드를 이용한 방식이다.

```
public class UserDao {
   private ConnectionMaker connectionMaker;
   public void setConnectionMaker(ConnectionMaker connectionMaker) {
      this.connectionMaker = connectionMaker;
   }
} 
```

```
@Bean
public UserDao userDao() {
   UserDao userDao = new UserDao();
   userDao.setConnectionMaker(connectionMaker());
   return userDao;
}
```

하지만 수정자 메소드처럼 set 으로 시작하지 않고, 여러개의 파라미터를 가지고 싶다면 일반 메소드를 이용한다.

## 1.8 XML을 이용한 설정 ##

애노테이션으로 설정된 application context 클래스를 XML 로 변경해본다.
ref 속성으로 여전히 의존관계를 설정할 수 있다.
(d사와 n사는 각자 가져가서 알아서 쓸테지만 ref 를 잘 설명할 수 있을 것 같아 예제를 좀 바꾸어 보았다.)

**applicationContext.xml**
```
<beans>
  <bean id="dConnectionMaker" class="springbook...DConnectionMaker" />
  <bean id="nConnectionMaker" class="springbook...NConnectionMaker" />

  <bean id="userDao" class="springbook...UserDao">
     <property name="connectionMaker" ref="dConnectionMaker" />
  </bean>
</beans>
```

xml을 이용해 application context 를 만들었으니 생성자도 같이 바뀐다.

```
ApplicationContext context = new GenericXmlApplicionContext("applicationContext.xml");
```

**DataSource 구현 클래스 이용하기**

(결국 이 길고 긴 설명을 통해 dataSource DI 까지 겨우 왔다.
당연히 가져다 쓰면 되겠지~ 했던 이 dataSource 까지도 아 네가 이 과정을 거쳐왔구나 하고 깊게 생각하게 된다ㅋ)

```
<bean id="dataSource" class="org.springframewokr.jdbc.datasource.SimpleDriverDataSource">
   <property name="driverClass" value="com.mysql.jdbc.Driver" />
   <property name="url" value="jdbc:mysql://localhost/springbook" />
   <property name="username" value="spring" />
   <property name="password" value="book" />
</bean>

<bean id="userDao" class="springbook...UserDao">
   <bean id="dataSource" ref="dateSource" />
</bean>
```


나는 처음 개발할 때 applicationContext 를 xml 로만 작성해서인지
애노테이션에 적응이 덜 되서 인지
한 눈에 파악할 수 있는 XML 방식이 더 편하고 좋은 것 같다.
여러분의 생각은 어떠신지요??


# 생각하기 #

"여기 xml 에서 정의하시고 블라블라블라~ 그리고 setter 없으면 에러나요!"

약 1년전, 스트러츠만 하다가 스프링프로젝트에 처음 들어왔을 때
동료가 설명해준 스프링에 대한 나의 기억이다.

이 기억들은 스프링교육이나 책을 공부하면서 점점 의미를 더해가고 살이 붙어지고 있다.
그렇다고 왜 에러가 나는지 몰랐다는 뜻은 아니다. 다만 IoC, DI 라는 개념을 생각해 봤을 때
나는 그저 "그냥 원래 그런거야~" 라고 깊게 생각하지 않고 개발을 해왔던 것 같다.

겨우 1장을 통해서 일단 급한대로 개발하면서 지나쳐 왔던 객체지향을 다시금 보게 되었고,

이제 나도 다른사람에게 스프링을 알려줄 때
"xml을 통해 DI 를 받고 ... 이러이러 저러저러 해서 요러요러 하다" 라고 설명해줄 수 있겠...지?