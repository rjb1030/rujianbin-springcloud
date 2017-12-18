package rujianbin.security.principal.author;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rujianbin.security.principal.entity.AuthorityEntity;
import rujianbin.security.principal.entity.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 汝建斌 on 2017/4/1.
 */
public class RjbSecurityUser extends UserEntity implements UserDetails,Serializable {

    public static final String sessionKey = "userInfo";

    public RjbSecurityUser(){}

    public RjbSecurityUser(UserEntity user){
        super.setId(user.getId());
        super.setName(user.getName());
        super.setUsername(user.getUsername());
        super.setPassword(user.getPassword());
        super.setCreateDate(user.getCreateDate());
        super.setAuthorityEntityList(user.getAuthorityEntityList());
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<AuthorityEntity> AuthorityEntityList = this.getAuthorityEntityList();
        if(AuthorityEntityList != null){
            for (AuthorityEntity author : AuthorityEntityList) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(author.getAuthorityCode());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public String getUsername(){
        return super.getUsername();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof RjbSecurityUser){
            RjbSecurityUser tmp = (RjbSecurityUser)obj;
            if(getUsername()!=null && tmp!=null && getUsername().equals(tmp.getUsername())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return getUsername().hashCode();
    }
}
