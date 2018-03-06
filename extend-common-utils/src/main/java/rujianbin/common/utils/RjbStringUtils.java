package rujianbin.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by rujianbin on 2018/1/18.
 */
public class RjbStringUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String ObjectToString(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String startupLog(Environment env) {

        try {
            return "\n----------------------------------------------------------"+
                    "\n\tApplication '"+env.getProperty("spring.application.name")+"' is running! Access URLs:"+
                    "\n\t" + "Local: \t\thttp://127.0.0.1:"+env.getProperty("server.port")+
                    "\n\tExternal: \thttp://"+ InetAddress.getLocalHost().getHostAddress()+":"+env.getProperty("server.port")+
                    "\n\tConfig Server:\t"+(env.getProperty("configserver.status")==null?"Not found or not setup for this application":env.getProperty("configserver.status"))+
                    "\n----------------------------------------------------------";
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }
}
