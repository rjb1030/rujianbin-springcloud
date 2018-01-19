package rujianbin.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
