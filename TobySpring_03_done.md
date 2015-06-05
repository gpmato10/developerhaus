

# 내용정리 #

## 1. DAO 코드 흐름 정리 ##
  * 예외처리 → 고정되는 부분 VS 로직에 따라 변하는 부분 : 변하는 부분을 메소드로 분리 → 분리한 메소드를 다른 곳에 적용하지 못함, DAO 로직 마다 새롭게 만들어서 확장돼야 하는 문제 발생 → 템플릿 메소드 패턴으로 분리 → DAO 로직마다 상속을 통해 새로운 클래스를 만들어야 하는 문제 발생 → 전략 패턴 적용(deleteAll()은 context / PreparedStatement를 만들어주는 기능은 전략, Connection은 전략의 인터페이스) → 컨텍스트 안에서 전략 클래스인 DeleteAllStatement를 사용하도록 고정되고 있는 문제 발생 → 컨텍스트에 해당하는 부분을 별도의 메소드로 독립(jdbcContextWithStatementStrategy), 클라이언트를 책임을 담당하는 delteAll() 메소드 재구성 → add 메소드에도 적용 → user라는 부가적인 정보가 필요하기 때문에 컴파일 에러 발생 → 클라이언트로 부터 User 타입 Object를 받을 수 있도록 AddStatement 생성자를 통해 제공받게 함, add() 메소드에 user 정보를 생성자를 통해 전달하도록 수정 → DAO 메소드 마다 새로운 StatementStrategy 구현 클래스를 만들어야 하는 문제, 부가적인 정보가 있는 경우 오브젝트를 전달받는 생성자와 이를 저장해둘 인스턴스 변수를 만들어야 하는 문제 발생 → UserDao 클래스 안에 내부 클래스로 정의(로컬 클래스로 정의) → 익명 내부 클래스로 좀 더 간결하게 클래스 이름을 제거 → 적용된 구조를 다시 확인해보면, 익명 내부 클래스로 만들어지는 것은 개별적인 "전략", jdbcContextWithStatementStrategy() 메소드는 "컨텍스트", UserDao 내의 PreparedStatement를 실행하는 기능은 "컨텍스트 메소드" →  jdbcContextWithStatementStrategy()메소드는 다른 DAO에서도 사용가능하기 때문에 클래스로 분리 → 오브젝트 간의 의존관계를 스프링 설정에 적용(jdbcContext 빈 추가) → JdbcContext를 빈으로 등록하는 대신 UserDao 내부에서 직접 DI를 적용(setDataSource에 jdbcContext 의존 오브젝트 주입) →  바뀌지 않는 부분을 빼내서 executeSql() 메소드로 적용 → 재사용 가능한 콜백을 담고 있는 메소드 executeSql을 JdbcContext로 옮김

## 2. DAO 코드에 적용된 템플릿 기법 ##
  * 템플릿 메소드 패턴 http://code.google.com/p/spring3-study-with-cwp/wiki/TemplateMethodPattern
  * 전략 패턴 http://code.google.com/p/spring3-study-with-cwp/wiki/StrategyPattern
  * 템플릿/콜백 패턴
    * 전략패턴 기본 구조에 익명 내부 클래스 활용한 방식
    * 작업 흐름 : ①Callback 생성(클라이언트) → ②Callback 전달 / Template 호출 → ③Workflow 시작(템플릿) → ④참조정보 생성(템플릿) → ⑤Callback 호출 / 참조정보 전달 → ⑥Client final 변수 참조(콜백) → ⑦작업 수행(콜백) → ⑧Callback 작업 결과 → ⑨Workflow 진행(템플릿) → ⑩Workflow 마무리(템플릿) → ⑪Template 작업 결과

  * ①StatementStrategy Callback 생성 → ②Callback 전달 / workWithStatementStrategy 호출 → ③Connection 준비 → ④Connection정보 생성 → ⑤Callback 호출 / Connection정보 전달 → ⑥final user 정보 참조 → ⑦PrepareStatement 만들기 → ⑧PrepareStatement 작업 결과 전달 → ⑨PrepareStatement 사용 → ⑩executeUpdate실행(또는 executeQuery 등) → ⑪void 전달

