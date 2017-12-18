package rujianbin.autoconfiguration.springsecurity.config.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import rujianbin.security.principal.entity.UserEntity;

/**
 * Created by 汝建斌 on 2017/4/1.
 */
@Mapper
public interface IUserDao {


    @Select("select id,name,username,password,create_date from rjb_user where username=#{username}")
    public UserEntity findUser(String username);
}
