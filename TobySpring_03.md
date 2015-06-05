# 내용정리 #

## 3.1 다시 보는 초난감 DAO ##
UserDao의 코드에는 아직 문제점이 남아 있다. DB 연결과 관련된 여러 가지 개선 작업은 했지만, 다른
면에서 심각한 문제점이 있는데 바로 예외상황에 대한 처리이다.
DB 커넥션이라는 제한적인 리소스를 공유해 사용하는 서버에서 동작하는 JDBC 코드에서는 정상적인 흐름에 따르지 않고 중간에 어떤 이유로든 예외가 발생했을 경우에도 사용한 리소스를 반드시 반환하도록 만들어야 한다.

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

이 메소드에서는 Connection, PreparedStatement, ResultSet이라는 3개의 공유 리소스를 가져와서 사용한다. 정상적으로 메소드를 마치면 close()를 호출해 리소스를 반환하지만 Connection, PreparedStatement, ResultSet를 처리 중간에 예외가 발생하면 리소스를 반환하지 못하고 메소드를 빠져나가 버린다. 서버에서는 DB커넥션을 제한된 개수를 만들어서 재사용 가능한 풀로 관리하는데 이런 에러가 발생하여 반환을 못하면 어느 순간에 커넥션 풀에 여유가 없어지고 리소스가 모자란다는 심각한 오류를 내며 서버가 중단될 수 있다.

그래서 이런 JDBC 코드에서는 어떤 상황에서도 가져온 리소스를 반환하도록 try/catch/finally 구문 사용을 권장하고 있다.
```
public int getCount() throws SQLException {
        Connection c = null;
	PreparedStatement ps = null;
	ResultSet rs = null;		
		
	try {
		 c = dataSource.getConnection();
			
		 ps = c.prepareStatement("select count(*) from users");
			
		rs = ps.executeQuery();
		rs.next();			
		return rs.getInt(1);
	} catch (SQLException e) {
   	    throw e;
	} finally {
  	   if(rs != null) {
		try{
		    rs.close();
		} catch(SQLException e) {					
		}
	   }
	   if(ps != null) {
		try{
		   ps.close();
		} catch(SQLException e) {					
		}
	   }
	   if(c != null) {
		try{
		   c.close();
		} catch(SQLException e) {					
		}
	   }
	}
    }
```
trt/catch/finally 블록을 적용해서 예외상황에서도 안전한 코드가 됐다.

## 3.2 JDBC try/catch/finally 코드의 문제점 ##
이제 try/catch/finally 블록도 적용돼서 완성도 높은 DAO 코드가 된 UserDao이지만, 막상 코드를 훑어보면 한숨부터 나온다. 복잡한 try/catch/finally 블록이 2중으로 중첩까지 되어 나오는데다, 모든 메소드마다 반복된다. 이러면 복사해서 붙이기를 해서 코드를 작성을 하지만 자칫 finally 블록의 c.close() 라이 하나를 빼먹어도 별문제가 없다. 그러다가 언젠가 서버에서 리소스가 꽉 찾다는 에러가 나면서 서비스가 중단되는 상황이 발생한다.
테스트를 통해 DAO마다 예외상황에서 리소스를 반납하는지 체크하게 하면 좋은데 적용이 쉽지가 않다. 차라리 코드를 열심히 살펴보면서 빠진 부분은 없는지 확인하는 편이 낫다.
이런 코드를 효과적으로 다룰수 있는 방법을 생각해보자. 중복되는 코드와 로직에 따라 자꾸 확장되고 자주 변하는 코드를 분리하는 작업이다.
UserDAO의 메소드 개선작업을 시작해보면 변하는 코드와 변하지 않는 코드를 분리해서 변하지 않는 코드를 재상용할 수 있는 방법을 생각해보자.

**메소드 추출**

```
public void deleteAll() throws SQLException {
  ...
  try {
    c = dataSource.getConnection();
  
    ps = makeStatement(c);

    ps.executeUpdate();
   
  } catch (SQLException e)
   ...
}

private PreparedStatement makeStatement(Connection c) throws SQLException {
   PreparedStatement ps;
   ps = c.preparedStatement("delete from users");
   return ps;
}
```

