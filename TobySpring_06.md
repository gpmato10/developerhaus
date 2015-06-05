

# 내용정리 #
> AOP(Aspect Oriented Programming)

> 문제를 해결하기 위한 핵심 관심 사항(core concern, 핵심 로직)과 전체에 적용되는 공통 관심 사항(cross-cutting concern, 공통 기능)을 기준으로 프로그래밍함으로써 공통 모듈을 여러 코드에 쉽게 적용할 수 있도록 해 줌.

![http://developerhaus.googlecode.com/files/aop0001.png](http://developerhaus.googlecode.com/files/aop0001.png)

> 근데 이걸 작성하다가 문득... Filter와는 차이가 뭘까요?


## 6.1 트랜잭션 코드의 분리 ##

> UserService interface를 구현하고 멤버필드로 PlatformTransactionManager, UserService(UserServiceImpl)를 가지는 UserSErviceTx class를 만든다.
> UserServiceImpl class에서는 transaction 코드는 분리한다.

## 6.2 고립된 단위 테스트 ##

> UserServiceTest 내의 upgradeLevels() 테스트의 경우 실제 UserDao, MailSender 객체를 세팅하지 않고 같은 결과를 리턴하는 짝퉁 클래스(MockUserDao, MockMailSender)를 만들어 세팅한다. 첫번째 고립된 단위 테스트 방법이고, 두번째는 Mockito 클래스를 이용해서 하는 방법이 있다.
> Mockito를 좀더 자세히 들여다 보면 재미있을 것 같다.

## 6.3 다이내믹 프록시와 팩토리 빈 ##

> 우선 선행 학습이 필요하다.
> java.lang.reflect을 테스트 해보는 jdk 폴더 내의 Reflaction.java 그리고 DynamicProxyTest.java
> spring 폴더 내의 테스트도 꼭 해 봐야 한다.

## 6.4 스프링의 프록시 팩토리 빈 ##

## 6.5 스프링 AOP ##

## 6.6 트랜잭션 속성 ##

> ### 6.6.1 트랜잭션 정의 ###

> 트랜잭션 전파

  * ROPAGATION
> REQUIRED : 이미 tx가 존재할 경우, 해당 tx에 참여 / tx가 없을 경우, 신규 tx를 생성하고 실행

> SUPPORTS : 이미 tx가 존재할 경우, 해당 tx에 참여 / tx가 없을 경우, 그냥 실행

> MANDATORY : 이미 tx가 존재할 경우, 해당 tx에 참여 / tx가 없을 경우, Exception 발생

> REQUIRES\_NEW : 이미 tx가 존재할 경우, 해당 tx를 suspend 시키고 신규 tx를 생성 / tx가 없을 경우, 신규 tx를 생성

> NOT\_SUPPORTED : 이미 tx가 존재할 경우, 해당 tx를 suspend 시키고 tx 없이 실행 / tx가 없을 경우, 그냥 실행

> NEVER : 이미 tx가 존재할 경우, Exception 발생 / tx가 없을 경우, tx 없이 실행

> NESTED : 이미 tx가 존재할 경우, 해당 tx에 참여 / tx가 없을 경우, nested tx 실행

  * SOLATION
> DEFAULT : datastore에 의존

> READ\_UNCOMMITED : Dirty Reads, Non-Repeatable Reads, Phantom Reads 발생

> READ\_COMMITED : Dirty Reads 방지, Non-Repeatable Reads, Phantom Reads 발생

> REPEATABLE\_READ : Non-Repeatable Read 방지, Phantom Reads 발생

> SERIALIZABLE : Phantom Read 방지

> 퍼옴 => http://hopangbear.tistory.com/187

> 참고 => http://dev.anyframejava.org/anyframe/doc/core/3.1.0/corefw/guide/transaction.html


> ### 6.6.3 포인트컷과 트랜잭션 속성의 적용 전략 ###

  * 트랜잭션 포인트컷 표현식은 타입 패턴이나 빈 이름을 이용한다.
  * 공통된 메소드 이름 규칙을 통해 최소한의 트랜잭션 어드바이스와 속성을 정의한다.
  * 프록시 방식 AOP는 같은 타깃 오브젝트 내의 메소드를 호출할 때는 적용되지 않는다.

## 6.7 애노테이션 트랜잭션 속성과 포인트컷 ##

## 6.8 트랜잭션 지원 테스트 ##




# 생각하기 #