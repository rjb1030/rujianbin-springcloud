package rujianbin.autoconfiguration.springsecurity.config.handle.login;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import rujianbin.autoconfiguration.springsecurity.config.dao.IAuthorityDao;
import rujianbin.autoconfiguration.springsecurity.config.dao.IUserDao;
import rujianbin.security.principal.author.RjbSecurityUser;
import rujianbin.security.principal.entity.AuthorityEntity;
import rujianbin.security.principal.entity.UserEntity;

import java.util.List;

/**
 * Created by 汝建斌 on 2017/4/1.
 */
@Component("rjbUserDetailsService")
public class RjbUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IAuthorityDao authorityDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userDao.findUser(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("UserName " + username + " not found");
        }
        List<AuthorityEntity> AuthorityEntityList = authorityDao.getAuthorityEntityList(userEntity.getId());
        userEntity.setAuthorityEntityList(AuthorityEntityList);
        RjbSecurityUser seu = new RjbSecurityUser(userEntity);
        return  seu;
    }
}
