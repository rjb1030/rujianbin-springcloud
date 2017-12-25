package rujianbin.microsoft.server.dubbo.impl;

import org.springframework.stereotype.Component;
import rujianbin.microsoft.server.dubbo.client.ISay;
import rujianbin.microsoft.server.dubbo.client.bean.HelloBean;

import java.util.Date;

/**
 * Created by rujianbin on 2017/12/25.
 */
@Component
public class SayImpl implements ISay{


    //http://localhost:7777/dubbo-restful/test/1
    @Override
    public HelloBean getHelloBean(Long id) {
        HelloBean bean = new HelloBean();
        bean.setId(id);
        bean.setCreateDate(new Date());
        bean.setMessage("你好，我是dubbo服务");
        return bean;
    }
}
