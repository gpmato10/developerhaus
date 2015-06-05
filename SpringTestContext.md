# Introduction #

스프링 테스트 컨텍스트를 정리한다.


# Details #

## 스프링 테스트 컨텍스트 계층 구조 ##

![http://developerhaus.googlecode.com/files/testcase.jpg](http://developerhaus.googlecode.com/files/testcase.jpg)

## 각 컨테스트별 기능 ##
Spring 통합 테스트 클래스들은 다양한 테스트 환경을 고려하여 설계되어 있으며 org.springframework.test 패키지에 존재합니다.

  * 어떤 테스트 컨텍스트를 사용해야 할지 결정하기전 고려할 사항
    1. 초기화 또는 관리를 위한 추가적인 코드 작성 없이 Spring 애플리케이션 컨텍스트를 사용할 것인가
    1. 데이터 처리를 테스트 할 것인가(데이터 소스를 사용하여)
    1. 트랜잭션 내부에서 메소드를 테스트 할것인가(트랜잭션 관리자를 사용하여)
    1. JDK 버전, 만일 JDK 1.4를 사용한다면 JDK 1.5에서 추가된 Annotation 기능의 혜택을 이용하지 못합니다.

### AbstractDependencyInjectionSpringContextTests ###
> 테스트 의존성을 주입하여 특별히 Spring 어플리케이션 컨텍스트를 룩업할 필요가 없도록 합니다. getCinfigLocation() 메소드를 통해 정의된 환경설정 파일을 읽어들여 적절한 객체를 자동으로 배치합니다.

### AbstractTransactionalDataSourceSpringContextTests ###
> 트랜잭션 내부에서 실행되는 코드를 테스트 하는데 사용되며 각 테스트 케이스의 트랜잭션을 생성하고 롤백합니다. 우리는 트랜잭션이 주어져 있다고 가정하고 코드를 작성합니다. 이것은 JdbcTemplate와 같은 필드를 제공하여, 테스트 오퍼레이션후 데이터베이스 상태를 확인하는데 사용되거나 어플리케이션 코드에 의해 수행된 질의의 결과를 확인하는데 사용됩니다. ApplicationContext 또한 상속되어, 필요한 경우 명시적인 룩업에 사용될 수 있습니다.

### AbstractJpaTests ###
> 이 테스트 클래스는 JPA기능을 테스트 하는데 사용됩니다. 이것은 JPA메소드 호출시 사용할 EntityManager 인스턴스를 제공합니다.

### AbstractJpaTests ###
이 클래스는 AbstractJpaTests를 확장하며 AspectJ사용시 로드타임위빙(load-time weaving. LTW)목적으로 사용됩니다. 우리는 getActualAopXmlLocation()메소드를 오버라이드 하여 AspectJ 설정 xml파일의 위치를 기술합니다.

### AbstractModelAndViewTests ###
이것은 프레젠테이션 및 컨트롤러 계층(Spring MVC에서)을 테스팅하기 위해 편리한 기능을 제공합니다.