**정의**

변하지 않는 부분과 변하는 부분을 분리해서 변하는 부분을 상황에 따라 교환해서 사용할 수 있도록 상속대신 구성하는 것

![http://developerhaus.googlecode.com/files/strategypattern2.jpg](http://developerhaus.googlecode.com/files/strategypattern2.jpg)

[출처 : 예제로 보는 Pattern 연상법, http://www.slideshare.net/soomong/pattern-4797069]


**예제**

![http://developerhaus.googlecode.com/files/strategypattern.png](http://developerhaus.googlecode.com/files/strategypattern.png)

[출처 : 여름으로 가는 문 - OOP 의 원칙들, http://doortts.tistory.com/67]

위 예제와 같이 ScoreProcess 에서 변하는 부분인 Sorter 를 인터페이스(전략)로 분리하고 하위 클래스에서 이를 구성(composition)하여 사용하도록 하였다.