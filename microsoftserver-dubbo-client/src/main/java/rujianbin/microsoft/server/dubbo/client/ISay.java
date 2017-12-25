package rujianbin.microsoft.server.dubbo.client;

import rujianbin.autoconfiguration.dubbo.annotation.DubboService;
import rujianbin.microsoft.server.dubbo.client.bean.HelloBean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by rujianbin on 2017/12/25.
 */
@Path("/dubbo-restful")
@DubboService(version="1.0.0")
public interface ISay {

    @Path("/test/{id}")
    @GET
    @Produces("application/json; charset=UTF-8")
    public HelloBean getHelloBean(@PathParam("id")Long id);
}
