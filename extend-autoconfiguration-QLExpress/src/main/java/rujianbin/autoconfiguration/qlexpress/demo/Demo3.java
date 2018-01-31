package rujianbin.autoconfiguration.qlexpress.demo;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

/**
 * Created by rujianbin on 2018/1/30.
 *
 * addFunctionOfClassMethod 绑定java类的方法
 *      参数：方法别名，类名，类方法，方法参数类型，errorList
 * addFunctionOfServiceMethod 绑定对象的方法
 *      参数：方法别名，实体对象，实体对象方法，方法参数类型，errorList
 * addFunction 直接添加方法，参考demo2
 *      参数：方法别名，实体对象（该对象的class继承Operator接口）
 */
public class Demo3 {


    public static void main(String[] args) throws Exception{
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();

        runner.addFunctionOfClassMethod("取绝对值", Math.class.getName(), "abs",
                new String[] { "double" }, null);

        runner.addFunctionOfClassMethod("转换为大写", BeanExample.class.getName(),
                "upper", new Class[] { String.class }, null);

        runner.addFunctionOfServiceMethod("打印", System.out, "println",new String[] { "String" }, null);

        runner.addFunctionOfServiceMethod("是否包含", new BeanExample(), "anyContains",
                new Class[] { String.class, String.class }, null);

        String express1 = "取绝对值(-100);";
        Object r1 =runner.execute(express1, context, null, false, false);
        System.out.println(express1+"----->"+r1);

        String express2 = "转换为大写(\"hello world\");";
        Object r2 =runner.execute(express2, context, null, false, false);
        System.out.println(express2+"----->"+r2);

        String express3 = "是否包含(\"helloworld\",\"ae\")";
        Object r3 =runner.execute(express3, context, null, false, false);
        System.out.println(express3+"----->"+r3);

        String express4 = "打印(\"你好吗？\")";
        Object r4 =runner.execute(express4, context, null, false, false);
        System.out.println(express4+"----->"+r4);
    }


    public static class BeanExample {
        public static String upper(String abc) {
            return abc.toUpperCase();
        }
        public boolean anyContains(String str, String searchStr) {

            char[] s = str.toCharArray();
            for (char c : s) {
                if (searchStr.contains(c+"")) {
                    return true;
                }
            }
            return false;
        }
    }
}
