


# 내용정리 #

**http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/jdbc.html**


## 12. Data access with JDBC ##
### 12.1 Introduction to Spring Framework JDBC ###

Spring Framework JDBC abstraction에서 제공하는 부가가치는 아래의 표에서 가장 잘 보여준다.
이 표는 어떤 action은 Spring이 처리하고, 어떤 action은 당신(application developer)이 처리해야 하는 것을 보여준다.

**Spring JDBC - who does what? (어떤일을 분담할 것인가?)**| **Action** | **Spring** | **You** |
|:-----------|:-----------|:--------|
| connection 파라미터들을 정의하기 |　         | X       |
| connection 오픈 | X          |　      |
| 구체적인 SQL 작성 |　         | X       |
| 파라미터 선언 및 파라미터 값 제공 |　         | X       |
| 질의문 준비 및 실행 | X          |　      |
| 결과에 대한 반복 루프 준비(필요하다면) | X          |　      |
| 반복되는 각 항목에 대한 작업 수행 |　         | X       |
| 예외 처리 | X          |　      |
| 트랜잭션을 다루기 | X          |　      |
| connection 닫기 | X          |　      |


Spring framework는 지루한 API와 같은 Jdbc가 되게하는 모든 낮은 수준의 세부사항들을 다룬다.

#### 12.1.1 JDDBC database access 접근법 선택 ####

JDBC database access를 기반으로 한 몇 몇의 접근법들 중에 선택할수 있다.
JdbcTemplate의 세가지 특징 이외에도, 새로운 Simple Jdbclnsert와 SimplejdbcCall approach는 database meatadata에 최적화 되어 있다. 그리고 the RDBMS object style은 JDO QUery design 수준의 객체지향적 접근법을 취한다.
일단 당신은 이 접근법들을 사용하며 시작해라. 당신은 서로다른 접근법에서의 각 특징들을 훨씬 더 많이 섞어서 사용할 수 있다. 모든 접근법은 JDBC 2.0-compliant driver를 요구한다. 그리고 일부 향상된 JDBC 3.0 driver 특징들을 요구한다.


![http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/images/note.gif](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/images/note.gif) **NOTE**


Spring 3.0은 generics(제네릭스)와 varargs(가변인자)와 같은 Java 5 지원과 함께 다음에 나오는 모든 접근법으로 업데이트 된다.

  * **JdbcTemplate** 은 전통적 Spring JDBC 접근법이며, 가장 유명하다. 이것은 "가장 낮은 레벨"의 접근법이다. 그리고 다른 모든것은 JdbcTemplate 안으로 들어와 사용한다. 그리고 모두 generics와 varargs와 같은 Java 5 지원으로 업데이트된다.

  * **NamedParameterJdbcTemplate** 은 traditional JDBC의 "?"를 대신하여 named parameters를 JdbcTemplate를 제공한다. 이 접근법은 더 나은 문서화와 SQL문의 다수의 매개변수(multiple parameters)를 사용하기 쉽게 제공한다.

  * **SimpleJdbcTemplate** 은 JdbcTemplate과 NamedParameterJdbcTemplate의 사용빈도가 높은 작업들을 결합한다.

  * **SimpleJdbcInsert and SimpleJdbcCall** 은 필요한 환경설정의 양을 제한하기 위하여 database metadata를 최적화한다. 이 접근법은 테이블의 이름 또는 절차와 행이름에 어울리는 parameters의 map을 제공할 필요가 있는 당신을 위하여 코딩을 간단하게 한다. 이것은 단지 데이타베이스가 충분한 메타데이타를 제공할 때만 수행한다. 만약 데이타베이스가 이 메타데이타를 제공하지 못한다면 당신은 parameters의 확실한 환경설정을 제공해야만 할 것이다.

  * **RDBMS Objects including MappingSqlQuery, SqlUpdate and StoredProcedure** 은 data access layer를 초기화 하는 동안에 재사용 가능하고 thread-safe object들을 만들도록 요구한다. 이 접근법은 query string을 정의하고, parameter들을 선언하고, 그리고 query를 컴파일하는 점에서 JDO Query을 모방해서 만들었다 할 수있다. execute methods는 여러 parameter 값들이 지나갈 때 multiple times에 call 될 수 있다.



#### 12.1.2 Package 계층 ####
Spring Framework의 JDBC abstraction framework는 core, datasource, object, support의 서로 다른 4개의 package들로 구성되어 있다.

  * **org.springframework.jdbc.core** package는 JdbcTemplate 클래스와 다양한 callback interface들, 뿐만 아니라 연관된 다양한 클래스까지도 포함한다. org.springframework.jdbc.core.simple로 명명된 subpackage는 SimpleJdbcTemplate 클래스와 그에 연관된 SimpleJdbcInsert 와 SimpleJdbcCall 클래스들을 포함한다. org.springframework.jdbc.core.namedparam 로 명명된 또 다른 subpackage는 NamedParameterJdbcTemplate 클래스와 연관된 support 클래스들을 포함한다. Section 12.2, “Using the JDBC core classes to control basic JDBC processing and error handling”, Section 12.4, “JDBC batch operations”, Section 12.5, “Simplifying JDBC operations with the SimpleJdbc classes”를 참고하길 바람.



  * **The org.springframework.jdbc.datasource** package는 DataSource에 쉽게 접근하기 위한 utility 클래스를 포함한다. 그리고 다양한 simple DataSource를 implementations한다. 그것은 testing 과 Java EE container의 외부의 변경하지 않은 JDBC 코드를 실행할 때 사용할 수 있다. org.springfamework.jdbc.datasource.embedded로 명명된 서브패키지는 HSQL과 H2와 같은 자바 데이터베이스 엔진을 사용하는 in-memory database instances를 만들어 내는 지원을 제공한다. Section 12.3, “Controlling database connections”, Section 12.8, “Embedded database support”를 참고하기바람.


  * **The org.springframework.jdbc.object** package 는 RDBMS queries, updates, and stored procedures as thread safe, reusable objects를 대표하는 클래스를 포함한다. Section 12.6,“Modeling JDBC operations as Java objects"를 참고하길 바람. 이 접근법은 JDO에 의해 만들어졌다. 그러나 물론 쿼리에 의해 되돌아온 object들은 데이터베이스에서 "disconnected" 된다. 이 높은 단계의 JDBC abstraction은  org.springframework.jdbc.core package의 낮은 단계의 abstraction에 의존한다.



  * **The org.springframework.jdbc.support** package 는 SQLException translation functionality과 몇몇의 유틸리티 클래스를 제공한다.JDBC processing 동안에 Exceptions thrown는 org.springframework.dao package에서 정의된 예외로 번역되어 진다. 이것은 Spring JDBC abstraction layer를 사용하는 코드가 JDBC 또는 RDBMS-specific error handling 같은 도구를 필요로 하지 않음을 의미한다. 번역된 예외 모두는 체크되어지지 않았다. 예외를 catch하는 옵션을 준다. 다른 예외들이 caller에게 전달될 수 있도록 허용하는 동안에 당신은 회복할 수 있다. Section 12.2.4,“SQLExceptionTranslator 를 참고 바람.



# 생각하기 #