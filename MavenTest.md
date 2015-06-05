

# Introduction #

  1. STS(Spring Source Tool Suite) 설치
  1. Maven 프로젝트 생성
  1. SVN 연동
  1. SVN Checkout
  1. 자신의 전문분야 프로젝트 공유하기

# Details #

## 1. STS(Spring Source Tool Suite) 설치 ##
  * [STS download](http://www.springsource.com/products/springsource-tool-suite-download)

  * 위의 링크로 가면 최신 STS를 다운로드 받으실 수 있습니다. STS 다운로드시 사용자 정보를 요구합니다. 이 글이 작성되는 시점에 최신 버전인 Window 용 "springsource-tool-suite-2.5.1.RELEASE-e3.6.1-win32.zip"을 다운로드 받았습니다. 다른 OS를 사용하고 계시다면 "Other Downloads"를 클릭하셔서 OS에 맞는 STS를 다운로드 받으시길 바랍니다.
> > ![http://lh5.ggpht.com/_ISarakVcQjw/TPex3-uaG7I/AAAAAAAAABA/c-tLDrLNp98/STS-download.jpg](http://lh5.ggpht.com/_ISarakVcQjw/TPex3-uaG7I/AAAAAAAAABA/c-tLDrLNp98/STS-download.jpg)

  * 다운로드 받은 STS를 적당한 폴더에 압축 해제 후 폴더 내 STS.exe를 실행합니다.
> > ![http://lh6.ggpht.com/_ISarakVcQjw/TPe0JXIdVPI/AAAAAAAAABQ/7kc919MOwRo/s720/STS-dir.jpg](http://lh6.ggpht.com/_ISarakVcQjw/TPe0JXIdVPI/AAAAAAAAABQ/7kc919MOwRo/s720/STS-dir.jpg)

## 2. Maven 프로젝트 생성 ##

  * STS에 tcServer, Roo, maven을 포함하고 있기 때문에 maven을 별도로 설치 하지 않으셔도 됩니다.

  * Maven Project를 생성합니다. 아래의 그림 순서대로 Project를 생성합니다.
> > ![http://lh6.ggpht.com/_ISarakVcQjw/TPe9_mdIsfI/AAAAAAAAACw/v615UTg2VrA/Maven-Project.jpg](http://lh6.ggpht.com/_ISarakVcQjw/TPe9_mdIsfI/AAAAAAAAACw/v615UTg2VrA/Maven-Project.jpg)
> > ![http://lh6.ggpht.com/_ISarakVcQjw/TPe9_y5D2lI/AAAAAAAAAC0/c32Ug6ye84U/New-Maven-Project.jpg](http://lh6.ggpht.com/_ISarakVcQjw/TPe9_y5D2lI/AAAAAAAAAC0/c32Ug6ye84U/New-Maven-Project.jpg)

  * Group Id, Artifact Id를 입력합니다. MVC 모듈과 연계할 것이므로 Packaging을 war로 지정합니다. 마지막 단계 Dependencies는 별도로 설정할 것이므로 Finish를 클릭합니다.
> > ![http://lh4.ggpht.com/_ISarakVcQjw/TPe-AdJHNmI/AAAAAAAAAC4/8FfUTbD3k84/Maven-Conf.jpg](http://lh4.ggpht.com/_ISarakVcQjw/TPe-AdJHNmI/AAAAAAAAAC4/8FfUTbD3k84/Maven-Conf.jpg)
> > ![http://lh5.ggpht.com/_ISarakVcQjw/TPe-A9GqopI/AAAAAAAAAC8/yII877LUulM/Maven-dependency.jpg](http://lh5.ggpht.com/_ISarakVcQjw/TPe-A9GqopI/AAAAAAAAAC8/yII877LUulM/Maven-dependency.jpg)

  * 생성된 프로젝트의 "pom.xml"을 엽니다. 하단 탭의 pom.xml을 열면 프로젝트 생성 시 설정한 내용이 추가되어 있는 것을 볼 수 있습니다.
> > ![http://lh4.ggpht.com/_ISarakVcQjw/TPe-BQ-XCiI/AAAAAAAAADA/EWXBBsHVDcY/Maven-create.jpg](http://lh4.ggpht.com/_ISarakVcQjw/TPe-BQ-XCiI/AAAAAAAAADA/EWXBBsHVDcY/Maven-create.jpg)
> > ![http://lh4.ggpht.com/_ISarakVcQjw/TPfBhK-gkrI/AAAAAAAAADI/OLHGtSGAi6A/pom-xml.jpg](http://lh4.ggpht.com/_ISarakVcQjw/TPfBhK-gkrI/AAAAAAAAADI/OLHGtSGAi6A/pom-xml.jpg)

  * Spring 및 관련 라이브러리에 대한 의존성을 추가합니다. 먼저 repository를 설정하고 그 다음 dependency를 추가합니다. Spring과 Junit 라이브러리에 대한 의존성을 추가하였습니다.
> > ![http://lh3.ggpht.com/_ISarakVcQjw/TPfGHfguEDI/AAAAAAAAADY/oo4wh1Y0bOA/pom-dependencies.jpg](http://lh3.ggpht.com/_ISarakVcQjw/TPfGHfguEDI/AAAAAAAAADY/oo4wh1Y0bOA/pom-dependencies.jpg)

```
<repositories>	
    <repository>  
        <id>com.springsource.repository.libraries.release</id>  
        <url>http://repository.springsource.com/maven/libraries/release </url>  
    </repository>  
    <repository>  
        <id>com.springsource.repository.libraries.external</id>  
        <url>http://repository.springsource.com/maven/libraries/external </url>  
    </repository>  
    <repository>  
        <id>com.springsource.repository.bundles.release</id>  
        <url>http://repository.springsource.com/maven/bundles/release </url>  
    </repository>  
    <repository>  
        <id>com.springsource.repository.bundles.external</id>  
        <url>http://repository.springsource.com/maven/bundles/external </url>  
    </repository>  
</repositories>
```
```
<dependencies>
    <dependency>  
        <groupId>org.springframework</groupId>  
        <artifactId>org.springframework.spring-library</artifactId>  
        <type>libd</type>  
        <version>3.0.5.RELEASE</version>  
    </dependency> 
    <dependency>
        <groupId>org.junit</groupId>
        <artifactId>com.springsource.org.junit</artifactId>
        <version>4.4.0</version>
    </dependency>
</dependencies> 
```

  * 프로젝트 생성이 잘 되었는지를 확인하기 위해 Junit test 케이스와 자바 클래스를 만듭니다. 참고로 저는 Bowling-Kata로 테스트 하였습니다. maven install 하시면 target폴더에 war파일이 생성된 것을 볼 수 있습니다.
> > ![http://lh5.ggpht.com/_ISarakVcQjw/TPfTCJyBAiI/AAAAAAAAADg/WIdSaJ_D4Vg/maven-build.jpg](http://lh5.ggpht.com/_ISarakVcQjw/TPfTCJyBAiI/AAAAAAAAADg/WIdSaJ_D4Vg/maven-build.jpg)
> > ![http://lh6.ggpht.com/_ISarakVcQjw/TPfThXfNF2I/AAAAAAAAADk/K3FYaZwGL2Y/target.jpg](http://lh6.ggpht.com/_ISarakVcQjw/TPfThXfNF2I/AAAAAAAAADk/K3FYaZwGL2Y/target.jpg)

## 3. SVN 연동 ##
  * SVN "Help > Install New Software" 에서 아래의 SVN 사이트를 추가한 뒤 Install 합니다.
  * SVN Location : http://community.polarion.com/projects/subversive/download/eclipse/2.0/update-site/
> > ![http://lh3.ggpht.com/_ISarakVcQjw/TPfVusllWCI/AAAAAAAAADs/YANB8_iYwCQ/svn.jpg](http://lh3.ggpht.com/_ISarakVcQjw/TPfVusllWCI/AAAAAAAAADs/YANB8_iYwCQ/svn.jpg)
> > ![http://lh4.ggpht.com/_ISarakVcQjw/TPfXNZBrcBI/AAAAAAAAAD0/asnU6YLNjWI/svn-check.jpg](http://lh4.ggpht.com/_ISarakVcQjw/TPfXNZBrcBI/AAAAAAAAAD0/asnU6YLNjWI/svn-check.jpg)

  * Source 탭에 보시면 SVN checkout 주소와 비밀번호를 확인할 수 있습니다.
  * SVN Repositories에서 Repository Location을 추가합니다.
  * SVN URL : https://developerhaus.googlecode.com/svn/trunk/
> > ![http://lh6.ggpht.com/_ISarakVcQjw/TPfYoHoiHHI/AAAAAAAAAD8/fIGR6dJ9pkQ/svn-repository.jpg](http://lh6.ggpht.com/_ISarakVcQjw/TPfYoHoiHHI/AAAAAAAAAD8/fIGR6dJ9pkQ/svn-repository.jpg)
> > ![http://lh4.ggpht.com/_ISarakVcQjw/TPfaQ3kRTRI/AAAAAAAAAEI/jT-aXbpYFLI/s512/svn-location-info.jpg](http://lh4.ggpht.com/_ISarakVcQjw/TPfaQ3kRTRI/AAAAAAAAAEI/jT-aXbpYFLI/s512/svn-location-info.jpg)

## 4. SVN Checkout ##
  * SVN Repositories에서 "Check Out 또는 Find/Check Out As" 를 클릭하여 프로젝트를 Checkout 받습니다.
> > ![http://lh6.ggpht.com/_ISarakVcQjw/TPfbt2pP2qI/AAAAAAAAAEQ/VGlLoc0RaNI/checkout.jpg](http://lh6.ggpht.com/_ISarakVcQjw/TPfbt2pP2qI/AAAAAAAAAEQ/VGlLoc0RaNI/checkout.jpg)

  * Checkout 받은 프로젝트를 확인합니다.
> > ![http://lh6.ggpht.com/_ISarakVcQjw/TPfdCBrejoI/AAAAAAAAAEY/yS8fghDmvS0/s512/test-ok.jpg](http://lh6.ggpht.com/_ISarakVcQjw/TPfdCBrejoI/AAAAAAAAAEY/yS8fghDmvS0/s512/test-ok.jpg)

## 5. 자신의 전문분야 프로젝트 공유하기 ##

  * SVN Repositories에서 "Find/Check Out As" 를 클릭합니다.
> > ![http://lh4.ggpht.com/_ISarakVcQjw/TQIBct-35PI/AAAAAAAAAEw/d6GRlvVIHDM/svn_checkout.jpg](http://lh4.ggpht.com/_ISarakVcQjw/TQIBct-35PI/AAAAAAAAAEw/d6GRlvVIHDM/svn_checkout.jpg)


  * Next를 클릭하여 자신의 전문분야 프로젝트명으로 수정한 뒤 checkout 받습니다.
> > ![http://lh5.ggpht.com/_ISarakVcQjw/TQIBc5dHV5I/AAAAAAAAAE0/4uMrSnYoGMs/change_project.jpg](http://lh5.ggpht.com/_ISarakVcQjw/TQIBc5dHV5I/AAAAAAAAAE0/4uMrSnYoGMs/change_project.jpg)


  * 프로젝트를 클릭하시고 "Team > Disconnect"를 클릭합니다. SVN meta 정보도 지웁니다.
> > ![http://lh3.ggpht.com/_ISarakVcQjw/TQIBdLy0CeI/AAAAAAAAAE4/TGftn0RulGE/disconnect.jpg](http://lh3.ggpht.com/_ISarakVcQjw/TQIBdLy0CeI/AAAAAAAAAE4/TGftn0RulGE/disconnect.jpg)
> > ![http://lh6.ggpht.com/_ISarakVcQjw/TQIBduBJ5II/AAAAAAAAAE8/2sVBz-DXxO0/confirm.jpg](http://lh6.ggpht.com/_ISarakVcQjw/TQIBduBJ5II/AAAAAAAAAE8/2sVBz-DXxO0/confirm.jpg)


  * 프로젝트를 클릭하시고 "Team > Share Project"를 클릭하여 다음과 같이 프로젝트를 공유합니다.    ![http://lh3.ggpht.com/_ISarakVcQjw/TQIChv6mICI/AAAAAAAAAFI/XffZdhjuvT8/s576/share.jpg](http://lh3.ggpht.com/_ISarakVcQjw/TQIChv6mICI/AAAAAAAAAFI/XffZdhjuvT8/s576/share.jpg)
> > ![http://lh4.ggpht.com/_ISarakVcQjw/TQIBd6P233I/AAAAAAAAAFA/AqoqbPFNd1M/s576/finish.jpg](http://lh4.ggpht.com/_ISarakVcQjw/TQIBd6P233I/AAAAAAAAAFA/AqoqbPFNd1M/s576/finish.jpg)


# Reference #

  * 빌드 툴과 라이브러리 관리
> > - 이일민, "토비의 스프링3", 빌드 툴과 라이브러리 관리, pp.713-720, 2010.

  * Spring Source Bundle Repository
> > - http://ebr.springsource.com/repository/app/

  * How do I configure a Maven build to work with the repository?
> > - http://ebr.springsource.com/repository/app/faq