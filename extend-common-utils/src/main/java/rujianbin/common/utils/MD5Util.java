package rujianbin.common.utils;


import com.google.common.hash.Hashing;

public class MD5Util {

    public static String md5(String str){
        return Hashing.md5().hashBytes(str.getBytes()).toString();
    }
}
