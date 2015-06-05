## 환경설정 ##

#### Maven 설정 ####
> - repository : Jboss, http://repository.jboss.org/maven2

> - dependency : hibernate-core, slf4j-log4j12 등...

#### In-Memory DB ####
> - HSQL DB 사용

## Repository 구현 ##

#### Hibernate 설정 ####
> - 도메인 별 `*.hbm.xml` 매핑파일 설정 또는 애노테이션 설정 (데이터 베이스 의존성 제거)

#### Spring + Hibernate ####
> - SessionFactory 를 SpringBean 으로 등록하기

> - o.s.orm.hibernate3.HibernateTemplate 이용
> > - 템플릿/콜백 패턴으로 중복제거, 예외 변환, 트랜잭션 동기화의 장점이 있으나 스프링 template 구조에 종속되어 사용되므로 hibernate api 직접 사용이 불가한 단점도 있다.


> - ~~targetClass 수동 DI~~ -> 코드를 통한 제네릭 타입 알아내기

#### 조회 조건 : HQL or Criteria ####
> - Criteria, Criterion, Order, CriterionOperator 공통으로 사용

#### 이슈사항 ####

**2011.01.19 이슈사항**

> - 도메인 클래스 기준 API 제공

> - d.r.api.Criteria 를 Hibernate 의 Criteria 로 변환 이슈 (간단한 것 부터..)

> - 리파지토리 수정 없이 클라이언트가 조회 조건 설정할 수 있도록 구현

> - 테이블 별이 아닌 Aggregate 단위 구현 이슈 (마지막에 생각해 볼 문제)