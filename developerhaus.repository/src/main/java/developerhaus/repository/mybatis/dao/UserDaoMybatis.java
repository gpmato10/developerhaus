package developerhaus.repository.mybatis.dao;

import org.mybatis.spring.SqlSessionTemplate;

import developerhaus.domain.User;

/**
 * USER 정보의 등록/수정/삭제/조회 처리를 위한 DAO implementation 이다.
 * 
 * @author Juyeong, Seo
 */
public class UserDaoMybatis implements UserDao {

	private SqlSessionTemplate sqlSessionTemplate;
	
	/**
	* setter injection을 통해 SqlSessionTemplate 객체 설정
	*
	* @param sqlSessionTemplate
	*/
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	/**
	 * User 정보를 조회한다.
	 * 
	 * @param id
	 * @return User
	 */
	@Override
	public User getUser(String id) {
		return (User) sqlSessionTemplate.selectOne("findMemberById" , id);
	}
}
