package rujianbin.autoconfiguration.qlexpress.promotion;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import rujianbin.autoconfiguration.qlexpress.demo.Demo2;
import rujianbin.autoconfiguration.qlexpress.demo.ProDemo1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rujianbin on 2018/1/31.
 */
public class ExpressConfig {

    private static ExpressRunner runner = new ExpressRunner();

    static{
        try {
            runner.addOperator("在区间",new BetweenOperate());
            runner.addOperatorWithAlias("如果","if",null);
            runner.addOperatorWithAlias("并且","and",null);
            runner.addOperatorWithAlias("则","then",null);
            runner.addOperatorWithAlias("否则","else",null);
            runner.addOperatorWithAlias("大于",">",null);
            runner.addOperatorWithAlias("小于","<",null);
            runner.addFunctionOfClassMethod("hasMatchLabelProducts", RuleCalculate.class.getName(), "hasMatchLabelProducts",
                    new Class[] {List.class,PromotionRule.class}, "无标签匹配商品");
            runner.addFunctionOfClassMethod("matchProductAmount",RuleCalculate.class.getName(),"matchProductAmount",
                    new Class[] {List.class},null);
            runner.addFunctionOfClassMethod("matchProductNum",RuleCalculate.class.getName(),"matchProductNum",
                    new Class[] {List.class},null);
            runner.addFunctionOfClassMethod("behaveAmountSub",RuleCalculate.class.getName(),"behaveAmountSub",
                    new Class[] {List.class,PromotionRule.class},null);
            runner.addFunctionOfClassMethod("behaveAmountDiscount",RuleCalculate.class.getName(),"behaveAmountDiscount",
                    new Class[] {List.class,PromotionRule.class},null);
            runner.addFunctionOfClassMethod("behaveUnitPriceSub",RuleCalculate.class.getName(),"behaveUnitPriceSub",
                    new Class[] {List.class,PromotionRule.class},null);
            runner.addFunctionOfClassMethod("behaveGiveGift",RuleCalculate.class.getName(),"behaveGiveGift",
                    new Class[] {PromotionRule.class},null);
            runner.addFunctionOfClassMethod("behaveGiveCoupon",RuleCalculate.class.getName(),"behaveGiveCoupon",
                    new Class[] {PromotionRule.class},null);
            runner.addFunctionOfClassMethod("behaveNone",RuleCalculate.class.getName(),"behaveNone",
                    new Class[] {},null);

            runner.addMacro("商品标签匹配","hasMatchLabelProducts(products,rule)");
            runner.addMacro("总金额","matchProductAmount(products)");
            runner.addMacro("总件数","matchProductNum(products)");
            runner.addMacro("无行为","behaveNone()");
            runner.addMacro("总金额满减","behaveAmountSub(products,rule)");
            runner.addMacro("总金额折扣","behaveAmountDiscount(products,rule)");
            runner.addMacro("单价减","behaveUnitPriceSub(products,rule)");
            runner.addMacro("送赠品","behaveGiveGift(rule)");
            runner.addMacro("送优惠券","behaveGiveCoupon(rule)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RuleCalculate.CalculateResult execute(String express, List<Product> products, PromotionRule rule) throws Exception{
        IExpressContext<String,Object> context = new DefaultContext<String,Object>();
        context.put("products",products);
        context.put("rule",rule);
        List<String> errorInfo = new ArrayList<String>();
        RuleCalculate.CalculateResult result = (RuleCalculate.CalculateResult)runner.execute(express, context, errorInfo, true, false);
        return result;
    }
}