자주바뀌는 부분을 메소드로 독립시켰지만 별 이득이 없다. 분리시킨 메소드를 다른 곳에서 재사용할 수 있어야 하는데 이건 반대로 분리시키고 남은 메소드가 재사용이 필요한 부분이고, 분리된 메소는 DAO 로직마다 새롭게 만들어서 확장돼야 하는 부분이기 때문이다.

**템플릿 메소드 패턴의 적용**

템플릿 메소드 패턴은 상속을 통해서 기능을 확장해서 사용하자. 변하지 않는 부분은 수펴클래스에 두고 변하는 부분은 추상 메소드로 정의해서 서브클래스에 오버라이드하여 새롭게 정의해 쓰도록 한다.

추출해서 별도의 메소드로 독립시킨 makeStatement() 메소드를 추상메소드 선언으로 변경한다.
abstract protected PreparedStatement makeStatement(Connection c) throws SQLException

그리고 이를 상속하는 서브클래스를 만들어서 메소드를 구현한다.
```
public class UserDaoDeleteAll extends UserDao {

   protected PreparedStatement makeStatement(Connection c) throws SQLException {
    PreparedStatement ps = c.prepareStatment("delete from users");
    return ps;
  }
}
```
하지만 DAO 로직마다 상속을 통해 새로운 클래스를 만들어야 한다. 또한 변하지 않는 코드를 가진 UserDao의 JDBC try/catch/finally 블로과 변하는 PreparedStatement를 담고 있는 서브클래스들이 이미 클래스 레벨에서 컴파일 시점에 이미 그 관계가 결정되어 있어서 유연성이 떨어진다.

**전략 패턴의 적용**

개방 폐쇄 원칙을 잘 지키는 구조이면서도 템플릿 메소드 패턴보다 유연하고 확장성이 뛰어난 것이 오브젝트를 아예둘로 분리하고 클래스 레벨에서는 인터페이스를 통해서만 의존하도록 만드는 전략 패턴이다.

```
 public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection c) throws SQLException;
 }
```

이 인터페이스를 상속해서 바뀌는 부분인 PreparedStatement를 생성하는 클래스부분
```
 public class DeleteAllStatement implements StatementStrategy {
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
      PreparedStatement PS = c.prepareStatement("delete from users");
      return ps;
    }
 }
```

이제 DeleteAllStatement를 UserDao의 deleteAll() 메소드에서 사용해 보자.

```
  public void deleteAll() throws SQLException {
   ...
   try {
     c = dataSource.getConnection();

     StatementStrategy strategy = new DeleteAllStatement();
     ps = strategy.makePreparedStatement(c);

     ps.executeUpdate();
   } catch (SQLException e) {
    ...
  }
 }
```

전략 패턴은 필요 따라 컨텍스트는 그대로 유지되면서 전략을 바꿔 쓸 수 있다는 것인데, 이렇게 컨텍스트 안에서 이미 구체적인 전략 클래스인 DeleteAllStatement를 사용하도록 고정되어 있다면 뭔가 이상하다. 컨텍스트가 인터페이스 뿐만아니라 특정 구현 클래스도 알고 있다는건, 전략패턴에도 OCP에도 잘 들어맞는다고 불 수 없기 때문이다.

**DI 적용을 위한 클라이언트/컨텍스트 분리**

전략 패턴에 따르면 Context가 어떤 적략을 사용하게 할 것인가는 Context를 사용하는 앞단의 Client가 결정하는게 일반적이다.Clinet가 구체적인 전략의 하나를 선택하고 오브제특로 만들어서 Context에 전달하는 것이다. Context는 전달받은 그 Strategy구현 클래스의 오브젝트를 사용한다.
```
 private void jdbcContextWithStatementStrategy(StatementStrategy stmt)throws
          SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();

			ps = stmt.makeStatement(c);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
```

클라이언트 책임을 담당할 deleteAll 메소드

```
 public void deleteAll() throws SQLException {
   StatementStrategy st = new DeleteAllStatement();
   jdbcContextWithStatementStrategy(st);
 }
```

