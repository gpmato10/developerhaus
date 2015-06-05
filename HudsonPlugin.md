

# 준비 #


## 환경설정 ##

### 1. 이클립스에서 메이븐 프로젝트 생성 ###

groupId : hudson.plugin

artifactId : HelloWorld

### 2. pom.xml 을 다음과 같이 구성 ###

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<parent>
           <groupId>org.jvnet.hudson.plugins</groupId>
           <artifactId>plugin</artifactId>
           <version>1.318</version>
           <relativePath>../pom.xml</relativePath>
        </parent>

	<groupId>hudson.plugin</groupId>
	<artifactId>HelloWorld</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>hpi</packaging>
	<name>HelloWorld</name>
	<url>http://maven.apache.org</url>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	</properties>

	<!-- dependency specification -->
	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.4</version>
			<scope>test</scope>
		</dependency>

	<dependency>
		<groupId>org.jvnet.hudson.main</groupId>
		<artifactId>hudson-core</artifactId>
		<version>1.170</version>   
		<scope>provided</scope>
	</dependency>



	<dependency>
		<groupId>org.jvnet.hudson.main</groupId>
		<artifactId>hudson-war</artifactId>
		<type>war</type>
		<version>1.170</version>   
		<scope>test</scope>
	</dependency> 

		
	</dependencies>   
	<!-- build process and details specification -->
	<build>
		<defaultGoal>package</defaultGoal>
		<plugins>
			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>javancss-maven-plugin</artifactId>
				<executions>
					<execution>
						<phase>process-sources</phase>
						<goals>
							<goal>report</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>
```

### 3. settings.xml 을 다음과 같이 구성 ###

{사용자 디렉토리}/.m2/settings.xml 이 없다면 생성한다.

settings.xml 을 설정하지 않을 경우 hpi:create, hpi:run 이 실행되지 않는다.

```
<settings>
  <mirrors>
    <mirror>
      <!--This sends everything else to /public -->
      <id>nexus</id>
      <mirrorOf>external:*</mirrorOf>
      <!-- your address may differ: -->
      <url>https://maven.nuxeo.org/nexus/content/groups/public/</url>
    </mirror>
  </mirrors>
  <profiles>
    <profile>
      <id>nexus</id>
      <!--Enable snapshots for the built in central repo to direct -->
      <!--all requests to nexus via the mirror -->
      <repositories>
        <repository>
          <id>central</id>
          <url>http://central</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>true</enabled></snapshots>
        </repository>
      </repositories>
     <pluginRepositories>
        <pluginRepository>
          <id>central</id>
          <url>http://central</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>true</enabled></snapshots>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>
  <activeProfiles>
    <!--make the profile active all the time -->
    <activeProfile>nexus</activeProfile>
  </activeProfiles>
  <pluginGroups>
    <pluginGroup>org.jvnet.hudson.tools</pluginGroup>
  </pluginGroups>

</settings>
```

### 4. 실행 ###

mvn clean

mvn package

mvn hpi:create (artifactId 입력)

mvn hpi:run

http://127.0.0.1:8080/ 으로 hudson 접속 가능

## 구조 ##

```
+ src
    + main
        + java
             +  full.package.name
                    +- MyClassPublisher.java
                    +- PluginImpl.java
        + resources         
             +  full.package.name
                    +- config.jelly
                    +- global.jelly
             +- index.jelly
        + webapp
            +- help-globalConfig.html
            +- help-projectConfig.html
```

## TO DO ##

Extension Point 클래스를 상속한 클래스 구현, 실 구현체의 설명자 Decriptor 구현




# 참고사이트 #

http://wiki.hudson-ci.org/display/HUDSON/Extend+Hudson

http://notatube.blogspot.com/2010/10/custom-hudson-plugins.html

http://www.theserverlabs.com/blog/2008/09/24/developing-custom-hudson-plugins-integrate-with-your-own-applications/

http://blog.naver.com/sckim0524?Redirect=Log&logNo=91225934

https://svn.java.net/svn/hudson~svn/trunk/hudson/plugins/ (플러그인 svn)