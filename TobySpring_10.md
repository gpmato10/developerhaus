

# 정리 #

## 10.1 IoC 컨테이터: 빈 팩토리와 애플리케이션 컨텍스트 ##

### IoC컨테이너 정의 ###
  * 오브젝트의 생성과 관계설정, 사용, 제거 등의 작업을 컨테이너가 담당
  * 오브젝트의 제어권을 컨테이너가 가지고 있어 IoC라 부름
  * 스프링 컨테이너를 Ioc컨테이너라고도 함
  * 스프링에선 IoC를 담당하는 컨테이너를 빈 팩토리 또는 애플리케이션 컨텍스트라고 부르기도함

```
  public interface ApplicationContext extends ListableBeanFactory,
  HierarchicalBeanFactory, MessageSource, ApplicationEventPublisher,
  ResourcePatternResolver {
```

  * 스프링의 빈 팩토리와 애플리케이션 컨텍스트는 각각 기능을 대표하는 BeanFactory와 ApplicationContext라는 두 개의 인터페이스로 정의
  * 스프링 컨테이너 또는 IoC 컨테이너라고 말하는 것은 바로 이 ApplicationContext 인터페이스를 구현한 클래스의 오브젝트

### IoC컨테이너를 이용해 애플리케이션 만들기 ###

```
  StaticApplicationContext ac = new StaticApplicationContext();
```

  * ApplicationContext 인터페이스를 구현한 클래스인 StaticApplicationContextdml 오브젝트 생성
  * 본격적인 IoC 컨테이너로서 동작하려면 POJO클래스와 설정 메타정보 필요

#### POJO 클래스 ####
  * POJO는 특정 기술과 스펙에서 독립적일뿐더러 의존관계에 있는 다른 POJO와 느슨한 결합을 갖도록 만듬.

![http://dl.dropbox.com/u/19052099/Study/POJO.jpg](http://dl.dropbox.com/u/19052099/Study/POJO.jpg)

#### 설정 메타정보 ####
  * IoC 컨테이너의 가장 기초적인 역할은 오브젝트를 생성하고 이를 관리
  * 스프링 컨테이너가 관리하는 이런 오브젝트는 빈이라 부름
  * 스프링의 설정 메타정보는 XML 파일이 아님
  * 스프링의 설정 메타정보는 BeanDefinition 인터페이스로 표현되는 순수한 추상 정보
  * 애플리케이션 컨텍스트는 BeanDefinition으로 만들어진 메타정보를 담은 오브젝트를 사용해 IoC와 DI 작업 수행
  * 스프링의 메타정보는 특정한 파일 포맷이나 형식에 종속되지 않음
  * 원본의 포맷과 구조, 자료의 특성에 맞게 읽어와 BeanDefinition 오브젝트로 변환해주는 BeanDefinitionReader있으면 됨

  * 스프링 IoC컨테이너는 각 빈에 대한 정보를 담은 설정 메타정보를 읽어들인 뒤에 이를 참고해서 빈 오브젝트를 생성하고 프로퍼티나
생성자를 통해 의존 오브젝트를 주입해주는 DI작업을 수행.
  * DI를 연결되는 오브젝트들이 모여서 하나의 애플리케이션을 구성하고 동작
  * 스프링 애플리케이션이란 POJO 클래스와 설정 메타정보를 이용해 IoC 컨테이너가 만들어주는 오브젝트의 조합

```
StaticApplicationContext ac = new StaticApplicationContext();
ac.registerSingleton("hello1", Hello.class);

Hello hello1 = ac.getBean("hello1", Hello.class);
assertThat(hello1, is(notNullValue()));
```
  * StaticApplicationContext는 코드에 의해 설정 메타정보를 등록하는 기능을 제공하는 애플리케이션 컨텍스트

```
BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
helloDef.getPropertyValues().addPropertyValue("name", "Spring");
ac.registerBeanDefinition("hello2", helloDef);
```

  * BeanDefinition타입의 설정 메타정보를 만들어서 IoC 컨테이너에 등록하는 방법
  * RootBeanDefinition은 가장 기본적인 BeanDefinition 인터페이스 구현클래스

```
 Hello hello2 = ac.getBean("hello2", Hello.class);
 assertThat(hello2.sayHello(), is("Hello Spring"));
 
 assertThat(hello1, is(not(hello2)));
 assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
```

  * 빈은 오브젝트 단위로 등록되고 만들어지기 때문에 같은 클래스 타입이더라도 두 개를 등록하면 서로 다른 빈 오브젝트가 생성

#### StaticApplicationContext ####
  * 코드를 통해 빈 메타정보를 등록하기 위해 사용
  * 스프링의 기능에 대한 학습 테스트를 만들 때를 제외하면 실제로 사용되지 않음

#### GenericApplicationContext ####
  * 가장 일반적인 애플리케이션 컨텍스트의 구현 클래스
  * StaticApplicationContext와는 다르게 XML 파일과 같은 외부의 리소스에 있는 빈 설정 메타정보를 리더를 통해 읽어들여서 메타정보로 전환해서 사용

```
   <bean id="hello" class="springbook.learningtest.spring.ioc.bean.Hello">
	<property name="name" value="Spring" />
        <property name="printer" ref="printer" />
   </bean>

   <bean id="printer" class="springbook.learningtest.spring.ioc.bean.StringPrinter" />
```
  * XML로 만든 빈 설정 메타정보
```
 @Test
 public void genericApplicationContext() {
	GenericApplicationContext ac = new GenericApplicationContext();
	XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
	reader.loadBeanDefinitions("springbook/learningtest/spring/ioc/genericApplicationContext.xml");

        ac.refresh();

	Hello hello = ac.getBean("hello", Hello.class);   
        hello.print();

	assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
```
  * XmlBeanDefinitionReader는 스프링의 리소스 로더를 이용해 XML 내용을 읽어옴
  * 스프링을 이용하면서 GenericApplicationContext를 직접 이용할 일은 거의 없음(코드에서 직접 만들고 초기화하지 않을 뿐이지 실제로 자주 사용)
  * 스프링의 JUnit 테스트는 테스트 내에서 사용할 수 있도록 애플리케이션 컨텍스트를 자동으로 만들어주는데 이때 생성되는 애플리케이션 컨텍스트가 바로 GenericApplicationContext

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loactions = "/test-applicationContext.xml")
public class UserServiceTest {
 @Autowired ApplicationContext applicationContext;
 ...
```
  * 테스트가 실행되면서 GenericApplicationContext가 생성과 동시에 XML 파일을 읽어오고 초기화까지 수행

#### GenericXmlApplicationContext ####
  * GenericApplicationContext 와 XmlBeanDefinitionReader 클래스가 결합된 형태
  * GenericXmlApplicationContext는 XmlBeanDefinitionReader를 내장하고 있기 때문에 XML 파일을 읽어들이고 refresh()를 통해 초기화하는 것까지 한 줄로 끝냄

```
GenericApplicationContext ac = new GenericXmlApplicationContext("springbook/learningtest/spring/ioc/genericApplicationContext.xml");

Hello hello = ac.getBean("hello", Hello.class);
...
```

#### WebApplicationContext ####

  * 스프링 애플리케이션에서 가장 많이 사용되는 애플리케이션 컨텍스트
  * 이름 그대로 웹 환경에서 사용할 때 필요한 기능이 추가된 애플리케이션 컨텍스트
  * XML 설정파일을 사용하도록 만들어진 XmlWebApplicationContext가 가장 많이 사용
  * 스프링 IoC 컨테이너는 빈 설정 메타정보를 이용해 빈 오브젝트를 만들고 DI작업을 수행
  * 이것만으로는 애플리케이션이 동작하지 않고 어디에선가 특정 빈 오브젝트의 메소드를 호출함으로써 애플리케이션을 동작시킴

### IoC 컨테이너 계층구조 ###
  * 모든 애플리케이션 컨텍스트는 부모 애플리케이션 컨텍스트를 가질 수 있음 (이를 이용해 트리구조의 컨텍스트 계층을 만들 수 있음)
  * 계층구조 안의 코든 컨텍스트는 각자 독립적인 설정정보를 이용해 빈 오브젝트를 만들고 관리
  * 각자 독립적으로 자신이 관리하는 빈을 갖고 있긴 하지만 DI를 위해 빈을 찾을때는 부모 애플리케이션 컨텍스트의 빈까지 검색
  * 자신이 관리하는 빈 중에서 필요한 빈을 찾아보고 없으면 부모 컨텍스트에게 빈을 찾아달라고 요청
  * 부모 컨텍스트에서도 없으면 부모 컨텍스트의 부모 컨텍스트에게 까지 다시요청하고 이렇게 계층구조를 따라 가장 위에 존재하는 루트 컨텍스트까지 요청이 전달
  * 중요한건 부모 컨텍스트에게만 빈 검색을 요청하지 자식, 형제 컨텍스트에는 요청하지 않음
  * 검색 순서는 자신이 먼저이고 다음 직계 부모 순서
  * 부모 컨텍스트와 같은 이름의 빈을 자신이 정의해서 갖고 있다면 자신것이 우선

```
   <bean id="printer" class="springbook.learningtest.spring.ioc.bean.StringPrinter" />
   
