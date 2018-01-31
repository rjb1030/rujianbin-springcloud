package rujianbin.autoconfiguration.qlexpress.demo;

import com.ql.util.express.ExpressRunner;

/**
 * Created by rujianbin on 2018/1/30.
 *
 * 查看表达式需要输入的外部变量  有int和没int的区别
 */
public class Demo5 {

    public static void main(String[] args)throws Exception {
        /**
         * 有int表示平均分是内部定义的变量 没有则表示外部定义的变量
         */
        String express = "int 平均分 = (语文+数学+英语+综合考试.科目2)/4.0;return 平均分";
        ExpressRunner runner = new ExpressRunner(true,true);
        String[] names = runner.getOutVarNames(express);
        for(String s:names){
            System.out.println("var : " + s);
        }
    }
}