## 3.3 JDBC 전략 패턴의 최적화 ##
이번엔 add() 메소드에 전략패턴을 적용해보자. 먼저 add() 메소드에서 변하는 부분인 PreparedStatement를 만드는 코드를 AddStatment 클래스로 옮겨놓고 적용한다.

```
  public class AddStatement implements StatementStrategy {
      User user;

      public AddStatement(User user) {
        this.user = user;
      }

      public PreparedStatement makePreparedStatement(Connection c) {
         ...
         ps.setString(1, user.getId()); 
         ps.setString(2, user.getName()); 
         ps.setString(3, user.getPassword()); 
         ...
      }   
   }
```

클라이언트 책임을 담당할 add() 메소드

```
   public void add(User user) throws SQLException {
       StatementStrategy st = new AddStatement(user);
       jdbcContextWithStatementStrategy(st);
   }
```

이렇게 코드가 많이 깔끔했지만 2가지 문제가 발생한다.

1. DAO 메소드마다 새로운 StatementStrategy 구현 클래스를 만들어야 하는점.

2. DAO 메소드에서 StatementStrategy에 전달할 User와 같은 부가적인 정보가 있는 경우, 이를 위해 오브젝트를 전달받는 생성자와 이를 저장해둘 인스턴스 변수를 만들어야 하는 점.

로컬 클래스를 이용해서 클래스 파일이 많아지는 문제를 해결해보자.
```
   public void add(final User user) throws SQLException {
      class AddStatement implements StatementStrategy {
         
        public PreparedStatement makePreparedStatement(Connection c) {
         ...
         ps.setString(1, user.getId()); 
         ps.setString(2, user.getName()); 
         ps.setString(3, user.getPassword()); 
         ...
      }   
   }

       StatementStrategy st = new AddStatement();
       jdbcContextWithStatementStrategy(st);
   }
```
로컬 클래스를 사용함으로써 자신이 선언된 곳의 정보에 접근할 수 있다. 내부 메소드는 자신이 정의된 메소드의 로컬 변수에 직접 접근할 수 있기 때문에 생성자를 통해 User 오브젝트를 전달해줄 필요가 없다.

좀더간단한게 클래스 이름도 제거해 보자. 익명 내부 클래스로 만들면 선언과 동시에 오브젝트를 생성한다.
```
   public void add(final User user) throws SQLException {
      class AddStatement implements StatementStrategy {
         
        public PreparedStatement makePreparedStatement(Connection c) {
         ...
         ps.setString(1, user.getId()); 
         ps.setString(2, user.getName()); 
         ps.setString(3, user.getPassword()); 
         ...
      }   
   }

       StatementStrategy st = new AddStatement();
       jdbcContextWithStatementStrategy(new StatementStrategy() { 

         public PreparedStatement makePreparedStatement(Connection c) {
           ...
           ps.setString(1, user.getId()); 
           ps.setString(2, user.getName()); 
           ps.setString(3, user.getPassword()); 
           ...
         }   
        }
      );
   }
```

## 3.4 JdbcContext의 분리 ##
jdbcContextWithStatementStrategy() 메소드를 UserDao 클래스 밖으로 독립시켜서 모든 DAO가 사용할 수 있게 해보자.

분리해서 만들 클래스의 이름을 JdbcContext라고 하고 UserDao에 있던 컨텍스트 메소드를 workWithStatementStrategy()으로 옮겨놓자. 이렇게하면 datasource가 필요한 것은 UserDao가 아니라 JdbcContext가 돼버린다.
```
public class JdbcContext {
	DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;

		try {
			c = dataSource.getConnection();

			ps = stmt.makePreparedStatement(c);
		
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
			if (c != null) { try {c.close(); } catch (SQLException e) {} }
		}
	}
}
```

