package rujianbin.autoconfiguration.qlexpress.demo;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rujianbin on 2018/1/30.
 * https://github.com/alibaba/QLExpress
 *
 * 钻石买家 且 未买过商品的  可以买商品
 */
public class ProDemo1 {


    private ExpressRunner runner = new ExpressRunner();

    public void initial() throws Exception{
        runner.addOperatorWithAlias("而且","and",null);
        runner.addFunctionOfClassMethod("userTagJudge", ProDemo1.class.getName(), "userTagJudge",
                new String[] {UserInfo.class.getName(),"int"}, "你不是钻石买家");
        runner.addFunctionOfClassMethod("hasOrderGoods", ProDemo1.class.getName(), "hasOrderGoods",
                new String[] {UserInfo.class.getName(),"long"}, "你已买过该商品");
        runner.addMacro("钻石买家", "userTagJudge(userInfo,3)");//3表示钻石买家的标志位
        runner.addMacro("未曾买过", "hasOrderGoods(userInfo,100)");//100表示旺铺商品的ID
    }

    /**
     * 如果tag=7二进制就是0111   tagBitIndex=2则表示第三位是标记
     * 运算实际等于  0111 & 0100
     * @param user
     * @param tagBitIndex
     * @return
     */
    public boolean userTagJudge(UserInfo user,int tagBitIndex){
        boolean r =  (user.getUserTag() & ((long)Math.pow(2, tagBitIndex))) > 0;
        return r;
    }

    /**
     * 判断一个用户是否订购过某个商品
     * @param user
     * @param goodsId
     * @return
     */
    public boolean hasOrderGoods(UserInfo user,long goodsId){
        //随机模拟一个
        if(user.getUserId() % 2 == 1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断逻辑执行函数
     * @param userInfo
     * @param expression
     * @return
     * @throws Exception
     */
    public String hasPermissionBuy(UserInfo userInfo,String expression) throws Exception {
        IExpressContext<String,Object> expressContext = new DefaultContext<String,Object>();
        expressContext.put("userInfo",userInfo);
        List<String> errorInfo = new ArrayList<String>();
        Boolean result = (Boolean)runner.execute(expression, expressContext, errorInfo, true, false);
        String resultStr ="";
        if(result.booleanValue() == true){
            resultStr = "可以订购此商品";
        }else{
            for(int i=0;i<errorInfo.size();i++){
                if(i > 0){
                    resultStr  = resultStr + "," ;
                }
                resultStr  = resultStr + errorInfo.get(i);
            }
            resultStr = resultStr  + ",所以不能订购此商品";
        }
        return "亲爱的" + userInfo.getName() + " : " + resultStr;
    }


    public static void main(String[] args)throws Exception {
        ProDemo1 demo = new ProDemo1();
        demo.initial();
        System.out.println(demo.hasPermissionBuy(new UserInfo(100,"xuannan",7),  "钻石买家   而且   未曾买过"));
        System.out.println(demo.hasPermissionBuy(new UserInfo(101,"qianghui",8), "钻石买家   而且   未曾买过"));
        System.out.println(demo.hasPermissionBuy(new UserInfo(100,"张三",8), "钻石买家 and 未曾买过"));
        System.out.println(demo.hasPermissionBuy(new UserInfo(100,"李四",7), "钻石买家 and 未曾买过"));
    }




    public static class UserInfo {
        long id;
        long tag;
        String name;

        public UserInfo(long aId,String aName, long aUserTag) {
            this.id = aId;
            this.tag = aUserTag;
            this.name = aName;
        }
        public String getName(){
            return name;
        }
        public long getUserId() {
            return id;
        }

        public long getUserTag() {
            return tag;
        }

    }
}
