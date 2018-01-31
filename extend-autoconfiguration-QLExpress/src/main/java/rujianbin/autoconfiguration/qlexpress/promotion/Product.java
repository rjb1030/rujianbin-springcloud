package rujianbin.autoconfiguration.qlexpress.promotion;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by rujianbin on 2018/1/30.
 */
public class Product {

    private String productName;
    private String productLabel;
    private int productNum;
    private BigDecimal productPrice;

    /**
     * 原价总金额
     */
    private BigDecimal productTotalPrice;

    /**
     * 促销后总金额
     */
    private BigDecimal promotionTotalPrice;

    public Product(String productName,String productLabel,int productNum,BigDecimal productPrice){
        this.productName = productName;
        this.productLabel=productLabel;
        this.productNum=productNum;
        this.productPrice = productPrice;
        this.productTotalPrice = productPrice.multiply(new BigDecimal(productNum));
        this.promotionTotalPrice = productTotalPrice;
    }

    /**
     * 每个促销平摊的金额
     */
    private Map<String,BigDecimal> promotionSubTotalPriceMap = new LinkedHashMap<>();

    /**
     * 是否满足规则的标签匹配
     */

    private boolean isMatchLabel;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(String productLabel) {
        this.productLabel = productLabel;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }


    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public Map<String, BigDecimal> getPromotionSubTotalPriceMap() {
        return promotionSubTotalPriceMap;
    }

    public void setPromotionSubTotalPriceMap(Map<String, BigDecimal> promotionSubTotalPriceMap) {
        this.promotionSubTotalPriceMap = promotionSubTotalPriceMap;
    }

    public BigDecimal getPromotionTotalPrice() {
        return promotionTotalPrice;
    }

    public void setPromotionTotalPrice(BigDecimal promotionTotalPrice) {
        this.promotionTotalPrice = promotionTotalPrice;
    }

    @JsonIgnore
    public boolean isMatchLabel() {
        return isMatchLabel;
    }

    public void setMatchLabel(boolean matchLabel) {
        isMatchLabel = matchLabel;
    }
}
