# 스프링 MVC 예외처리 #
## HandlerExceptionResolver을 이용한 예외처리 ##

Controller 인터페이스의 handleRequest() 메서드는 Exception 을 발생할 수 있는데

```
public interface Controller {
    ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception;
}
```

따라서 Controller 는 서블릿과 관련된 ServletException 외에 RuntimeException 등 다양한 예외를 던질 수 있다.

보통은 서블릿 엔진이 제공하는 에러 화면이 웹 브라우저에 출력되는데, 만약 예외에 따라 알맞은 에러 화면을 출력하고 싶다면 Sprinb MVC 에서 제공하는 HandlerExceptionResolver 를 사용하면 된다. DispatcherServlet 은 HandlerExceptionResolver 가 등록되어 있을 경우, 예외 처리를 HandlerExceptionResolver 에 맡긴다


```
package org.springframework.web.servlet;

public interface HandlerExceptionResolver {
    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);
}
```


간단한 예외 발생 시 처리를 위해서는 주로 SimpleMappingExceptionResolver를 사용 한다. SimpleMappingExceptionResolver 클래스는 HandlerExceptionResolver 인터페이스를 구현하고 있으며 이 클래스는 예외 타입에 따라 사용될 뷰를 지정하여 알맞은 에러 화면을 출력할 수 있도록 하고 있다.

```
<beans>
 <!-- Exception Resolver -->
 <bean id="exceptionMapping"
  class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
     <property name="exceptionMappings">
        <props>
            <prop key="DataNotFoundException">
                 ErrorMessage
            </prop>
        </props>
     </property>
     <property name="exceptionAttribute" value="exceptionMsg" />
     <property name="defaultErrorView" value="Error" />
</bean>
</beans>
```


DataNotFoundException 이 발생하게 되면 DataNotFoundException ViewName(여기서는 ErrorMessage)을 리턴하게 되고 SimpleMappingExceptionResolver에서 viewName에 따라서 해당 ModelAndView를 리턴하게 된다.

위 속성을 제외하고도 SimpleMappingExceptionResolver는 여러가지 유용한 속성들을 노출시키고 있다.

| mappedHandlers | 특정된 맵 핸들러를 세팅해줄수 있다.|
|:---------------|:--------------------------------------------------|
| mappedHandlerClasses | 특정된 맵 핸들러클래스를 세팅해줄수 있다.|
| defaultErrorView | 지정된 예외가 아닌 예외일때 기본적으로 포워딩해줄 view페이지를 세팅할수있다.|
| defaultStatusCode | 예외가 떴을대 HTTP 상태코드를 세팅해줄수가 있다. 단 이 세팅은 Top - Level Request에만 유효하다.|
| exceptionAttribute | 예외가 노출시킬 기본 모델 속성을 세팅할수 있다. 기본적으로 exception으로 설정이 되여있으나 경우에 따라 변경할수 있다. 만약 이 속성에 값을 세팅하지 않으면 기본적으로 view페이지에서 ${exception.message}를 통하여 예외메세지를 받을수 있다.|


- 단순히 에러발생 메세지나 간단한 메세지만 보내주는 것만이 아니라 에러가 발생한 객체내의 어떤 특정 값이나 에러 메세지를 유연하게 처리하고 싶을땐 HandlerExceptionResolver 인터페이스를 직접 상속받어서 구현하는 방법이 있다.
```
public class BaseExceptionResolver implements HandlerExceptionResolver 
{
   private String view = null;
   public void setView(String view) 
   {
     this.view = view;
   }
  
   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception) 
   {
     // 여기에 처리 로직이~~
     request.setAttribute("exception",exception);  // request로 exception을 넘긴다.
     return new ModelAndView(view);
   }
}
```

이런식으로 구현을하고 bean 설정은 다음과 같이 바꿔줘야 한다.
```
<bean id="exceptionResolver" class="BaseExceptionResolver">
    <property name="view" value="error"/>
</bean>
```


**exception 처리 규칙**
  1. exceptionMappings에 등록된 예외를 찾을 때, 해당하는 예외 명이 두 개 이상일 경우 이름이 짧은 쪽을 우선시 한다.
```
<property name="exceptionMappings">
    <props>
        <prop key="Excep">exceptionPage</prop>
        <prop key="ExceptionChild">moreSpecificPage</prop>
    </props>
</property>
```
> - ExceptionChild가 발생한 경우, 여기서는 exceptionPage가 리턴되게 된다.

> 2. 예외가 상속 구조를 가질 경우 exceptionMappings에 등록된 예외를 찾을 경우에도 그대로 적용된다.
```
<property name="exceptionMappings">
    <props>
        <prop key="ExceptionParent">parentPage</prop>
    </props>
</property>
```
> - ExceptionChild가 ExceptionParent를 상속 받은 경우 여기서는 parentPage가 리턴되게 된다.

> 3. 만약 스프링과 서블릿 컨테이너에서 같이 java.lang.Exception 을 처리한다면 스프링 처리가 더 우선시 된다.
```
// 스프링 
<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
	<property name="exceptionMappings">
	<props>
		<prop key="java.lang.Exception">error</prop>
	</props>
	</property>
</bean>

//web.xml
<error-page>
    <exception-type>java.lang.Exception</exception-type>
	<location>/WEB-INF/pages/error.jsp</location>
</error-page>
```

@ExceptionHandler 어노테이션을 이용한 예외처리는 조사중~