   <bean id="hello" class="springbook.learningtest.spring.ioc.bean.Hello">
	<property name="name" value="Parent" />
        <property name="printer" ref="printer" />
   </bean>

```
  * 부모(루트) 컨텍스트가 사용하는 설정파일

```
   <bean id="hello" class="springbook.learningtest.spring.ioc.bean.Hello">
	<property name="name" value="Child" />
        <property name="printer" ref="printer" />
   </bean>
```
  * 자식 컨텍스트가 사용하는 설정파일

```
 ApplicationContext parent = new GenericXmlApplicationContext(basePath + "parentContext.xml");
```
  * 부모 컨텍스트는 더 이상 상위에 부모 컨텍스트가 존재하지 않는 루트 컨텍스트
  * 루트컨텍스트는 반드시 스스로 완전한 빈 의존관계를 보장(자신 외에는 다른 데서 필요한 빈을 찾을 방법이 없기 때문)

```
 GenericApplicationContext child = new GenericApplicationContext(parent);
```
  * 애플리케이션 컨텍스트를 생성할 때 앞에서 만든 parent를 부모 컨텍스트로 지정

```
 XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
 reader.loadBeanDefinitions(basePath + "childContext.xml");
 child.refresh();
```
  * 자식 컨텍스트가 사용할 설정정보를 읽어들이고 초기화
  * 설정 메타정보를 읽고 refresh() 해주면 컨텍스트를 초기화하면서 DI를 진행
  * 이때 child 컨텍스트에 필요한 빈이 존재하지 않을 경우 부모 컨텍스트에게 빈검색을 요청

### 웹 애플리케이션의 IoC 컨테이너 구성 ###

  * 스프링에서는 보통 독립적으로 배치 가능한 웹 모듈 형태로 애플리케이션을 배포
  * 하나의 웹 애플리케이션은 여러 개의 서블릿을 가질 수 있음
  * 최근에는 많은 웹 요청을 한번에 받을 수 있는 대표 서블릿을 등록해두고 공통적인 선행 작업을 수행하게 한 후에 각 요청의 기능을 담당하는 핸들러라고 불리는 클래스를 호출하는 방식으로 개발
  * 몇 개의 서블릿이 중앙집중식으로 모든 요청을 다 받아서 처리하는 이런 방식을 프론트 컨트롤러 패턴이라 함
  * 스프링 웹 애플리케이션에 사용되는 서블릿의 숫자는 하나이거나 많아야 두셋 정도
  * 스프링 애플리케이션의 요청을 처리하는 서블릿 안에서 만들어지는 것과 웹 애플리케이션 레벨에서 만들어지는 두가지 방식으로 웹 애플리케이션 안에서 동작하는 IoC컨테이너가 만들어짐

#### 웹 애플리케이션의 컨텍스트 계층구조 ####

  * 웹 애플리케이션 레벨에 등로되는 컨테이너는 보통 루트 웹 애플리케이션 컨텍스트라 불림
  * 루트 웹 애플리케이션 컨텍스트는 서블릿 레벨에 등록되는 컨테이너들의 부모 컨테이너가 되고 최상단에 위치한 루트 컨텍스트
  * 웹 애플리케이션에는 하나 이상의 스프링 애플리케이션의 프론트 컨트롤러 역할을 하는 서블릿이 등록될 수 있음
  * 이런 경우 각 서블릿이 공유하게 되는 공통적인 빈들이 있고 이런 빈들을 웹 애플리케이션 레벨의 컨텍스트에 등록(공통되는 빈듯이 서블릿별로 중복돼서 생성되는 걸 방지)
![http://dl.dropbox.com/u/19052099/Study/Application.jpg](http://dl.dropbox.com/u/19052099/Study/Application.jpg)
  * 서블릿 A와 서블릿 B는 각각 자신의 전용 애플리케이션을 갖고 있음
  * 동시에 두 컨텍스트가 공유해서 사용하는 빈을 담아놓을 수 있는 별도의 컨텍스트가 존재
  * 이 컨텍스트는 각 서블릿에 존재하는 컨텍스트의 부모 컨텍스트(또 최상단에 있으므로 계층구조로 볼때는 루트 컨텍스트)
  * 스플링에서 애플리케이션 컨텍스트 계층구조가 사용되는 경우

  * 일반적으로는 스프링의 애플리케이션 컨텍스트를 가지면서 프론트 컨트롤러 역할을 하는 서블릿은 하나만 만들어 사용
![http://dl.dropbox.com/u/19052099/Study/Application2.jpg](http://dl.dropbox.com/u/19052099/Study/Application2.jpg)
  * 웹 기술에 의존적인 부분과 그렇지 않은 부분을 구분
  * 스플링을 이용하는 웹 애플리케이션이라고 해서 반드시 스프링이 제공하는 웹 기술을 사용해야는 건 아님


  * 스프링은 웹 애플리케이션마다 하나씩 존재하는 서블릿 컨텍스트를 통해 루트 애플리케이션 컨텍스트에 접근할 수있는 방법을 제공

```
   WebApplicationContextUtils.getWebApplicationContext(SevletContext sc)
```

  * 스프링의 간단한 유틸리티 메소드를 이용하면스프링 밖의 어디서라도 웹 애플리케이션의 루트 애플리케이션 컨텍스트를 얻을 수 있음(getBean() 메소드를 사용하면 루트 컨텍스트의 빈을 가져와 쓸수 있음)
  * SevletContext는 웹 애플리케이션마다 하나씩 만들어지는 것으로, 서블릿의 런타임 환경정보를 담고있음

#### 웹 애플리케이션의 컨텍스트 구성방법 ####

  * 서블릿 컨텍스트와 루트 애플리케이션 컨텍스트 계층 구조
> > - 가장 많이 사용되는 기본적인 구성 방법
> > - 스프링 웹 기술을 사용하는 경우 웹 관련 빈들은 서블릿의 컨텍스트에 두고 나머지는 루트 애플리케이션 컨텍스트에 등록

  * 루트 애플리케이션 컨텍스트 단일구조
> > - 스프링 웹 기술을 사용하지 않고 서드파티 웹 프레임워크나 서비스 엔진만을 사용해서 프리젠테이션 계층을 만든다면 스프링 서블릿을 둘 이유 없음(서블릿의 애플리케이션 컨텍스트 사용하지 않음)

  * 서블릿 컨텍스트 단일구조
> > - 스프링 웹 기술을 사용하면서 스프링 외의 프레임워크나 서비스 엔진에서 스프링의 빈을 이용할 생각이 아니라면 루트 애플리케이션 생략 가능
> > - 서블릿에서 만들어지는 컨텍스트에 모든 빈을 등록

#### 루트 애플리케이션 컨텍스트 등록 ####
  * 이벤트 리스너를 이용해 루트 웹 애플리케이션 컨텍스르르 등록
  * 스프링은 웹 애플리케이션의 시작과 종료 시 발생하는 이벤트를 처리하는 리스너인 ServletContextLister를 이용
  * 웹 애플리케이션이 시작될 때 루트 애플리케이션 컨텍스트를 만들어 초기화하고, 웹 애플리케이션이 종료될 때 컨텍스트를 함께 종료하는 기능을 가진 리스너를 만듬(스프링은 ContextLoaderListener를 제공)

```
  <listner>
	<listner-class>org.springframework.web.context.ContextLoderListener
     </listner-class>
  </listner>
```

  * 애플리케이션 컨텍스트 클래스 : XmlWebApplicationContext
  * XML 설정파일 위치 : /WEB-INF/applicationContext.xml
  * 컨텍스트 클래스와 설정파일 위치는 서블릿 컨텍스트 파라미터를 선언해서 변경가능

```
   <context-param>
	<param-name>contextConfigLocation</param-name>
 	<param-value>
  		/WEB-INF/daoContext.xml
 		/WEB-INF/applicationContext.xml
   	</param-value>
   </context-param>
```

  * 디폴트 설정 파일 위치인 /WEB-INF/applicationContext.xml은 무시되고 파라미터로 제공된 설정파일을 사용

```
	<param-value>classpath:applicationContext.xml</param-value>
```

  * 리소스 로더가 사요아는 접두어를 사용해서 표현 할 수 있음

```
   <context-param>
	<param-name>contextClass</param-name>
 	<param-value>org.springframework.web.context.support.AnnoationConfigWebApplicationContext
   	</param-value>
   </context-param>
```

  * 애플리케이션 컨텍스트 구현 클래스 변경
  * 여기서 사용될 컨텍스트는 반드시 WebApplicationContext 인터페이스를 구현

#### 서블릿 애플리케이션 컨텍스트 등록 ####
  * 스프링의 웹 기능을 지원하는 프론트 컨트롤러 서블릿은 DispatcherServlet
  * 서블릿 이름을 다르게 지정해주면 하나의 웹 애플리케이션에 여러 개의 DispatcherServlet 등록
  * 각 DispatcherServlet은 서블릿이 초기화 될 때 자신만의 컨텍스트를 생성하고 초기화

```
    <servlet>
        <servlet-name>spring</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
```

  * DispatcherServlet에 의해 만들어지는 애플리케이션 컨텍스트는 모두 독립적인 네임스페이스를 가지고 있음
  * 이 네임스페이스가 서블릿 단위로 만들어지는 컨텍스트를 구분하는 키
  * 네임스페이스는 servlet-name으로 지정한 서블릿 이름에 -servlet을 붙여서 만듬
  * DispatcherServlet이 사용할 디폴트 XML 설정파일의 위츠를 네임스페이스를 이용해서 만듬
> > - '/WEB-INF/' + 서블릿네임스페이스 + '.xml'
  * load-on-startup은 서블릿 컨테이너가 등록된 서블릿을 언제 만들고 초기화할지, 또 그 순서는 어떻게 되는지를 지정하는 정수 값
  * 이 항목을 생략하거나 음수를 넣으면 해당 서블릿은 임의로 정한 시점에서 만들어지고 초기화
  * 0이상의 값을 넣으면 웹 애플리케이션이 시작되는 시점에서 서블릿을 로딩하고 초기화
  * 여러개 서블릿이 등록되어 있다면 작은 수를 가진 서블릿이 우선만들어짐

```
  <servlet>
    <servlet-name></servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatchServlet</servlet-class>
    <init-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>
	     /WEB-INF/application.xml
	     /WEB-INF/spring-servlet.xml
	</param-value>		
    </init-param>
     <load-on-startup>1</load-on-startup>
  </servlet>
```

  * DispatcherServlet의 컨텍스트에 대한 디폴트 설정을 변경하고 싶다면 루트 애플리케이션 컨텍스트와 마찬가지로 contextConfigLocation과 contextClass를 지정
  * context-param대신 servlet안에 있는 init-param을 이용한다는 점만 다름



## 10.2 IoC/DI를 위한 빈 설정 메타정보 작성 ##

  * IoC 컨테이너
    * 코드를 대신해 애플리케이션을 구성하는 오브젝트를 생성하고 관리하는 역할을 한다.
    * 빈 설정 메타정보를 통해 빈의 클래스와 이름을 제공 받아 자신이 만들 오브젝트를 파악한다.
      * 빈 설정 메타정보는 파일이나 애노테이션 같은 리소스로부터 전용 리더를 통해 BeanDefinition 타입의 오브젝트로 변환되고 이 정보를 IoC 컨테이너가 활용한다.

### 10.2.1 빈 설정 메타정보 ###
  * BeanDefinition
    * beanClassName(필수) : 빈 오브젝트의 클래스 이름. 빈 오브젝트는 이 클래스의 인스턴스가 된다.
    * parentName : 빈 메타정보를 상속받을 부모 BeanDefinition의 이름. 빈의 메타정보는 계층구조로 상속할 수 있다.
    * factoryBeanName : 팩토리 역할을 하는 빈을 이용해 빈 오브젝트를 생성하는 경우에 팩토리 빈의 이름을 지정한다.
    * factoryMethodName : 다른 빈 또는 클래스의 메소드를 통해 빈 오브젝트를 생성하는 경우 그 메소드 이름을 지정한다.
    * scope : 빈 오브젝트의 생명주기를 결정하는 스코프를 지정한다. 크게 싱글톤과 비싱글톤 스코프로 구분할 수 있다.(디폴트 = 싱글톤)
    * lazyInit 빈 오브젝트의 생성을 최대한 지연할 것인지를 지정한다. 이 값이 true이면 컨테이너는 빈 오브젝트의 생성을 꼭 필요한 시점까지 미룬다.(디폴트 = false)
    * dependsOn : 먼저 만들어져야 하는 빈을 지정할 수 있다. 빈 오브젝트의 생성 순서가 보장돼야 하는 경우 이용한다. 하나 이상의 빈 이름을 지정할 수 있다.
    * autowireCandidate : 명시적인 설정이 없어도 미리 정해진 규칙을 가지고 자동으로 DI 후보를 결정하는 자동와이어링의 대상으로 포함시킬지의 여부(디폴트 = true)
    * primary : 자동와이어링 작업 중에 DI 대상 후보가 여러 개가 발생하는 경우가 있다. 이때 최종 선택의 우선권을 부여할지 여부. primary가 지정된 빈이 없이 여러 개의 후보가 존재하면 자동와이어링 예외가 발생한다.(디폴트 = false)
    * abstract : 메타정보 상속에만 사용할 추상 빈으로 만들지의 여부. 추상 빈이 되면 그 자체는 오브젝트가 생성되지 않고 다른 빈의 부모를 빈으로만 사용된다.(디폴트 = false)
    * autowireMode : 오토와이어링 전략. 이름, 타입, 생성자, 자동인식 등의 방법이 있다.
    * dependencyCheck : 프로퍼티 값 또는 레퍼런스가 모두 설정되어 있는지를 검증하는 작업의 종류(디폴트 : 체크하지 않음)
    * initMethod : 빈이 생성되고 DI를 마친 뒤에 실행할 초기화 메소드의 이름
    * destoryMethod : 빈의 생명주기가 다 돼서 제거하기 전에 호출할 메소드의 이름
    * propertyValues : 프로퍼티의 이름과 설정 값 또는 레퍼런스. 수정자 메소드를 통한 DI 작업에서 사용한다.
    * annotationMetadata : 빈 클래스에 담긴 애노테이션과 그 애트리뷰트 값. 애노테이션을 이용하는 설정에서 활용한다.

빈은 오브젝트이기 때문에 빈을 생성하기 위해서는 클래스가 반드시 필요하다. 그렇기 때문에 위의 메타정보 항목 중에 가장 중요한 것은 클래스이름이다. 클래스 이름과 빈의 아이디 또는 이름이 있으면 간단한 빈을 정의할 수 있다.


### 10.2.2 빈 등록 방법 ###

> 빈을 등록할 때에는 보통 XML 문서, 프로퍼티 파일, 소스코드 애노테이션과 같은 외부 리소스로 빈 메타정보를 작성하고 적절한 리더나 변환기를 통해 애플리케이션 컨텍스트가 사용할 수 있는 정보로 변환해주는 방법을 사용한다.

  * XML:`<bean>` 태그
```
<bean id="hello" class="springbook.learningtest.spring.ioc.bean.Hello">
	<property name="printer">
		<bean class="springbook.learningtest.spring.ioc.bean.StringPrinter" />
	</property>
</bean>
```
    * `<beans>` 엘리먼트 하위에 `<bean>` 태그가 존재한다.
    * 빈 설정 안에 정의하는 내부 빈(inner bean) 을 이용해 특정 빈에서 참조하는 bean을 설정할 수 있다.
  * XML:네임스페이스와 전용 태그
    * 컨텍스트가 사용하는 설정정보를 담은 빈과 애플리케이션 로직을 담은 빈이 동일하게 `<bean>` 태그를 이용해 만들어지면 구분하기가 힘들다. 또한 한눈에 빈을 등록한 의도와 의미를 파악하기 힘들다.
    * 네임스페이스와 전용 태그, 전용 애트리뷰트를 이용해 선언하면 내용이 분명하게 드러나고 선언도 깔끔해지고 애플리케이션 로직을 담은 다른 빈 설정과 혼동되지도 않는다.
    * 예 : 포인트컷
```
<bean id="mypointcut" class="org.springframework.aop.aspectj.AspectJExpressionPointcut">
	<property name="expression" value="execution(* *..*ServiceImpl.upgrade*(..))" />
</bean>
```
```
<aop:pointcut id="mypointcut" expression="execution(* *..*ServiceImpl.upgrade*(..))" />
```
    * 예 : 내장형 DB 선언
```
<jdbc:embedded-database id="embeddedDatabase" type="HSQL">
	<jdbc:script location="schema.sql" />
</jdbc:embedded-database>
```
    * 예 : 커스텀 태그
```
<app:module id-prefix="user" class-prefix="User" package="com.mycompany.user" />
```
```
<bean id="userController" class="com.mycompany.user.UserController">
	<property name="service" ref="userService" />
</bean>
<bean id="userService" class="com.mycompany.user.UserService">
	<property name="dao" ref="userDao" />
</bean>
<bean id="userDao" class="com.mycompany.userUserDao">
</bean>
```
  * 자동인식을 이용한 빈 등록: 스테레오타입 애노테이션과 빈 스캐너
    * XML 문서와 같이 한곳에 명시적으로 선언하지 않고 스프링 빈으로 등록하는 방법
> > 특정 애노테이션이 붙은 클래스를 찾아서 빈으로 등록해주는 방식
    * 스테레오타입(stereotype) 애노테이션 : 디폴트 필터에 적용되는 애노테이션(@Component, @Repository, @Service, @Controller)
```
@Component
public class AnnotatedHello {
...
}
```
    * 클래스 이름의 첫 글자를 소문자로 바꾼 것을 빈의 아이디로 사용한다.
    * 빈 이름 지정 방법
```
@Component("myAnnotatedHello")
public class AnnotatedHello {
...
}
```
    * 복잡한 XML 문서를 생성하거나 관리할 필요가 없어지고 그만큼 개발 속도를 향상시킬 수 있다.
    * 애플리케이션에 등록된 빈이 어떤것들인지, 그 정의는 어떻게 되는지 한눈에 파악할 수 없다.
    * 상세한 메타정보 항목을 지정할 수 없고, 클래스당 한 개 이상의 빈을 등록할 수 없다.
    * 개발 중에는 생산성을 위해 빈 스캐닝 기능을 사용해 빈을 등록하고, 운영 시점이 되면 다시 XML 형태의 빈 선언을 적용하는 것도 좋다.
    * 자동인식을 통한 빈 등록 방법
      * XML을 이용한 빈 스캐너 등록
        * XML 설정 파일에 `<context:component-scan>` 태그를 넣어주면 된다.
```
<context:component-scan base-package="springbook.learningtest.spring.ioc.bean" />
```
      * 빈 스캐너를 내장한 애플리케이션 컨텍스트 사용
        * 루트 컨텍스트가 XML 대신 빈 스캐너를 이용해 빈을 등록하도록 web.xml 안에 컨텍스트 파라미터를 설정하는 예
```
<context-param>
	<param-name>contextClass</param-name>
	<param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
</context-param>

<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>springbook.learningtest.spring.ioc.bean</param-value>
</context-param>
```
        * `<param-value>`에 하나 이상의 스캔 대상 패키지를 지정할 때에는 각 패키지 사이에 공백을 넣어준다.

빈 클래스 자동인식을 위해 @Component 외에 몇 가지 종류가 더 있는데, 계증별로 빈의 특성이나 종류를 나타내기도 하고, AOP의 적용 대상 그룹을 만들기 위해 여러가지 애노테이션을 사용한다.

  * 스테레오타입 애노테이션의 종류
| **스테레오타입 애노테이션** | **적용 대상** |
|:---------------------------------------|:------------------|
| @Repository                            | 데이터 액세스 계층의 DAO 또는 리포지토리 클래스에 사용된다. DataAccessException 자동변환과 같은 AOP의 적용 대상을 선정하기 위해서도 사용된다. |
| @Service                               | 서비스 계층의 클래스에 사용된다. |
| @Controller                            | 프레젠테이션 계층의 MVC 컨트롤러에 사용된다. 스프링 웹 서블릿에 의해 웹 요청을 처리하는 컨트롤러 빈으로 선정된다. |

특정 계층으로 분류하기 힘든 경우에는 @Component를 사용한다.
또한 직접 커스텀 태그를 설정할 수도 있다.
```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface BusinessRule {
	String value() default "";
}
```

  * 자바 코드에 의한 빈 등록: @Configuration 클래스의 @Bean 메소드
    * 하나의 클래스 안에 여러 개의 빈을 정의할 수 있다.
    * 애노테이션을 이용해 빈 오브젝트의 메타정보를 추가할 수 있다.
    * 클래스 자체가 자동인식 빈의 대상이 되기 때문에 XML을 통해 명시적으로 등록하지 않아도 된다.
```
//<beans>
@Configuration
public class AnnotatedHelloConfig {
	//<bean>, 메소드명이 빈의 이름이 된다.
	@Bean
	public AnnotatedHello annotatedHello() {
		//컨테이너가 리턴된 오브젝트를 빈으로 활용한다.
		return new AnnotatedHello();
	}
}
```
    * @Configuration이 붙은 클래스에 @Bean이 붙은 메소드 뿐만 아니라 해당 클래스 자체도 빈으로 등록된다.
    * 생성된 빈은 메타정보의 디폴트 값에 따라 **싱글톤\*으로 생성된다. 그렇기 때문에 컨테이너 안에서 한번만 만들어진다.
    * 일반 자바 클래스와는 달리 싱글톤으로 객체가 생성되기 때문에 @Configuration과 @Bean을 사용해 만들어진 클래스는 순수한 오브젝트 팩토리 클래스라기 보다는 자바 코드로 표현되는 메타정보라고 이해하는게 좋다.
    * 자바 코드를 이용한 빈 등록은 단순한 빈 스캐닝을 통한 자동인식으로 등록하기 힘든 기술 서비스 빈의 등록이나 컨테이너 설정용 빈을 XML 없이 등록하려고 할 때 유용하게 쓰인다.
    * `<bean>`을 이용한 SimpleDriverDataSource
```
<bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource" >
	<property name="driverClass" value="com.mysql.jdbc.Driver" />
	<property name="url" value="jdbc:mysql://localhost/testdb" />
	<property name="username" value="spring" />
	<property name="password" value="book" />
</bean>
```
    * 자바 코드를 이용한 dataSource 빈 설정
```
@Configuration
public class ServiceConfig {
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/testdb");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");

