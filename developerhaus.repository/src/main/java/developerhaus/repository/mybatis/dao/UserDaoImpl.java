package developerhaus.repository.mybatis.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import developerhaus.domain.User;

/**
 * USER 정보의 등록/수정/삭제/조회 처리를 위한 DAO implementation 이다.
 * mybatis의 SqlSessionDaoSupport를 상속함 
 * 
 * @author Juyeong, Seo
 */
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {

	/**
	 * User 정보를 조회한다.
	 * 
	 * @param id
	 * @return User
	 */
	@Override
	public User getUser(String id) {
		return (User) getSqlSessionTemplate().selectOne("findMemberById", id);
	}

}
