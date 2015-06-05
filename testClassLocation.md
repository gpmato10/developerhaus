#테스트클래스의 위치를 결정한다.

# Introduction #

단위 테스트 케이스를 소스 구조 안에서 어디에 놓을건지 결정한다.

# Details #

  * 컨셉 : 구현소스와 테스트소스의 폴더는 다르게, 패키지는 동일, 컴파일된 클래스는 각각 다른곳으로

### 토비3 기준 소스코드 위치 ###

![http://developerhaus.googlecode.com/files/%EC%9C%84%EC%B9%98%EC%A1%B0%EC%A0%95%EC%A0%84%ED%81%B4%EB%9E%98%EC%8A%A4.jpg](http://developerhaus.googlecode.com/files/%EC%9C%84%EC%B9%98%EC%A1%B0%EC%A0%95%EC%A0%84%ED%81%B4%EB%9E%98%EC%8A%A4.jpg)

> 문제점 : 구현소스와 테스트코드가 같은 폴더내에 위치하여 구분이 어렵고, 배포시에 테스트 클래스만 발췌해야 하는 불편함이 있다.


### 최종 구조 ###

  * 패키지 구조 **![http://developerhaus.googlecode.com/files/%EC%B5%9C%EC%A2%85%ED%8C%A8%ED%82%A4%EC%A7%80%EA%B5%AC%EC%A1%B0.jpg](http://developerhaus.googlecode.com/files/%EC%B5%9C%EC%A2%85%ED%8C%A8%ED%82%A4%EC%A7%80%EA%B5%AC%EC%A1%B0.jpg)**


  * 네비게이터 구조 **![http://developerhaus.googlecode.com/files/%EC%B5%9C%EC%A2%85%EB%84%A4%EB%B9%84%EA%B2%8C%EC%9D%B4%ED%84%B0%EA%B5%AC%EC%A1%B0.jpg](http://developerhaus.googlecode.com/files/%EC%B5%9C%EC%A2%85%EB%84%A4%EB%B9%84%EA%B2%8C%EC%9D%B4%ED%84%B0%EA%B5%AC%EC%A1%B0.jpg)**

### 환경 구성 방법 ###

> #### 대상 프로젝트의 Properties 중 Java Build Path항목을 선택한 다음 Source탭을 선택한 후 Add Folder... 버튼을 클릭한다. ####

![http://developerhaus.googlecode.com/files/testCodeLocation_1.jpg](http://developerhaus.googlecode.com/files/testCodeLocation_1.jpg)

> #### 프로젝트의 이름을 선택하고 Create New Foler를 누른다. ####

![http://developerhaus.googlecode.com/files/testCodeLocation_2.jpg](http://developerhaus.googlecode.com/files/testCodeLocation_2.jpg)

> #### 테스트 클래스들이 위치할 폴더 이름(test)을 정한 후 OK를 선택한다. ####

![http://developerhaus.googlecode.com/files/testCodeLocation_3.jpg](http://developerhaus.googlecode.com/files/testCodeLocation_3.jpg)

> #### 각각 다른 곳으로 컴파일된 파일이 위치할 수 있도록, 'Allow output folders for source folders(소스 폴더들에 대해 출력 폴더를 지정할 수 있도록 허용)'을 선택한다. 선택하면 이전에 보이지 않앗던 Output 폴더를 지정할 수 있는 항목이 나타난다. src 폴더의 output 폴더가 Default로 되어 있는것이 보인다. 선택한 다음 Edit 버튼을 눌러서 변경한다. ####

![http://developerhaus.googlecode.com/files/testCodeLocation_4.jpg](http://developerhaus.googlecode.com/files/testCodeLocation_4.jpg)

> #### 아래 'Specific output folder' 라디오 버튼을 선택한 다음 Browse 버튼을 누른다. ####

![http://developerhaus.googlecode.com/files/testCodeLocation_5.jpg](http://developerhaus.googlecode.com/files/testCodeLocation_5.jpg)

> #### 기존의 기본 output폴더인 bin을 선택하고 'Create New Foler'을 눌러서 bin 하위에 main과 test폴더를 각각 만들자. ####

![http://developerhaus.googlecode.com/files/testCodeLocation_5_1.jpg](http://developerhaus.googlecode.com/files/testCodeLocation_5_1.jpg)

> #### 우선 테스트 대상 클래스가 컴파일될 src 폴더 쪽이니까 main을 선택한 다음 OK 버튼을 누른다. ####

![http://developerhaus.googlecode.com/files/testCodeLocation_9.jpg](http://developerhaus.googlecode.com/files/testCodeLocation_9.jpg)

> #### Output Location을 변경할 때 경고가 표시될 수 있지만, test폴더의 output까지 변경하면 해결되니까 우선은 무시한다. ####
> #### 마찬가지로 test소스의 output 위치를 test로 선택한 다음 OK를 누른다. ####
> #### 최종적으로 다음과 같은 모습이 되면 정상이다. ####

![http://developerhaus.googlecode.com/files/testCodeLocation_6.jpg](http://developerhaus.googlecode.com/files/testCodeLocation_6.jpg)

> #### Navigator 뷰를 선택해서 변경된 위치로 클래스 파일들이 정상적으로 생성됐는지 확인한다. ####

![http://developerhaus.googlecode.com/files/testCodeLocation_7.jpg](http://developerhaus.googlecode.com/files/testCodeLocation_7.jpg)

> #### 정리를 위해 bin 폴더 아래에 컴파일 파일이 쌓였던 기존 springbook 폴더를 지운다. 최종적으로 만들어진 구조는 다음과 같다. ####

  * 패키지 구조 **![http://developerhaus.googlecode.com/files/%EC%B5%9C%EC%A2%85%ED%8C%A8%ED%82%A4%EC%A7%80%EA%B5%AC%EC%A1%B0.jpg](http://developerhaus.googlecode.com/files/%EC%B5%9C%EC%A2%85%ED%8C%A8%ED%82%A4%EC%A7%80%EA%B5%AC%EC%A1%B0.jpg)**


  * 네비게이터 구조 **![http://developerhaus.googlecode.com/files/%EC%B5%9C%EC%A2%85%EB%84%A4%EB%B9%84%EA%B2%8C%EC%9D%B4%ED%84%B0%EA%B5%AC%EC%A1%B0.jpg](http://developerhaus.googlecode.com/files/%EC%B5%9C%EC%A2%85%EB%84%A4%EB%B9%84%EA%B2%8C%EC%9D%B4%ED%84%B0%EA%B5%AC%EC%A1%B0.jpg)**


#### 정리 ####
> 위와 같이 환경구성을 통해 여러가지 장점을 얻을 수 있다.
    * 대상클래스와 테스트클래스를 동일한 패키지로 선언할 수 있다.
    * 접근범위(scope) default나 protected로 선언된 메소드도 테스트 케이스로 작성할 수 있다.
    * 컴파일된 대상 클래스와 테스트클래스의 위치가 최상위 폴더부터 다르게 만들어지기 때문에, 서로 섞일 염려가 없다.
    * 배포시에도 분리가 쉽고 빠르게 패키징이 가능하다.

위의 내용을 바탕으로 maven을 사용할 경우 구성되는 구조에 대해서 만들어보자.
  * src/main/java : 제품 코드가 들어가는 위치
  * src/main/resources : 제품 코드에서 사용하는 각종 파일, XML 등의 리소스 파일들
  * src/test/java : 테스트 코드가 들어가는 위치
  * src/test/resources : 테스트 코드에서 사용하는 각종 파일, XML 등의 리소스 파일들