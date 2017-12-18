package rujianbin.security.principal.entity;

import java.io.Serializable;

/**
 * Created by 汝建斌 on 2017/4/1.
 */
public class AuthorityEntity implements Serializable{

    private static final long serialVersionUID = -3344292771474970578L;
    private Long id;

    private String authorityCode;


    public AuthorityEntity(){}

    public AuthorityEntity(String authorityCode){
        this.authorityCode=authorityCode;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }
}
