package rujianbin.microsoft.server.dubbo.client.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rujianbin on 2017/12/25.
 */
public class HelloBean implements Serializable{

    private static final long serialVersionUID = 3275061229713579388L;
    private Long id;

    private Date createDate;

    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
