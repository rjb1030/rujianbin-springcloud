package rujianbin.autoconfiguration.qlexpress.demo;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

/**
 * Created by rujianbin on 2018/1/30.
 *
 *
 */
public class Demo1 {

    public static void main(String[] args) throws Exception{
        ExpressRunner runner = new ExpressRunner();
        runner.addOperatorWithAlias("如果", "if",null);
        runner.addOperatorWithAlias("则", "then",null);
        runner.addOperatorWithAlias("否则", "else",null);
        String express = "如果 1 == 2 则 false 否则 true";
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        Object r = runner.execute(express,context,null,false,false,null);
        System.out.println(express+"------>"+r);


        ExpressRunner runner2 = new ExpressRunner();
        String express2 = "for(i=0;i<10;i++){sum=i+1}return sum;";
        DefaultContext<String, Object> context2 = new DefaultContext<String, Object>();
//        context.put("a",10);
//        context.put("b",2);
//        context.put("c",3);

        Object r2 = runner2.execute(express2, context2, null, true, false);
        System.out.println(r2);
    }
}
