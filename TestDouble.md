# Test Double 정리

# Introduction #

Test Double과 그에 속한 Test Stub, Test Spy, Mock Object, Fake Object에 대해 정리한다.


# Details #

## Test Double ##
> 오리지널 객체를 사용해서 테스트를 진행하기 어려울 경우 이를 대신해서 테스트를 진행할 수 있도록 만들어 주는 객체를 지칭한다.

![http://xunitpatterns.com/Test%20Double.gif](http://xunitpatterns.com/Test%20Double.gif)
> - SUT(System Under Test) : 테스트 대상이 되는 시스템(이나 모듈)을 뜻한다.

#### Test Double 하위 분류 ####
![http://xunitpatterns.com/Types%20Of%20Test%20Doubles.gif](http://xunitpatterns.com/Types%20Of%20Test%20Doubles.gif)

#### 테스트 예시 ####
  * 인터넷 쇼핑몰에서 유저에게 쿠폰을 발급하는 업무를 가정
  * 고객이 쿠폰을 발급받아 저장하고, 그 내역을 확인할 수 있는 기능을 구현
  * 쿠폰 인터페이스
```
  public interface ICoupon {
	
	String getName();				// 쿠폰이름
	boolean isValid();				// 쿠폰 유효 여부 확
	int getDiscountPercent();		// 할인율
	boolean isAppliable(Item item); // 해당 아이템에 적용 가능 여부
	public void doExpire();			// 사용할 수 없는 쿠폰으로 만듦
}
```


## Dummy Object ##
> 객체의 전달에만 사용되고 실제로 이를 사용하지 않는 것이다. 대개 매개변수 목록을 채우는데 쓰이며, 오로지 인스턴스화될 수 있는 수준으로만 구현된다.
```
public class DummyCoupon implements ICoupon {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public int getDiscountPercent() {
		return 0;
	}

	@Override
	public boolean isAppliable(Item item) {
		return false;
	}

	@Override
	public void doExpire() {
	}
}
```

#### 테스트 구현 ####
```
	@Test
	public void testAddCoupon() throws Exception {
	
		User user = new User("abc");
		assertEquals(0, user.getTotalCouponCount());
		
		ICoupon coupon = new DummyCoupon();
		
		user.addCoupon(coupon);
		assertEquals(1, user.getTotalCouponCount());
	}
```

**더미객체는 단지 인스턴스화된 객체가 필요할 뿐 해당 객체의 기능까지는 필요하지 않은 경우에 사용한다. 따라서 해당 더미 객체의 메소드가 호출됐을 때의 정상 동작은 보장되지 않는다.**

기본타입이 아닌, 특정 상황을 가정해야 하는 경우가 생긴다면 다른식의 접근이 필요하다. 예를 들어 coupon.getName()이나 coupon.getDiscountRate() 같은 메소드 호출이 테스트케이스 과정에 필요한 경우이다.

## Test Stub ##
> 더미객체가 마치 실제로 동작하는 것처럼 보이게 만들어 놓은 객체다. 테스트를 위해 미리 준비한 응답만을 제공하며, 그외의 상황에 대해서는 정상적으로 작동하지 못하는 것이 일반적이다.
```
public class StubCoupon implements ICoupon {

	@Override
	public String getName() {
		return "VIP 고객 한가위 감사쿠폰";
	}

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public int getDiscountPercent() {
		return 7;
	}

	@Override
	public boolean isAppliable(Item item) {
		return false;
	}

	@Override
	public void doExpire() {
	}
}
```

#### Dummy Object와 Test Stub 차이 ####
  * 단지 인스턴스화 될 수 있는 객체 수준이면 더미
  * 인스턴스화 된 객체가 특정 상태나 모습을 대표하면 스텁

#### 테스트 구현 ####
```
	@Test
	public void testAddCoupon() throws Exception {
	
		User user = new User("abc");
		ICoupon eventCoupon = new StubCoupon();
		user.addCoupon(eventCoupon);
		
		ICoupon lastCoupon = user.getLastOccupiedCoupon());
		
		assertEquals("쿠폰 할인율", 7, lastCoupon.getDiscountPercent());
		assertEquals("쿠폰 이름", "VIP 고객 한가위 감사쿠폰", lastCoupon.getName());

	}
```

**Test Sutb은 특정 객체가 상태를 대신해주고 있긴 하지만, 거의 하드코딩된 형태이기 때문에 로직이 들어가는 부분을 테스트 할 수 없다.**

제품의 카테코리별로 아이템 적용 가능여부를 테스트 해야 될때 다른 접근법이 필요하다.

## Fake Object ##

Stub이 하나의 인스턴스를 대표하는데 주로 쓰인다면, Fake Object는 여러개의 인스턴스를 대표할 수 있는 경우이거나, 좀 더 복잡한 구현이 들어가 있는 객체를 지칭한다.

```
public class FakeCoupon implements ICoupon {
	
	List<String> categoryList = new ArrayList(); // 내부용으로 사용할 목록
	
	public FakeCoupon(){
		categoryList.add("부엌칼");
		categoryList.add("아동 장난감");
		categoryList.add("조리기구");
	}

	@Override
	public boolean isAppliable(Item item) {
		if( this.categoryList.contains( item.getCategory())){
			return true;
		}
		return false;
	}
...
}
```

**복잡한 로직이나, 객체 내부에서 필요로 하는 다른 외부 객체들의 동작을 비교적 단순화하여 구현한 객체이다. 결과적으로 테스트 케이스 작성을 진행하기 위해 필요한 다른 객체들과의 의존성을 제거하기 위해 사용된다.**

## Test Spy ##
테스트에 사용되는 객체에 대해서도, 특정 객체가 사용됐는지, 그리고 그 객체의 예상된 메소드가 정상적으로 호출됐는지를 확인 하는 등의 호출여부등을 기록했다가, 나중에 요청이 오면 해당기록 정보등을 전달해 주는데 사용한다.

```
public class SpyCoupon implements ICoupon {
	
	List<String> categoryList = new ArrayList(); // 내부용으로 사용할 목록
	private int isAppliableCallCount;
	
	public SpyCoupon(){
		categoryList.add("부엌칼");
		categoryList.add("아동 장난감");
		categoryList.add("조리기구");
	}

	@Override
	public boolean isAppliable(Item item) {
		isAppliableCallCount++; 	// 호출되면 증가
		if( this.categoryList.contains( item.getCategory())){
			return true;
		}
		return false;
	}
	
	public int getIsAppliableCallCount(){ // 호출수
		return this.isAppliableCallCount;
	}
...
}
```

#### 테스트 구현 ####
**calculator.getOrderPrice(item, coupon))** 호출시 coupon의 **boolean isAppliable(Item item)** 메소드 호출이 됐는지 검증
```
	@Test
	public void testGetOrderPrice() throws Exception {
		PriceCalculator calculator = new PriceCalculator();
		Item item = new Item("LightSavor", "Kitchen knife", 10000);
		ICoupon coupon = new SpyCoupon();
		
		assertEquals("쿠폰으로 인해 할인된 가격", 9300, calculator.getOrderPrice(item, coupon));
		
		int methodCallCount = ((SpyCoupon) coupon).getIsAppliableCallCount();
		assertEquals("coupon.isAppliable 메소드 호출 횟수", 1, methodCallCount);
	}
```

## Mock Object ##
일반적인 Test Double은 상태(state)를 기반으로 테스트를 작성하지만, Mock Object는 행위(behavior)를 기반으로 테스트 케이스를 작성한다. 행위를 검증하기 위해 사용되는 객체를 지칭한다.

#### 상태기반 테스트 VS 행위기반 테스트 ####
  * 상태 기반 테스트 : 객체지향 프로그램의 특징 중 하나는 객체가 특정 시점에 자신만의 상태를 갖는다는 점이다. 상태 기반 테스트는 이런 특징에 기반한 테스트 방식이다. 동작하는 모양만으로 봤을 때, 상태 기반 테스트는 테스트 대상 클래스의 메소드를 호출하고, 그 결과값과 예상값을 비교하는 식이다. 물론 메소드는 '두 값의 합 구하기' 같은 식의 '기능'으로만 동작할 수 도 있다. 하지만 많은 경우에 있어 메소드 호출은 객체의 상태를 변경한다. setName('토비') 같은 메소드만 보더라도, 객체의 이름 속성 값을 바꿔버린다. 특정한 메소드를 거친 후, 객체의 상태에 대해 예상값과 비교하는 방식이 상태기반 테스트이다. setName 메소드를 호출했으면, getName()메소드로 확인해 보는 식이다.

  * 행위기반 테스트 : 행위기반 테스트는 올바른 로직 수행에 판단의 근거로 특정한 독작의 수행 여부를 이용한다. 보통은 메소드의 리턴값이 없거나 리턴값을 확인하는 것만으로는 예상대로 동작했음을 보증하기 어려운 경우에 사용한다. 메소드 A와 B가 있을 때 메소드A에 특정 값이 입력 될때 메소드B의 호출여부가 다를때 메소드A 만으로는 정상 동작여부를 알아낼 수가 없다. 즉 메소드B의 호출 여부를 조사하지 않으면 메소드A의 정상 동작여부를 판단할 수 없다. 이럴땐 메소드B의 호출여부를 확인하는 것이 테스트 시나리오의 종료조건이 된다. 하지만 전통적인 테스트케이스 작성방식, 즉 상태기반 테스트에선 사실상 이런 상황에 테스트 케이스를 작성하기가 매우 어려벼거나 불편했다. 테스트 대상인A가 상태를 갖고 있지 않기 때문이다. 이럴때 찾아낸 방법이 Test Spy를 사용하거나 자체적으로 검증하는 기능을 제공하는 Mock 객체를 따로 만들어서 테스트케이스를 작성하는 것이었다. 즉 행위를 점검하는 걸로 테스트 케이스를 만드는 방식인 것이다. 따라서 행위기반 테스트를 수행할 때는 예상하는 행위들을 미리 시나리오로 만들어 놓고 해당 시나리오대로 동작이 발생했는지 여부를 확인하는 것이 핵심이 된다. 참고로, 초창기에 나온 Mock프레임워크들은 태생자체가 이런 행위기반 테스트를 지원해주기 위해서인 경우가 대다수였다.

쿠폰업무 규칙 중 '유저가 쿠폰을 받을 때, 쿠폰이 유효한 상태일 경우에만 다운받을수 있다.'는 규칙이 추가되었다.

```
  // jMock로 검증
  ICoupon coupon = mock(ICoupon.class);
  coupon.expects(once()).method("isVaild") // Mock으로 만들어진 coupon 객체의 isValid메소드가 1번 호출될 것을 예상함
        .withAnyArguments() // isValid에서 사용할 인자는 무엇이 됐든 상관 안함
        .will(returnValue(true)); // 호출시 리턴값은 true를 돌려주게 될 것임

  user.addCoupo(coupon);
  assertEquals(1, user.getTotalCouponCount());
```

호출 횟수 및 파라미터 값 지정 여부, 호출 시 반환할 리턴값까지 지정해서 예상대로 동작하는지를 테스트 한다. 예상과 달리 coupon.isValid() 메소드가 여러번 호출된다든가, 한번도 호출되지 않는다든가, 아니면 예상했던 파라미터와 다른 값으로 호출 된다면 테스트가 실패하게 된다.

### Reference ###

  * xUnit Patterns
> > - http://xunitpatterns.com/Test%20Double.html

  * 한계 돌파를 위한 노력, Mock를 이용한 TDD
> > - 채수원, "테스트 주도 개발 : 고품질 쾌속개발을 위한 TDD 실천법과 도구", pp. 191-214
