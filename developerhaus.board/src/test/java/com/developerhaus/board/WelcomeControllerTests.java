package com.developerhaus.board;

import org.junit.Test;

public class WelcomeControllerTests {
	
	private WelcomeController controller = new WelcomeController();
	
	@Test
	public void welcome() {
		controller.welcome();
	}
	
}