		return dataSource;
	}
...
}
```
      * 자바 코드에 의한 설정이 외부 설정파일을 이용하는 것보다 유용한 점
        * 컴파일러나 IDE를 통한 타입 검증이 가능하다.
          * XML 문서의 빈 설정에 문제가 없는지 확인하려면 컨테이너를 띄워서 예외가 발생하는지 확인해야 한다.
          * SpringIDE가 제공하는 XML 에디터를 사용하면 기본적인 검증과 자동완성을 이용할 수 있지만 서버환경이나 SpringIDE를 사용할 수 없다면 불편하다.
          * 자바 코드로 작성할 경우 컴파일 에러로 오류를 쉽게 검증할 수 있다.
        * 자동완성과 같은 IDE 지원 기능을 최대한 이용할 수 있다.
          * 자바코드로 빈 설정 메타정보를 작성하면 XML보다 양이 많아 보이지만 자바 에디터의 자동완성을 이용해 일일이 타이핑 하지 않고도 작성하기 쉽다.
        * 이해하기 쉽다.
          * 스프링에 익숙하지 않다면 XML 설정파일에 익숙하지 않아 자바 코드로 설정된 것을 더 쉽게 이해할 수 있다.
        * 복잡한 빈 설정이나 초기화 작업을 손쉽게 적용할 수 있다.
          * 복잡한 빈 설정은 보통 팩토리 빈을 만들어 사용하는데 팩토리빈은 매번 독립적인 클래스늘 만들어야 하고 XML로 등록해서 프로퍼티 정보를 설정해줘야 한다.
          * @Bean 메소드를 이용하면 하나의 클래스 안에 여러개의 빈을 만들 수 있고, 다양한 초기화 작업을 수행할 수 있다.(new 대신 스태틱 팩토리 메소드 등을 이용해 빈 오브젝트 생성 가능)**

  * 자바 코드에 의한 빈 등록: 일반 빈 클래스의 @Bean 메소드
    * 일반 POJO 클래스에 @Bean을 사용할 경우 새로운 빈을 정의하는 설정 메타정보로 사용되고, 그 리턴 오브젝트가 빈 오브젝트가 된다.
    * @Configuration 클래스 안에 @Bean을 사용할 경우 빈이 싱글톤으로 생성되기 때문에 여러번 호출돼도 하나의 오브젝트가 리턴되지만, 일반 클래스에 @Bean을 사용할 경우 싱글톤으로 생성되지 않기 때문에 DI코드를 주의해서 작성해야 한다.
      * HelloService 클래스가 Printer 빈을 직접 DI 받은 뒤에 이를 hello()와 hello2() 메소드가 사용하게 만들어 해결한다.
```
public class HelloService {
	private Printer printer;

