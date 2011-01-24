package developerhaus.repository.mybatis;

import java.io.Serializable;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import developerhaus.repository.api.GenericRepository;

public interface GenericMybatisSupportRepository<D, I extends Serializable> extends
		GenericRepository<D, I> {

	@Select("SELECT SEQ, ID, NAME, PASSWORD FROM USERS WHERE ID = #{id}")
	D get(@Param("id") I id);

}
