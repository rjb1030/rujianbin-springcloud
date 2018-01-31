package rujianbin.autoconfiguration.qlexpress.promotion;

import com.ql.util.express.Operator;

import java.math.BigDecimal;

/**
 * Created by rujianbin on 2018/1/31.
 */
public class BetweenOperate extends Operator {

    @Override
    public Object executeInner(Object[] objects) throws Exception {
        if(objects.length==2){
            Object opdata1 = objects[0];
            Object[] opdata2 = (Object[])objects[1];

            if(opdata1 instanceof BigDecimal){
                return compareBigDecimal((BigDecimal)opdata1,new BigDecimal(Double.valueOf(opdata2[0].toString())),new BigDecimal(Double.valueOf(opdata2[1].toString())));
            }else if(opdata1 instanceof Integer){
                return compareBigDecimal((Integer)opdata1,Integer.valueOf(opdata2[0].toString()),Integer.valueOf(opdata2[1].toString()));
            }else{
                System.out.println("BetweenOperate 仅支持BigDecimal Integer");
                return false;
            }
        }else{
            System.out.println("BetweenOperate  参数异常！！！！！！！！！！！！！！！！！！");
            return false;
        }
    }

    private boolean compareBigDecimal(BigDecimal param,BigDecimal from,BigDecimal to){
        if(param.compareTo(from)>=0 && param.compareTo(to)<=0){
            return true;
        }else{
            return false;
        }
    }


    private boolean compareBigDecimal(Integer param,Integer from,Integer to){
        if(param.intValue()>=from.intValue() && param.intValue()<=to.intValue()){
            return true;
        }else{
            return false;
        }
    }


}
