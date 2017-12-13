package rujianbin.app.web.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import rujianbin.app.web.entity.Person;

/**
 * Created by rujianbin on 2017/12/13.
 */
@Mapper
public interface TestMapper {

    @Select("select id,name,age from person where id=#{id,jdbcType=BIGINT}")
    Person findPerson(@Param("id") Long id);
}