> ![http://lh5.ggpht.com/_ISarakVcQjw/TQ41kXDVKdI/AAAAAAAAAFk/7iQnWT0ah9g/%ED%85%9C%ED%94%8C%EB%A6%BF%EC%BD%9C%EB%B0%B1%ED%8C%A8%ED%84%B4.jpg](http://lh5.ggpht.com/_ISarakVcQjw/TQ41kXDVKdI/AAAAAAAAAFk/7iQnWT0ah9g/%ED%85%9C%ED%94%8C%EB%A6%BF%EC%BD%9C%EB%B0%B1%ED%8C%A8%ED%84%B4.jpg)

  * JdbcContext 안에 클라이언트와 템플릿, 콜백이 동작하는 구조로 개선

> ![http://lh3.ggpht.com/_ISarakVcQjw/TQ4EDjKqbVI/AAAAAAAAAFc/RYrG7GjEpxw/jdbcContext.jpg](http://lh3.ggpht.com/_ISarakVcQjw/TQ4EDjKqbVI/AAAAAAAAAFc/RYrG7GjEpxw/jdbcContext.jpg)

## 3. 템플릿 / 콜백의 응용 ##
  * 고정된 작업 흐름을 갖고 있으면서 여기저기서 자주 반복되는 코드가 있다면, 중복되는 코드를 분리할 방법을 생각해보는 습관을 기르자.
  * 가장 전형적인 템플릿/콜백 패턴의 후보 try/catch/finally 블록을 사용하는 코드
  * 템플릿/콜백을 적용할 때는 템플릿과 콜백의 경계를 정하고 템플릿이 콜백에게, 콜백이 템플릿에게 각각 전달하는 내용이 무엇인지 파악하는게 중요
  * 제네릭스를 이용한 콜백 인터페이스 : 결과 타입이 Integer 타입으로 고정되어 있었다. → 다양한 오브젝트 타입을 지원하는 인터페이스나 메소드를 정의하기 위해 제네릭스 이용

## 4. 스프링이 제공하는 템플릿/콜백 기술 ##
  * 스프링이 제공하는 JDBC 코드용 기본 템플릿 : JdbcTemplate
  * update(SQL, 바인딩할 파라미터, ...)
  * queryForInt("Integer 타입의 결과를 가져올 수 있는 SQL")
  * queryForObject(SQL, SQL에 바인딩할 파라미터 값, ResultSet한 결과를 Object에 mapping해주는 RowMapper 콜백) → query의 결과가 Row 하나일때
  * query(SQL, (SQL에 바인딩할 파라미터 값:생략가능), RowMapper 콜백)  → 여러 개의 Row가 결과로 나오는 경우


# 생각하기 #

  * 템플릿/콜백 조사하다가 우연히 토비님의 글( http://toby.epril.com/?p=911 )을 읽게 되었는데요~ 헷갈리는 부분이 있어서요. "코드를 이용하는 수동 DI" 부분에서 템플릿/콜백을 통해 DI를 한것이 맞나요? JdbcContext가 DataSource 타입 빈을 다이나믹하게 주입 받아서 사용해야 하기때문에? 그래서 JdbcContext의 Object를 만들어서 인스턴스 변수에 저장하고, JdbcContext에 UserDao가 DI 받은 Object를 주입하는 방식으로? 제가 이해를 제대로 한건지..ㅎㅎ 궁금합니다~^^

  * 템플릿/콜백 응용에서 마지막 부분에 제네릭스를 이용하고 있잖아요. 그 부분에 대해서 질문이 생겼습니다. 저 같은 경우는 generic type warning이 나면 <?>아니면, @SuppressWarnings를 사용한 적이 있어요. 이런 경우엔 어떻게 해야지 잘하는 방법일까요? 관련 참고글 첨부합니다.
    * [자바 이론과 실습: 제네릭스 해부, Part 1](http://www.ibm.com/developerworks/kr/library/j-jtp04298.html)
    * [자바 이론과 실습: 제네릭스 해부, Part 2](http://www.ibm.com/developerworks/kr/library/j-jtp07018.html)