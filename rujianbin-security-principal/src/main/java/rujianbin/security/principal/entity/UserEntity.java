package rujianbin.security.principal.entity;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by 汝建斌 on 2017/4/1.
 */
public class UserEntity implements Serializable{


    private static final long serialVersionUID = -8458338799774157275L;
    private Long id;

    private Date createDate;

    private String name;

    private String username;

    private String password;

    private List<AuthorityEntity> AuthorityEntityList = new ArrayList<AuthorityEntity>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<AuthorityEntity> getAuthorityEntityList() {
        return AuthorityEntityList;
    }

    public void setAuthorityEntityList(List<AuthorityEntity> authorityEntityList) {
        AuthorityEntityList = authorityEntityList;
    }

    public Collection<? extends GrantedAuthority> getSecurityAuthorities() {
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
}
