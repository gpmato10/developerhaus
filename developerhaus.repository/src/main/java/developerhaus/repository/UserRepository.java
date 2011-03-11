package developerhaus.repository;

import java.util.List;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.api.GenericRepository;
import developerhaus.repository.api.criteria.Criteria;

/**========================================================
 *파일명         : UserRepository.java
 *파일용도       : 
 *
 *마지막변경일자 : 2011. 2. 12.
 *마지막변경자   : want
=========================================================*/

/**
 * @author want
 *
 */
public interface UserRepository extends GenericRepository<User, Integer> {

	List<UserPoint> getUserPointList(User user);
	List<UserPoint> getUserPointListById(String id);
	List<UserPoint> getUserPointList(Criteria criteria);
	
}