이렇게 코딩이 되면 UserDao는 이제 JdbcContext에 의존하고 있다. 그런데 JdbcContext는 인터페이스가 아니라 클래스이다. 스프링의 DI는 기본적으로 인터페이스를 사이에 두고 의존 클래스를 바꿔서 사용하는게 목적인데 이 경우 JdbcContext는 그 자체로 독립적인 JDBC 컨텍스트를 제공해주는 서비스 오브젝트로서 의미가 있을 뿐이고 구현 방법이 바뀔 가능성이 없기 때문에 인터페이스를 구현하지 않았다.  기존에는 UserDao가 dataSource를 직접 의존했지만 이제는 jdbcContext이 그 사이에 끼게 된다.
의존관계에 따라서 XML 설정파일을 수정하자.
```
 <bean id="userDao" class="springbook.user.dao.UserDao">
  <property name="dataSource" ref="dataSource" />
 </bean>

 <bean id="jdbcContext" class="springbook.user.dao.JdbcContext">
   <property name="dataSource" ref="dataSource" />
 </bean>

 <bean id="dataSource"
    class="org.springframework.jdbc.datasource.SimpleDriverDataSource" >
    ...
 </bean>
```

## 3.5 템플릿과 콜백 ##
전략 패턴의 기본 구조에 익명 내부 클래스를 확용한 방식을 스프링에서는 템플릿/콜백 패턴이라 부른다. 전략 패턴의 컨텍스트를 템플릿이라 부르고, 익명 내부 클래스로 만들어진 오브젝트를 콜백이라 부른다.

템플릿은 고정된 작업 흐름을 가진 코드를 재사용한다는 의미에서 붙인 이름이고 콜백은 템플릿 안에서 호출되는 것을 목적으로 만들어진 오브젝트를 말한다.

**템플릿/콜백의 동작원리**

여러 개의 메소드를 가진 일반적인 인터페이스를 사용할 수 있는 전략 패턴의 전략과 달리 템플릿/콜백 패턴의 콜백은 보통 단일 메소드 인터페이스를 사용한다. 콜백 인터페이스의 메소드에는 보통 파라미터가 있다. 이 파라미터는 템플릿의 작업 흐름 중에 만들어지는 컨텍스트 정보를 전달받을 때 사용된다.

**편리한 콜백의 재활용**

템플릿/콜백 방식은 매번 익명 내부 클래스를 사용하기 때문에 상대적으로 코드를 작성하고 읽기가 불편하다. 그래서 복잡한 익명 내부 클래스의 사용을 최소화할 수 있는 방법을 찾아보자.
```
 public void deleteAll() throws SQLException {
   this.jdbcContext.workWithStatementStrategy(
      new StatementStrategy() {
          public PreparedStatement makePreparedStatement(Connection c) throws         
            SQLException {
            return c.prepareStatement("delete from users");
         }
       }
    );
 }
```
makePreparedStatement() 메소드를 구현한 콜백 오브젝트 코드를 살펴보면 고정된 SQL 쿼리 하나를 담아서 PreparedStatement를 만드는게 전부다. 중복될 가능성이 있는 자주 바뀌지 않는 부분을 분리해보면 오직 바뀔부분은 "delete from users"라는 문자열뿐이다. sql 문장만 파라미터로 받아서 바꿀 수 있게 하고 메소드 내용 전체를 분리해 별도의 메소드로 만들본다.

```
  public void deleteAll() throws SQLException {
    executeSql("delete from users");
  }
```

```
   private void executeSql(final String query) throws SQLException {
     this.jdbcContext.workWithStatementStrategy(
      new StatementStrategy() {
          public PreparedStatement makePreparedStatement(Connection c) throws         
            SQLException {
            return c.prepareStatement(query);
         }
       }
    );   
  }
```

바뀌지 않는 부분을 빼내서 executeSql() 메소드로 만들고 바뀌는 부분인 SQL 문자만 파라미터로 받아서 사용하게 만들었다. 이렇게 해서 재활용 가능한 콜백을 담은 메소드가 만들어 졌다.

**콜백과 템플릿의 결합**

executeSql() 메소드를 UserDao만 사용하지말고 재사용 가능한 콜백을 담고 있는 메소드를 DAO가 공유할 수 있는 템플릿 클래스 안으로 옮겨서 모든 DAO메소드에서 사용하도록 하자.

```
public class JdbcContext {
   ...
   public void executeSql(final String query) throws SQLException {
	workWithStatementStrategy(
  	    new StatementStrategy() {
		public PreparedStatement makePreparedStatement(Connection c)
						throws SQLException {
					return c.prepareStatement(query);
				}
			}
		);
	}	
```

