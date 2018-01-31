package rujianbin.autoconfiguration.qlexpress.demo;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.DynamicParamsUtil;
import com.ql.util.express.ExpressRunner;

/**
 * Created by rujianbin on 2018/1/30.
 *
 * 不定参数的方法绑定
 *  1. addFunction时绑定的参数类型是 new Class[] { Object[].class } 而不是new String[]{"int","String"}这样的了
 *  2. 使用时，如果传参是数组那没问题，如果传参是不定参数则需要打开全局开关
 *
 *  故建议绑定方法的定义用Object[] 而不是不定参数
 */
public class Demo6 {


    public static void main(String[] args) throws Exception{
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();

        runner.addFunctionOfClassMethod("getTemplate", Demo6.class.getName(), "getTemplate",
                new Class[] { Object[].class }, null);

        //(1)默认的不定参数可以使用数组来代替
        Object r = runner.execute("getTemplate([11,'22',33L,true])", context, null,false, false);
        System.out.println(r);

        //(2)像java一样,支持函数动态参数调用,需要打开以下全局开关,否则以下调用会失败
        DynamicParamsUtil.supportDynamicParams = true;
        Object r2 = runner.execute("getTemplate(11556,'2322',33L,true)", context, null,false, false);
        System.out.println(r2);
    }

    //等价于getTemplate(Object[] params)
    public static Object getTemplate(Object... params) throws Exception{
        String result = "";
        for(Object obj:params){
            result = result+obj+",";
        }
        return result;
    }
}
