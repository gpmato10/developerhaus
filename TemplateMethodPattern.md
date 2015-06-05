**템플릿 메소드 패턴**

메소드에서 알고리즘의 골격을 정의하는 것.
알고리즘의 여러 단계 중 일부는 서브클래스에서 구현할 수 있다.
템플릿 메소드를 이용하면 알고리즘의 구조는 그대로 유지하면서 서브클래스에서 특정 단계를 재정의할 수 있다.

**팩토리 메소드 패턴**

상위 클래스의 추상 메소드를 오버라이딩 해서 재정의 하여 서브 클래스에서는 서로 다른 구현을 해서 서로 다른 처리가 실행되게 하는 것. (훅 메소드도 선택적으로 오버라이딩)

**훅 메소드**

훅(hook) 메소드는 추상 클래스에 선언되는 메소드이기는 하지만 기본적인 내용만 구현되어 있거나 아무 코드도 들어있지 않은 메소드이다. 서브 클래스에서 선택적으로 오버라이드 하며, 오버라이드 하지 않으면 추상 클래스에서 기본으로 제공한 코드가 실행된다.

서브 클래스가 많아질 수록 모든 단계가 필수적이지 않을 수 있다. 이럴 때 필수적이지 않은 부분은 추상 메소드가 아닌 훅 메소드로 사용하면 서브 클래스의 부담이 조금 줄어든다.

**예제**

![http://cnx.org/content/m17309/latest/TemplateMethodPattern.jpg](http://cnx.org/content/m17309/latest/TemplateMethodPattern.jpg)

출처 : Design Patterns for Sorting, http://cnx.org/content/m17309/latest/

위와 같이 슈퍼 클래스의 invariant 메소드가 템플릿 메소드가 되어서 전체적인 흐름을 관장하며

서브 클래스에서 각각 슈퍼클래스의 추상 메소드인 variant1, variant2를 오버라이딩하여 구체적인 기능을 구현하므로써 서로 다른 처리가 되게 한다.

**SimpleFormController**

스프링 MVC 의 SimpleFormController 는 입력 폼을 처리할 때 유용하게 사용할 수 있는 컨트롤러 이다. 이는 아래와 같이 AbstractFormController 를 상속받고 있다.

![http://developerhaus.googlecode.com/files/springmvccontroller_dazzilove.jpg](http://developerhaus.googlecode.com/files/springmvccontroller_dazzilove.jpg)

출처 : dazzilove.blog.me - Spring MVC Controller 들.. , http://dazzilove.blog.me/60042129027?Redirect=Log

추상 클래스인 AbstractFormController 에는 추상메소드인 showForm, processFormSubmission 가 있다. 이를 서브클래스인 SimpleFormController 에서 오버라이딩해서 구현하는 템플릿 메소드 패턴을 이용하고 있다.

showForm 과 processFormSubmission 은 입력 폼의 GET, POST 요청 과정에서 호출되서 사용되고 있으며 GET 과 POST 요청 시의 흐름은 다음과 같다.

![http://developerhaus.googlecode.com/files/simpleformcontroller_get.jpg](http://developerhaus.googlecode.com/files/simpleformcontroller_get.jpg)

SimpleFormController - GET

![http://developerhaus.googlecode.com/files/simpleformcontroller_post.jpg](http://developerhaus.googlecode.com/files/simpleformcontroller_post.jpg)

SimpleFormController - POST

출처 : 자바지기닷넷 - SimpleFormController 의 Workflow, http://www.javajigi.net/pages/viewpage.action?pageId=1161