이렇게 executeSql() 메소드가 JdbcContext로 이동했으니 UserDao의 메소드에서도 jdbcContext를 통해 executeSql() 메소드를 호출하도록 수정한다.

```
 public void deleteAll() throws SQLException {
   thsi.jdbcContext.executeSql("delete from users");
 }
```

## 3.6 스프링의 JdbcTemplate ##

스프링이 제공하는 템플릿/콜백 기술을 살펴보자.

**update()**

deleteAll()에 JdbcTemplate을 적용해보자. deleteAll()에 처음 적용했던 콜백은 StatementStrategy 인터페이스의 makePreparedStatement() 메소드다. 이에 대응되는 JdbcTemplate의 콜백은 PreparedStatementCreator인터페이스의 createPreparedStatement() 메소드다. 템플릿으로부터 Connection을 제공받아서 PreparedStatment를 만들어 돌려준다는 면에서 동일하다. PreparedStatementCreator타입의 콜백을 받아서 사용하는 JdbcTemplate의 템플릿 메소드는 update()다.
```
 public void deleteAll() {
   this.jdbcTemplate.update(new PreparedStatementCreator() {
      public PreparedStatement createPreparedStatement(Connection c)
             throws SQLException {
           return con.prepareStatement("delete from users");
       }
     }
   );
 }
```

```
 public void deleteAll() {
  this.jdbcTemplate.update("delete from users");
 }
```

JdbcTemplate의 내장 콜백을 사용하는 메소드를 호출하도록 수정했다.

**queryForInt()**

gecount()메소드에 템플릿/콜백 방식을 적용해보자. getCount()는 SQL쿼리를 실행하고 ResultSet을 통해 결과 값을 가져오는 코드다. 이런 작업 흐름에서는 PreparedStatementCreator콜백과 ResultSetExtractor 콜백을 파라미터로 받는 query() 메소드다. ResultSetExtractor는 PreparedStatment의 쿼리를 실행해서 얻은 ResultSet을 전달 받는 콜백이다. ResultSetExtractor 콜백은 템플릿이 제공하는 ResultSet을 이용해 원하는 값을 추출해서 템플릿에 전달하면 템플릿은 나머지 작업을 수행한 뒤에 그 값을 query() 메소드의 리턴 값으로 돌려준다.

```
  public int getCount() {
     return this.jdbcTemplate.query(new PreparedStatementCreator() {
       public PreparedStatement createPreparedStatement(Connection con)
             throws SQLException {
           return con.prepareStatement("select count(*) from users");}, 
       new ResultSetExtractor<Integer>() {
         public Integer extractData(ResultSet rs) throws SQLException,      
              DataAccessException {
           rs.next();
           return rs.getInt(1);
       } 
     });
  }
```

첫 번째 PreparedStatementCreator콜백은 템플릿으로부터 Connection을 받고 PreparedStatement를 돌려준다. 두 번째 ResultSetExtractor는 템플릿으로부터 ResultSet을 받고 거기서 추출한 결과를 돌려준다.

```
  public int getCount() {
      return this.jdbcTemplate.queryForInt("select count(*) from users");
  }
```

JdbcTemplate의 내장 콜백을 사용하는 메소드를 호출하도록 수정했다.

**queryForObject()**

get() 메소드에 JdbcTemplate을 적용해보자. 여기서는 RowMapper 콜백을 사용한다.

```
   public User get(String id) {
     return this.jdbcTemplate.queryForObject("select * from users where id = ?",
	new Object[] {id}, 
	new RowMapper<User>() {
	    public User mapRow(ResultSet rs, int rowNum)
		throws SQLException {
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		return user;
		}
	 });
   } 
```
첫번째 파라미터는 PreparedStatement를 만들기 위한 SQL이고, 두 번째는 여기에 바인딩할 값들이다.
이렇게 하면 get() 메소드의 기본기능과 예외상황도 처리를 해준다. 이미 queryForObject()는 SQL을 실행해서 받은 로우의 개수가 하나가 아니라면 예외를 던지도록 만들어져 있다.

**query()**