	public void setPrinter(Printer printer) {
		this.printer = printer;
	}

	@Bean
	private Hello hello() {
		Hello hello = new Hello();
		hello.setPrinter(this.printer);
		return hello;
	}
	
	@Bean
	private Hello hello2() {
		Hello hello = new Hello();
		hello.setPrinter(this.printer);
		return hello;
	}

	@Bean
	private Printer printer() {
		return new StringPrinter();
	}
}
```
      * 밀접한 의존관계를 갖는 종속적인 빈을 정의할때 유용하게 쓰이지만, 일반 애플리케잉션 코드와 설정정보가 함께 존재하기 때문에 유연성이 떨어진다.

  * 빈 등록 메타정보 구성전략
    * XML 단독 사용
      * 컨텍스트에서 생성되는 모든 빈을 XML에서 확인할 수 있지만, 빈의 개수가 많아지만 XML을 관리하기 번거로울 수 있다.
      * 모든 설정정보를 자바 코드에서 분리하고 순수한 POJO 코드를 유지하고 싶을 때 사용한다.
      * 애플리케이션 컴포턴트 상당 부분이 기존에 개발됬고, 가능한 한 코드에 손대지 않고 재사용 하고 싶은 경우에 사용한다.
      * 운영 중에 관리의 편의성을 위해 XML 설정으로 변경하기도 한다.
      * 커스텀 스키마와 전용 태그를 만들어 사용할 경우 사용하기도 한다.
    * XML과 빈 스캐닝의 혼용
      * 자동인식 방식으로 등록하기 불편한 기술 서비스, 기반 서비스, 컨테이너 설정 등의 빈은 XML을 사용한다.
      * 자동인식 방식으로 설정하기 어려운 DataSource, 트랜잭션 메니저 등은 `<bean>` 태그를 사용해 설정한다.
      * 웹 애플리케이션의 이중 컨텍스트 계층 구조와 빈 검색 우선순위를 잘 이해하고 빈 스캐닝 설정을 제공할 때 중복 등록이 발생하지 않도록 주의해야 한다.
      * XML과 빈 스캐닝을 함께 사용할 경우 XML을 사용하는 애플리케이션 컨텍스트를 기본으로 하고, 빈 스캐너를 context 스키마의 `<context:component-scan>` 태그를 이용해 등록해 준다.
```
<context:component-scan base-package="com.mycompany.app" />
```
    * XML 없이 빈 스캐닝 단독 사용
      * 애플리케이션 컴포턴트 및 각종 기술 서비스와 컨테이너 설정용 빈까지 모두 스캔으로 자동등록시킨다.
      * 자동인식을 위한 애노테이션을 부여할 수 있는 애플리케이션 컴포넌트 클래스는 빈 스캔 대상으로 삼고, 기존에 XML로 등록했던 기술 서비스 빈과 컨이너 설정용 빈 등은 @Configuration 자바 코드를 이용한 설정 메타정보로 만든다. 그리고 @Configuration 클래스들을 모두 빈 스캔 대상에 포함시킨다.
        * 장점 : 모든 빈의 정보가 자바 코드에 담겨 있으므로 빈의 설정정보를 타입에 안전한 방식으로 작성할 수 있다.
        * 단점 : 스프링이 제공하는 스키마에 정의된 전용 태그를 사용할 수 없다.(aop, tx 등의 전용 태그에 대응되는 빈을 @Configuration 클래스 안에 직접 등록해주면 모든 기능을 다 쓸 수는 있다.)

### 10.2.3 빈 의존관계 설정 방법 ###

  1. 명시적으로 구체적인 빈을 지정하는 방법
    1. DI 할 빈의 아이디를 직접 지정한다.
  1. 일정한 규칙에 따라 자동으로 선정하는 방법
    1. 자동와이어링(autowiring) 이라고 불리며, 타입 비교를 통해서 호환되는 타입의 빈을 DI 후보로 삼는다.

**빈 등록 방식과 빈 의존관계 주입 방법은 메타정보 작성 방법이 같지 않아도 된다.**

  * XML: `<property>`,  `<consturctor-arg>`
    * `<property>`: 수정자주입
      * 자바빈 규약을 따르는 수정자 메소드를 사용한다.
      * ref 애트리뷰트 통해 빈 이름을 이용해 주입할 빈을 찾는다.
```
<bean ...>
	<property name="printer" ref="defaultPrinter" />
