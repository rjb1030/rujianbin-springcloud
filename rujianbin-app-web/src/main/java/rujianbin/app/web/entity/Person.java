package rujianbin.app.web.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rujianbin on 2017/12/13.
 */
public class Person {

    private Long id;
    private String name;
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public static void main(String[] args) {

//            String regEx = "count(\\d+)(df)";
//            String s = "count000dfdfsdffaaaa1";
//            Pattern pat = Pattern.compile(regEx);
//            Matcher mat = pat.matcher(s);
//            if(mat.find()){
//                for(int i=0;i<=mat.groupCount();i++){
//                    System.out.println(mat.group(i));
//                }
//
//            }



        String regEx = "(\\w+)\\[(\\w+)\\]";
        String s = "/providerFMK/index[p1:f1:read]";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(s);
        if(mat.find()){
            for(int i=0;i<=mat.groupCount();i++){
                System.out.println(mat.group(i));
            }

        }

    }
}
