package rujianbin.autoconfiguration.qlexpress.demo;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

/**
 * Created by rujianbin on 2018/1/30.
 *
 * 集合的快速写法
 */
public class Demo7 {

    public static void main(String[] args) throws Exception{
        ExpressRunner runner = new ExpressRunner(false,false);
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        String express = "abc = NewMap(1:1,2:2); return abc.get(1) + abc.get(2);";
        Object r = runner.execute(express, context, null, false, false);
        System.out.println(r);
        express = "abc = NewList(1,2,3); return abc.get(1)+abc.get(2)";
        r = runner.execute(express, context, null, false, false);
        System.out.println(r);
        express = "abc = [1,2,3]; return abc[1]+abc[2];";
        r = runner.execute(express, context, null, false, false);
        System.out.println(r);
    }
}