</bean>

<bean id="defaultPrinter" class="..." />
```
      * value 애트리뷰트를 이용하면 단순 값 또는 빈이 아닌 오브젝트를 주입할 수 있다.
```
<property name="name" value="Spring" />
<property name="age" value="30" />
<property name="myClass" value="java.lang.String" />
```
      * value 애트리뷰트를 통해 정수, 실수, 스트링과 같은 기본적인 값 외에도 다양한 종류의 클래스를 주입할 수 있다.
    * `<constructor-arg>`: 생성자 주입
      * 생정자의 파라미터를 이용해 여러개의 오브젝트를 주입할 수 있다.
      * 파라미터의 순서나 타입을 명시해서 사용 할 수 있다. 특히 같은 타입이 여러개 있는 경우에 주의해서 사용해야 한다.
      * 예시
        * 빈으로 사용할 클래스의 생성자
```
public class Hello {
	...
	public Hello(String name, Printer printer) {
		this.name = name;
		this.printer = printer;
	}
}
```
        * index 지정 방법
```
<bean id="hello" class="springbook.learningtest.spring.ioc.bean.Hello">
	<constructor-arg index="0" value="Spring" />
	<constructor-arg index="1" ref="printer" />
</bean>
```
        * 파라미터에 중복되는 타입이 없다면 타입으로 구분할 수 있다.
```
<constructor-arg type="java.lang.String" value="Spring" />
<constructor-arg type="springbook.learningtest.spring.iocbean.Printer" ref="printer" />
```
        * 최적화를 위해 컴파일 과정에서 클래스 파일의 디버깅 심벌을 제거하지 않았다면 파라미터 이름을 사용할 수도 있다.
```
<constructor-arg name="name" value="Spring" />
<constructor-arg name="printer" ref="printer" />
```

  * XML: 자동와이어링
    * 명시적으로 프로퍼티나 생성자 파라미터를 지정하지 않고 미리 정해진 규칙을 이용해 자동으로 DI 설정을 컨테이너가 추가하도록 만드는 것으로 자동으로 관계가 맺어져야 할 빈을 찾아서 연결해 준다는 의미로 자동와이어링 이라고 부른다.
    * byName: 빈 이름 자동와이어링
      * 프로퍼티 이름과 참조 빈 이름이 동일할 경우
```
<bean id="hello" ...>
	<property name="printer" ref="printer" />
