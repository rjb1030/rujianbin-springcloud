package rujianbin.autoconfiguration.springsecurity.config.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import rujianbin.security.principal.entity.AuthorityEntity;

import java.util.List;

/**
 * Created by 汝建斌 on 2017/4/1.
 */
@Mapper
public interface IAuthorityDao {


    @Select("select DISTINCT ra.id,ra.authority_code from rjb_authority ra " +
            "INNER JOIN rjb_role_authority_rela rrar on rrar.authority_code = ra.authority_code " +
            "INNER JOIN rjb_role rr on rr.id = rrar.role_id " +
            "INNER JOIN rjb_user_role_rela rurr on rurr.role_id=rr.id " +
            "INNER JOIN rjb_user ru on rurr.user_id=ru.id " +
            "where ru.id=#{userId,jdbcType=BIGINT}")
    public List<AuthorityEntity> getAuthorityEntityList(Long userId);
}
