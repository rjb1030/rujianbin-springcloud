package rujianbin.autoconfiguration.qlexpress.promotion;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rujianbin on 2018/1/31.
 */
public class RuleCalculate {

    /**
     * 单个商品是否匹配规则标签
     * @param products
     * @param rule
     * @return
     */
    public static boolean hasMatchLabelProducts(List<Product> products,PromotionRule rule){
        boolean result = false;
        if(CollectionUtils.isNotEmpty(products)){
            for(Iterator<Product> it = products.iterator();it.hasNext();){
                Product p = it.next();
                if(StringUtils.isNotEmpty(p.getProductLabel()) && p.getProductLabel().equals(rule.getLabel())){
                    p.setMatchLabel(true);
                    result = true;
                    continue;
                }else{
                    p.setMatchLabel(false);
                    continue;
                }
            }
        }
        return result;
    }

    /**
     * 统计匹配标签后的商品总金额
     * @param products
     * @return
     */
    public static BigDecimal matchProductAmount(List<Product> products){
        BigDecimal amount = new BigDecimal(0);
        if(CollectionUtils.isNotEmpty(products)){
            for(Product p : products){
                if(!p.isMatchLabel()){
                    continue;
                }
                amount = amount.add(p.getPromotionTotalPrice());

            }
        }
        return amount.setScale(2,BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 统计匹配标签的商品总件数
     * @param products
     * @return
     */
    public static int matchProductNum(List<Product> products){
        int num = 0;
        if(CollectionUtils.isNotEmpty(products)){
            for(Product p : products){
                if(!p.isMatchLabel()){
                    continue;
                }
                num+=p.getProductNum();
            }
        }
        return num;
    }


    /**
     * 总金额满减
     * @param products
     * @param rule
     * @return 总金额
     */
    public static CalculateResult behaveAmountSub(List<Product> products,PromotionRule rule){
        BigDecimal amount = new BigDecimal(0);
        if(CollectionUtils.isNotEmpty(products)){
            for(Product p : products){
                if(!p.isMatchLabel()){
                    continue;
                }
                amount = amount.add(p.getPromotionTotalPrice());
            }
        }
        amount = amount.setScale(2,BigDecimal.ROUND_HALF_UP);

        /**
         * 优惠平摊
         */
        BigDecimal rulePromotionAmount = new BigDecimal(Double.valueOf(rule.getResult())).setScale(2,BigDecimal.ROUND_HALF_UP);
        for(Product p : products){
            if(!p.isMatchLabel()){
                continue;
            }
            //计算该规则下，商品应该优惠的总金额
            BigDecimal sharePrice = rulePromotionAmount.multiply(p.getPromotionTotalPrice())
                    .divide(amount,2,BigDecimal.ROUND_HALF_UP)
                    .setScale(2,BigDecimal.ROUND_HALF_UP);
            p.getPromotionSubTotalPriceMap().put(rule.getRuleName(),sharePrice);
            p.setPromotionTotalPrice(p.getPromotionTotalPrice().subtract(sharePrice));
        }
        return new CalculateResult(true,rulePromotionAmount);
    }

    /**
     * 总金额折扣
     * @param products
     * @param rule
     * @return 总金额
     */
    public static CalculateResult behaveAmountDiscount(List<Product> products,PromotionRule rule){
        BigDecimal amount = new BigDecimal(0);
        if(CollectionUtils.isNotEmpty(products)){
            for(Product p : products){
                if(!p.isMatchLabel()){
                    continue;
                }
                amount = amount.add(p.getPromotionTotalPrice());
            }
        }
        amount = amount.setScale(2,BigDecimal.ROUND_HALF_UP);

        /**
         * 优惠平摊
         */
        BigDecimal discount = new BigDecimal(Double.valueOf(rule.getResult())).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal rulePromotionAmount = amount.multiply(new BigDecimal(1).subtract(discount)).setScale(2,BigDecimal.ROUND_HALF_UP);
        for(Product p : products){
            if(!p.isMatchLabel()){
                continue;
            }
            p.getPromotionSubTotalPriceMap().put(rule.getRuleName(),p.getPromotionTotalPrice().multiply(new BigDecimal(1).subtract(discount)).setScale(2,BigDecimal.ROUND_HALF_UP));
            p.setPromotionTotalPrice(p.getPromotionTotalPrice().multiply(discount).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        return new CalculateResult(true,rulePromotionAmount);
    }


    /**
     * 单价减
     * @param products
     * @param rule
     * @return 总金额
     */
    public static CalculateResult behaveUnitPriceSub(List<Product> products,PromotionRule rule){
        BigDecimal amount = new BigDecimal(0);
        int num = 0;
        if(CollectionUtils.isNotEmpty(products)){
            for(Product p : products){
                if(!p.isMatchLabel()){
                    continue;
                }
                amount = amount.add(p.getPromotionTotalPrice());
                num+=p.getProductNum();
            }
        }
        amount = amount.setScale(2,BigDecimal.ROUND_HALF_UP);

        /**
         * 优惠平摊
         */
        BigDecimal subUnitPrice = new BigDecimal(Double.valueOf(rule.getResult())).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal rulePromotionAmount = new BigDecimal(num).multiply(subUnitPrice).setScale(2,BigDecimal.ROUND_HALF_UP);
        for(Product p : products){
            if(!p.isMatchLabel()){
                continue;
            }
            //该商品优惠总金额
            BigDecimal pAmount = subUnitPrice.multiply(new BigDecimal(p.getProductNum()));
            p.getPromotionSubTotalPriceMap().put(rule.getRuleName(),pAmount);
            p.setPromotionTotalPrice(p.getPromotionTotalPrice().subtract(pAmount));
        }
        return new CalculateResult(true,rulePromotionAmount);
    }


    /**
     * 不处理任何事情
     * @return
     */
    public static CalculateResult behaveNone(){
        return new CalculateResult(false,"规则条件不满足");
    }

    /**
     * 总赠品
     * @param rule
     * @return
     */
    public static CalculateResult behaveGiveGift(PromotionRule rule){
        System.out.println("赠品productId="+rule.getResult()+"，开始发放到人");
        return new CalculateResult(true,rule.getResult());
    }

    /**
     * 送优惠券
     * @param rule
     * @return
     */
    public static CalculateResult behaveGiveCoupon(PromotionRule rule){
        System.out.println("优惠券couponId="+rule.getResult()+"，开始发放到人");
        return new CalculateResult(true,rule.getResult());
    }


    public static class CalculateResult{
        private boolean success;
        private Object data;

        public CalculateResult(boolean success,Object data){
            this.success=success;
            this.data=data;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

}
