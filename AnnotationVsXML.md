# Introduction #
스프링 설정의 두가지 방법(애노테이션, XML)을 사례등을 통해 비교해본다.


# Details #

## 첫번째 사례 ##

**전제 : 기본적으로 인터페이스 기반 코딩, 사용자의 요구사항이 자주 바뀌지만, DB를 갈아 엎는다던가 하는 파격적인 변화가 없다고 가정**


  * 구현체가 바뀌는 경우 :  StudyService가 참조하는 StudyRespository 인터페이스의 구현체 StudyHibernateRepository에서 StudyIBatisRepository로 변경 해야 함
    1. XML 설정 : bean 설정 파일에서 class 속성 중에 StudyHibernateRepository가 등록되어 있는 부분을 찾아서 StudyIBatisRepository로 변경
    1. 애노테이션 설정 : StudyHibernateRepository에 붙어 있던 @Repository를 떼어내고, StudyIBatisRepository에 @Repository를 붙인다
> -> XML을 파일을 열어 확인할 필요 없이 코딩하던 파일을 조금만 수정하면 되므로, 애노테이션이 편하다는 의견

  * 특정 인터페이스의 구현체 여러개를 사용 하는 경우 : NotificationService 인터페이스의 구현체, MailService, GoogleTalkService, TwitterService 구현체들을 애플리케이션에서 사용하고 있다.
    1. XML 설정 : 세개의 빈을 등록하고, 애플리케이션에서 필요한 곳에 각 빈의 id값을 이용하여 DI해준다.
    1. 애노테이션 설정 :  세 개의 클래스에 @Service를 붙여준다. 애플리케이션에서 각각의 서비스를 필요로 하는 곳에 NotificationService 타입으로 주입을 받고, @Qualifier를 사용하여 구분한다.
> -> @Qualifier 설정이 지져분해져서 XML 설정이 편하다는 의견

## 두번째 사례 ##

**전제 : 검색 엔진을 만들어 납품했지만, 색인율이 낮다는 고객의 항의가 있었다. 그래서 특수 색인기를 만들어 급하게 만들어 설치하려 한다. 전에는 이런 요건이 없었기에 색인기만 따로 패키지 하지 않았다.**

  * XML 설정 : 해당 고객에게만 납품될 색인기를 따로 jar로 만들어 lib에 복사해 놓고 설정 파일만 변경해주면 특수 색인기로 대체
  * 애노테이션 설정 : 전체 엔진을 새로 빌드 후 배포

## 세번째 사례 ##

**전제 : 다른 사람이 Spring으로 구현한 소스를 처음 접한 입장에서 개발을 시작했다.**

  * XML 설정 : xml설정파일에서 해당 클래스를 찾아서 구현방법을 습득했다.
  * 애노테이션 설정 : 일일이 클래스 이름을 찾아서 구현방법을 습득했다.
> > -> 기 구현된 소스를 파악하기에는 XML 방식이 편하다는 의견


## 정리 ##
XML을 통해 구성설정을 명시적으로 기술해 주느냐와 애노테이션을 통해 묵시적으로 약속된 구성 설정을 하느냐에 대한 논의는 Case by case 일 것 같다. 하지만 일반적인 방법은 프레임워크나 라이브러리 같은 인프라나 공통적인 부분은 XML 설정으로 하고, 직접 코딩해야 하는 애플리케이션 코드는 애노테이션으로 설정하는 것이다.