</bean>

<bean id="printer" class="...StringPrinter" />
```
      * 이름을 이용한 자동와이어링 적용
```
<bean id="hello" class="...Hello" autowire="byName">
	<property name="name" value="Spring" />
	//<property name="printer" ref="printer" /> 는 자동와이어링을 통해 컨테이너가 자동으로 추가해준다.
</bean>

<bean id="printer" class="...StringPrinter" />
```
      * Hello 클래스에 setPrinter() 메소드가 있고, 이름이 같은 printer빈이 있으니 hello 빈의 printer 프로퍼티는 printer 빈을 DI 한다고 판단한다.
      * 모든 빈의 프로퍼티에 대해 이름이 동일한 빈을 찾아서 연결해주고, 프로퍼티와 이름이 같은 빈이 없을 경우에는 무시한다.
      * 모든 빈에 자동와이어링을 설정할 것이라면 루트태그`<beans>`에 디폴트 자동와이어링 옵션을 변경해도 된다.
```
<beans default-autowire="byName">
	<bean>...</bean>
	...
</beans>
```
    * byType: 타입에 의한 자동와이어링
      * 프로퍼티의 타입과 각 빈의 타입을 비교해서 자동으로 연결해주는 방법법
      * `<bean>`에 autowire="byType"을 넣어주거나 `<beans>`에 default-autowire="byType"을 넣어주고 사용할 수 있다.
      * 설정 예
```
<bean id="hello" class="...Hello" autowire="byType" ...</bean>
<bean id="mainPrinter" class="...StringPrinter" />
```
      * 타입에 의한 방식은 빈의 이름이나 프로퍼티 이름에 신경을 쓰지 않아도 되기 때문에 편리하다.
      * 하지만 같은 타입의 빈이 두 개 이상 존재하는 경우에는 적용할 수 없다.
      * 프로퍼티의 개수가 많아지면 자동 와이어링 대상이 아님에도 한 번씩 모든 빈의 타입과 비교하는 작업이 일어나야 한다.
      * 이름에 의한 자동와이어링은 프로퍼티 이름과 빈의 아이디를 비교하면 되지만 타입을 비교하면 이름을 비교할때보다 느리다.
      * 생성자 자동와이어링을 적용하려면 autowire="constructor" 애트리뷰트를 이용하면 된다.
    * 자동와이어링을 이용하면 XML 설정 파일의 양을 줄여줄 수 있다.
    * XML에서 자동 와이어링을 이용하면 XML만 보고 빈 사이의 의존관계를 알기 힘들다.
    * 코드만 보고 어떤 프로퍼티에 DI가 일어날지 간단히 알 수 없다.(빈 클래스에 프로퍼티가 있다고 항상 DI 되지 않는다. 그에 대응되는 빈이 존재해야 한다.)
    * 오타로 빈 이름을 잘못 넣으면 DI 되지 않는다.
    * 타입에 의한 자동와이어링은 같은 타입이 두개 이상 주입되면 문제가 발생한다.
    * 하나의 빈에 한 가지 자동와이어링 방식밖에 지정할 수 없다.
    * 보통 클래스당 하나의 빈이 등록되는 DAO나 서비스 계층 빈과 같은 경우에는 타입에 의한 자동와이어링이 편리하다. 반면에 기술 서비스 빈이나 기반 서비스 빈 같은 경우에는 동일한 타입의 빈이 하나 이상 등록될 가능성이 많기 때문에 이름에 의한 자동 와이어링을 적용하는 것이 낫다.

  * XML: 네임스페이스와 전용 태그
    * 스키마를 정의해서 사용하는 전용 태그는 `<bean>` 처럼 명확하게 `<property>` 또는 `<constructor-arg>` 라는 DI용 태그로 고정되어 있지 않기 때문에 의존관계를 지정하기가 쉽지 않다.
    * OXM을 위한 마샬러 빈을 oxm 스키마의 태그를 이용해 선언
```
<oxm:jaxb2-marshaller id="unmarshaller" contextPath="..." />
```
    * Marshaller 인터페이스를 구현한 빈이 하나 만들어질 것이고, 그 아이디는 unmarshaller 라는 것을 알 수 있다. 다른 빈에 DI 할때는 ref 값으로 넣어준다.
```
<bean id="sqlService" class="springbook.user.sqlservice.OxmSqlService">
	<property name="unmarshaller" ref="unmarshaller" />
	<property name="sqllRegistry" ref="sqlRegistry />
