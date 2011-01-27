package developerhaus.repository.ibatis.dao;

import developerhaus.user.User;

/**
 * USER 정보의 등록/수정/삭제/조회 처리를 위한 DAO Interface 이다.
 * 
 * @author Juyeong, Seo
 */
public interface UserDao {

	/**
	 * User 정보를 조회한다.
	 * 
	 * @param id
	 * @return User
	 */
    User getUser(String id);

}
