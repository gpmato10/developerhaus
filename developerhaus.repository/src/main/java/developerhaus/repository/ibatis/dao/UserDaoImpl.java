package developerhaus.repository.ibatis.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import developerhaus.user.User;

/**
 * USER 정보의 등록/수정/삭제/조회 처리를 위한 DAO implementation 이다.
 * ibatis의 SqlMapClientDaoSupport를 상속함
 * 
 * @author Juyeong, Seo
 */
public class UserDaoImpl extends SqlMapClientDaoSupport implements UserDao {

	/**
	 * User 정보를 조회한다.
	 * 
	 * @param id
	 * @return User
	 */
	@Override
	public User getUser(String id) {
		return (User) getSqlMapClientTemplate().queryForObject("findMemberById", id);
	}

}