</bean>
```
    * 전용 태그로 만들어지는 빈이 일반 `<bean>` 태그로 선언된 빈을 DI할 수 있다.
    * 일반적으로 스프링의 전용 태그는 보통 -ref 로 끝나는 애트리뷰트를 이용해 DI할 빈을 지정하는 관례가 있다.
    * AOP 어드바이저 빈을 `<aop:advisor>`로 선언했을 때 DI 받을 advice 빈의 아이디는 advice-ref로, 포인트컷 빈의 아이디는 pointcut-ref로 지정할 수 있다.
    * 포인트컷 표현식을 내장하는 경우에는 ref가 붙지 않은 pointcut 애트리뷰트를 사용한다. -ref가 붙는 경우와 붙지 않는 경우에 차이가 있으므로 주의해서 사용해야 한다.
```
<aop:config>
	<aop:advisor advice-ref="transactionAdvice" pointcut="bean(*service)" />
</aop:config>

<bean id="transaciontAdvice" ... />
```
    * 전용 태그도 내부적으로 `<bean>`으로 선언한 것과 동일하게 빈 메타정보가 만들어지므로 자동와이어링 대상이 될 수 있다. 하지만 가능하면 XML안에서 잘 파악되지 않는 방식은 적용하지 않는게 좋다.

  * 애노테이션: @Resource
    * `<property>` 선언과 비슷하게 주입할 빈을 아이디로 지정하는 방법이다.
    * 자바 클래스의 수정자 뿐만 아니라 필드에도 붙일 수 있다. 또한 @Resource 애노테이션을 사용하면 수정자 메소드가 없어도 직접 내부 필드에 DI 할 수 있다.
    * **수정자 메소드**
```
public class Hello {
	private Printer printer;
	...
	//<property name="printer" ref="printer" />와 동일한 의존관계 메타정보로 변환된다.
	@Resource(name="printer")
	public void setPrinter(Printer printer) {
		this.printer = printer;
	}
}
```
      * 자바 빈의 수정자 메소드의 관례를 따라서 메소드 이름으로부터 프로퍼티 이름을 끌어낸다.
      * @Resource 와 같은 애노테이션으로 된 의존관계 정보를 이용해 DI 가 이뤄지게 하려면 다음 세가지 중 하나를 선택해야 한다.
        1. XML의 `<context:annotation-config />`
          * @Resource와 같은 애노테이션 의존 관계 정보를 읽어서 메타정보를 추가해주는 기능을 가진 빈 후처리기를 등록해주는 전용 태그
```
<?xml version="1.0" encoding="UTF-8">
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context-3.0.xsd">

	<context:annotation-config />
	...
