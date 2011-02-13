package developerhaus.user;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import developerhaus.common.generic.GenericController;
import developerhaus.domain.User;
import developerhaus.repository.api.criteria.Criteria;

/**
 * User controller
 * 
 * @author sunghee, Park
 * 
 */
@Controller
@RequestMapping("/user/*.do")
public class UserController extends GenericController<User, Integer, UserService>{
	
	@Override
	public List<User> list(Criteria criteria) {
		// TODO Auto-generated method stub
		return super.list(criteria);
	}
}
