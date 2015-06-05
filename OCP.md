# 개념 #

  * 소프트웨어 실체(software entities : classes, modules, functions, 등..)는 확장에 대해서는 열려 있어야 하지만 변경에 대해서는 닫혀 있어야 한다.



# 상세내용 #

  * 확장에 열려있다.
    * This means that the behavior of the module can be extended. That we can make the module behave in new and different ways as the requirements of the application change, or to meet the needs of new applications.
  * 변경에 닫혀있다.
    * The source code of such a module is inviolate. No one is allowed to make source code changes to it.
  * 확장할 수 있는 부분은 확장이 가능하도록 만들고, 확장 함에 있어 기본 로직에 영향을 끼치지 않도록 분리한다.
  * 확장하는 클래스는 공통 로직이 어찌 구현되어 있는지 관심도 없고, 알 필요도 없다.
  * 다른 클래스가 유연하게 확장해서 사용할 수 있도록 구현되어야 한다.

### 간단히 이해하기 ###
  * 휴대폰 충전기
    * 휴대폰을 살때마다 기계에 종속적이여서 바꿔야만 했던 충전기를 표준화(24핀 충전기) 시켰다.
    * 하나의 충전기로 대부분의 휴대폰을 충전할 수 있기 때문에 휴대폰을 구입할때 충전기를 함께 구입하지 않아도 된다.
    * 여러 회사에서 나온 각각의 휴대폰을 젠더를 이용해 하나의 충전기를 가지고 확장해서 사용할 수 있다.


## 구현 예시 ##

  * 상속
    * 상속의 방법을 통해 유연하게 변경하고 확장할 수 있다.
    * 상속은 OCP의 하나의 간단한 예시일 뿐 유일한 방법도, 상속이 OCP를 나타내는 것도 아니다.
    * UserDao
      * 초난감 Dao는 매번 같은 일을 반복하는 코드를 메소드마다 넣어줘야 했다.
      * N사와 D사의 커넥션 방법이 다를 경우에도 매번 UserDao를 수정하지 않고도 사용할 수 있도록 분리한다.
> > > ![http://dl.dropbox.com/u/176648/ocp1.png](http://dl.dropbox.com/u/176648/ocp1.png)

  * 전략 패턴(StrategyPattern)
    * 인터페이스 형식의 인스턴스 변수를 이용해 동적으로 인터페이스를 구현한 클래스를 할당할 수 있어 유연하게 변경 가능하다.