```
        1. XML의 `<context:component-scan />`
          * 빈 스캐닝을 통한 빈 등록 방법을 지정하는 것인데, 내부적으로 첫 번째 태그로 만들어지는 빈을 함께 등록해준다.
        1. AnnotationConfigApplicationContext 또는 AnnotationConfigWebApplicationContext
          * 아예 빈 스캐너와 애노테이션 의존관계 정보를 읽는 후처리기를 내장한 애플리케이션 컨텍스트를 사용한다.
```
<context:component-scan base-package="...ioc.annotation" />
```
    * **필드**

  * 애노테이션: @Autowired / @Inject
    * **수정자 메소드와 필드**
    * **생성자**
    * **일반 메소드**
    * **컬렉션과 배열**
    * **@Qualifier**
    * **@javax.inject.Inject**
    * **@javax.inject.Qualifier**

  * @Autowired와 getBean(), 스프링 테스트

  * 자바 코드에 의한 의존관계 설정
    * 애노테이션에 의한 설정 @Autowired, @Resource
    * @Bean 메소드 호출
    * @Bean과 메소드 자동와이어링

  * 빈 의존관계 설정 전략
    * XML 단독
    * XML과 애노테이션 설정의 혼합
    * 애노테이션 단독


## 10.3 프로토타입과 스코프 ##


## 10.4 기타 빈 설정 메타정보 ##



# 생각하기 #