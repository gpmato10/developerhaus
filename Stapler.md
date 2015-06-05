## Stapler 소개 ##

[Kohsuke Kawaguchi](http://kohsuke.org) 가 허드슨을 위해 제작한 자바 RESTful 웹 프레임웍이다.

Stapler is a library that "staples" your application objects to URLs, making it easier to write web applications. The core idea of Stapler is to automatically assign URLs for your objects, creating an intuitive URL hierarchy.

Hudson uses the Stapler to associate jelly files with plugins. In MVC terms, your plugin class is the "M", Jelly is the "V", Stapler is the "C".

It's also used to dispatch requests to the plugin classes. Once again, Stapler does this all by convention.

If you have a form that submits to /foo/bar/SomePlugin then Stapler will try to invoke the doSubmit method on foo.bar.SomePlugin.

![http://stapler.java.net/stapler.png](http://stapler.java.net/stapler.png)



## 참고 사이트 ##

  * http://kohsuke.org/?s=stapler
  * http://notatube.blogspot.com/2010/10/custom-hudson-plugins.html
  * http://stapler.java.net