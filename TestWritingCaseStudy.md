## 스케쥴링 테스트 ##
  * 고민 : 스프링2.5에서 quartz를 이용하여 스케쥴링 한대로 동작하는지 여부를 파악하고 싶다. 

  * XML 설정
```
	<bean name="exampleJob"	class="org.springframework.scheduling.quartz.JobDetailBean">
		<property name="jobClass" value="scheduler.ExampleJob"/>
		<property name="jobDataAsMap">
			<map>
				<entry key="uploadPath" value="/data1/services/hpservice/ftptest/"/>
			</map>
		</property>
	</bean>
	
	<bean id="simpeTrigger" class="org.springframework.scheduling.quartz.SimpleTriggerBean">
		<property name="jobDetail" ref="exampleJob" />
		<property name="startDelay" value="1000" />
		<property name="repeatInterval" value="5000" />
	
	</bean>
	
	<bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
		<property name="triggers">
			<list>
				<ref bean="simpeTrigger"/>
			</list>
		</property>
	
        </bean>	
```

  * 테스트 코딩
```
public class SchedulingTest extends AbstractDependencyInjectionSpringContextTests 
{
	
	protected String[] getConfigLocations() {
		// TODO Auto-generated method stub
		return new String[] { "classpath:studytest/scheduling.xml" };
	}
	
	private JobDetailBean exampleJob;
	
	public void setExampleJob(JobDetailBean exampleJob) {
		this.exampleJob = exampleJob;
	}

	public void testSchedulling() throws Exception {
		
		assertNotNull(exampleJob);
		assertEquals(exampleJob.getJobClass(), scheduler.ExampleJob.class);
		
    		Thread.sleep(1000 * 20);		
	}

}   
```

  * 정리 : 스케쥴링이 한번만 실행되고 끝나는 것을 막기 위해, 적당히 테스트 코드를 넣은 다음 실행 주기보다 큰 시간을 sleep시켜 여러번 실행시킬 수 있도록 했습니다. quartz에 대해 학습하기 위해 테스트를 만들었지만, 이렇게 테스트를 만드는 것이 맞는지 확신 할 수 없었습니다. 여기에 대해서 좋은 의견 있으면 많은 조언 부탁 드리겠습니다.