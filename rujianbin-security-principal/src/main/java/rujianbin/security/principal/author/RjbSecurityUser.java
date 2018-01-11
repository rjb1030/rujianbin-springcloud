package rujianbin.security.principal.author;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import rujianbin.security.principal.entity.UserEntity;

import java.io.Serializable;

/**
 * Created by 汝建斌 on 2017/4/1.
 */
@JsonAutoDetect
public class RjbSecurityUser extends User implements UserDetails,Serializable{

    public static final String sessionKey = "userInfo";

    private Long id;

    private String name;


    public RjbSecurityUser(UserEntity user){
        super(user.getUsername(),user.getPassword(),user.getSecurityAuthorities());
        this.name=user.getName();
        this.id=user.getId();
    }

//    @JsonIgnore
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        List<AuthorityEntity> AuthorityEntityList = this.getAuthorityEntityList();
//        if(AuthorityEntityList != null){
//            for (AuthorityEntity author : AuthorityEntityList) {
//                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(author.getAuthorityCode());
//                authorities.add(authority);
//            }
//        }
//        return authorities;
//    }

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

//    @Override
//    public Long getId() {
//        return super.getId();
//    }
//
//    @Override
//    public void setId(Long id) {
//        super.setId(id);
//    }
//
//    @Override
//    public Date getCreateDate() {
//        return super.getCreateDate();
//    }
//
//    @Override
//    public void setCreateDate(Date createDate) {
//        super.setCreateDate(createDate);
//    }
//
//    @Override
//    public String getName() {
//        return super.getName();
//    }
//
//    @Override
//    public void setName(String name) {
//        super.setName(name);
//    }
//
//    @Override
//    public void setUsername(String username) {
//        super.setUsername(username);
//    }
//
//    @Override
//    public void setPassword(String password) {
//        super.setPassword(password);
//    }
//
//    @Override
//    public void setAuthorityEntityList(List<AuthorityEntity> authorityEntityList) {
//        super.setAuthorityEntityList(authorityEntityList);
//    }
//
//    @Override
//    public List<AuthorityEntity> getAuthorityEntityList() {
//        return super.getAuthorityEntityList();
//    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
