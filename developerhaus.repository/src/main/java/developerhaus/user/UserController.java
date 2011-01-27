package developerhaus.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import developerhaus.common.generic.GenericController;

/**
 * User controller
 * 
 * @author sunghee, Park
 * 
 */
@Controller
@RequestMapping("/user")
public class UserController extends GenericController<User, Integer, UserService>{
	
}
