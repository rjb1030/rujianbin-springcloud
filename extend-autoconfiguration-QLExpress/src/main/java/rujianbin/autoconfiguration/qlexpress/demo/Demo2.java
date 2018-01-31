package rujianbin.autoconfiguration.qlexpress.demo;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.Operator;

/**
 * Created by rujianbin on 2018/1/30.
 * 自定义操作符
 * 自定义方法
 * 重载操作符
 *
 */
public class Demo2 {


    public static void main(String[] args) throws Exception {
        /**
         * 自定义操作符
         */
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        runner.addOperator("join",new JoinOperator());
        String express = "1 join 2 join 3";
        Object r = runner.execute(express, context, null, false, false);
        System.out.println(express+"----->"+r);


        /**
         * 自定义方法
         */
        ExpressRunner runner3 = new ExpressRunner();
        DefaultContext<String, Object> context3 = new DefaultContext<String, Object>();
        runner3.addFunction("join",new JoinOperator());
        String express3 = "join(1,2,3)";
        Object r3 = runner3.execute(express3, context3, null, false, false);
        System.out.println(express3+"----->"+r3);


        /**
         * 重载操作符
         */
        ExpressRunner runner2 = new ExpressRunner();
        DefaultContext<String, Object> context2 = new DefaultContext<String, Object>();
        runner2.replaceOperator("+",new JoinOperator());
        String express2 = "1 + 2 + 3";
        Object r2 = runner2.execute(express2,context2, null, false, false);
        System.out.println(express2+"----->"+r2);

    }


    public static class JoinOperator extends Operator {

        @Override
        public Object executeInner(Object[] objects) throws Exception {
            //被定义成操作符 传入参数是2个
            if(objects.length==2){
                Object opdata1 = objects[0];
                Object opdata2 = objects[1];
                if(opdata1 instanceof java.util.List){
                    ((java.util.List)opdata1).add(opdata2);
                    return opdata1;
                }else{
                    java.util.List result = new java.util.ArrayList();
                    result.add(opdata1);
                    result.add(opdata2);
                    return result;
                }
            }else{
                //被定义成方法，则传入参数是多个
                java.util.List result = new java.util.ArrayList();
                for(Object obj : objects){
                    result.add(obj);
                }
                return result;
            }

        }
    }
}
