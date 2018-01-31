package rujianbin.autoconfiguration.qlexpress.demo;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

/**
 * Created by rujianbin on 2018/1/30.
 *
 * 宏定义
 *  宏和Function区别
 *      宏的别名是不能传参的，类似于一个逻辑状态一样，简单的用一个变量替换一段文本
 *      但是宏的实现可以是一个Function或者直接一段运算表达式，参数取自context
 *      宏的参数可以是变量，后续可以从context中获取，而方法的参数必须实际的传入值
 *
 */
public class Demo4 {

    public static void main(String[] args) throws Exception{
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();

        runner.addMacro("计算平均成绩", "(语文+数学+英语)/3.0");
        runner.addMacro("是否优秀", "计算平均成绩>90");
        context.put("语文", 88);
        context.put("数学", 99);
        context.put("英语", 95);
        String express = "是否优秀";
        Object r = runner.execute(express, context, null, false, false);
        System.out.println(express+"----->"+r);
    }
}