RowMapper를 좀 더 사용해보도록 테이블의 모든 로우를 가져오는 getAll() 메소드를 추가해보자.
이번에는 테스트를 먼져 만들어보면서 코딩을 한다.

```
   @Test
   public void getAll()  {
	dao.deleteAll();
		
	List<User> users0 = dao.getAll();
	assertThat(users0.size(), is(0));
		
	dao.add(user1); // Id: gyumee
	List<User> users1 = dao.getAll();
	assertThat(users1.size(), is(1));
	checkSameUser(user1, users1.get(0));
		
	dao.add(user2); // Id: leegw700
	List<User> users2 = dao.getAll();
	assertThat(users2.size(), is(2));
	checkSameUser(user1, users2.get(0));  
	checkSameUser(user2, users2.get(1));
		
	dao.add(user3); // Id: bumjin
	List<User> users3 = dao.getAll();
	assertThat(users3.size(), is(3));
	checkSameUser(user3, users3.get(0));  
	checkSameUser(user1, users3.get(1));  
	checkSameUser(user2, users3.get(2));  
}

	private void checkSameUser(User user1, User user2) {
	assertThat(user1.getId(), is(user2.getId()));
	assertThat(user1.getName(), is(user2.getName()));
	assertThat(user1.getPassword(), is(user2.getPassword()));
}
```

getAll() 메소드를 만들어보자.

```
    public List<User> getAll() {
	return this.jdbcTemplate.query("select * from users order by id",
	      new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
	   	        user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	});
  }
```
첫 번째 파라미터에는 실행할 SQL 쿼리를 넣고, 바인딩할 파라미터가 있다면 두번째 파라미터에 추가한다. 파라미터가 없다면 생략할 수 있다. 마지막 파라미터는 RowMapper 콜백이다.
query() 템플릿은 SQL을 실행해서 얻은 ResultSet의 모든 로우를열람하면서 로우마다 RowMapper 콜백을 호출한다. SQL 쿼리를 실행해DB에서 가죠오는 로우의 개수만큼 호출될 것이다. RowMapper는 현재 로우의 내용을 User타입 오브젝트에 매핑해서 돌려준다.이렇게 만들어진 User 오브젝트는 템플릿이 미리 준비한 List

&lt;User&gt;

 컬렉션에 추가되고 모든 로우에 대한 작업을 마치면 모든 로우에 대한 User 오브젝트를 담고 있는 List

&lt;User&gt;

 오브젝트가 리턴된다.

**재사용 가능한 콜백의 분리**

UserDao코드를 다시 한번 살펴보면서 코드를 정리해보자.


필요 없어진 DataSource 인스턴스 변수는 제거하자. UserDao의 모든 메소드가 JdbcTemplate을 이용하도록 만들었으니 DataSource를 직접 사용할 일은 없다. 단지 JdbcTemplate을 생성하면서 직접 DI 해주기 위해 필요한 DataSource를 전달받아야 하니 수정자 메소드는 남겨둔다.

```
   private JdbcTemplate jdbcTemplate;

   public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}	
```

다음은 중복코드가 없는지 살펴본다. get()과 getAll()을 보면 사용한 RowMapper의 내용이 똑같다는 사실을 알 수 있다. 중복되는 코드를 독립시켜보자.

```
    private RowMapper<User> userMapper = 
	new RowMapper<User>() {
	   public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		return user;
	   }
    };
```

인스턴스 변수에 저장해둔 userMapper 콜백 오브젝트는 다음과 같이 get()과 getAll()에서 사용된다.

```
    public User get(String id) {
	return this.jdbcTemplate.queryForObject("select * from users where id = ?",
				new Object[] {id}, this.userMapper);
    } 

    public List<User> getAll() {
	return this.jdbcTemplate.query("select * from users order by id",this.userMapper);
    }

```

# 생각해보기 #
지금것 코딩을 하면서 DAO부분의 코딩하면서 각 메소드별로 try/catch/finally 부분을 Copy&paste 신공으로 코딩을 했는데 참 어리석은 짓이었다고 생각이 든다. 변하지 않는 부분을 템플릿으로 만들어 깔끔하게 코딩하는 개발자가 되도록 해봐야 겠다.