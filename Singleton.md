# Introduction #

## 싱글톤에 대한 내용정리 ##

**일반적인 오브젝트 호출**

```

DaoFactory factory = new DaoFactory();
UserDao dao1 = factory.userDao();
UserDao dao2 = factory.userDao();

System.out.println(dao1);
System.out.println(dao2);

```

> 출력결과
```
  springbook.dao.UserDao@118f375
  springbook.dao.UserDao@117a8bd
```

출력결과를 보면 각기 다른 오브젝트가 2개가 생성된다.
userDao를 매번 호출하면 새로운 오브젝트가 생성이 되고 힙 영역에 여러개의 객체가 올라간다.

**애플리케이션 컨텍스트를 이용한 오브젝트 호출
```
   ApplicationContext context = 
                     new AnnotationConfigApplicationContext(DaoFactory.class);

   UserDao dao3 = context.getBean("userDao", UserDao.class);
   UserDao dao4 = context.getBean("userDao", UserDao.class);

   System.out.println(dao3);
   System.out.println(dao4);

```**

> 출력결과
```
 springbook.dao.UserDao@ee22f7
 springbook.dao.UserDao@ee22f7
```

스프링의 애플리케이션 컨텍스트를 이용한 방식으로 하면 매번 새로운 객체를 생성하지 않는다.
이렇게 스프링은 기본적으로 빈 오브젝트를 모두 싱글톤으로 만든다.

## 생각해보기 ##
스프링이 싱글톤으로 빈을 만드는 이유로 생각한것이 서버의 부하때문이라고 생각을 했었다. 매번 클라이언트가에서 요청할때마다 객체가 수십에서 수백만개씩 만들어지면 아무리 자바 오브젝트 생성과 가비지 컬렉션의 성능이 좋아졌다고 하더라도 부하가 걸리면 서버가 감당하기 힘들어서 싱글톤패턴이 적용됐다고 생각을 했다.
하지만 팀장님께서 서버부하가 주 이유이기 보다는 객체의 상태때문이라고 하신거 같다. 객체의 상태라고 하면 어떤거 의미하는지 좀 생각해봐야 겠다~