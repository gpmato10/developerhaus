package developerhaus.repository.mybatis.dao;

import developerhaus.domain.User;

/**
 * USER 정보의 등록/수정/삭제/조회 처리를 위한 DAO Interface 이다.
 * 
 * @author Juyeong, Seo
 */
public interface UserDao {

    User getUser(String id);

}
