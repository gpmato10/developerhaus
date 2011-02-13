package developerhaus.repository;

import java.util.List;

import developerhaus.domain.User;
import developerhaus.domain.UserPoint;
import developerhaus.repository.api.GenericRepository;

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

	List<UserPoint> getUserPointList();
	
}