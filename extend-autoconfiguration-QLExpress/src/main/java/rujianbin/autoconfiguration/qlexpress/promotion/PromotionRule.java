package rujianbin.autoconfiguration.qlexpress.promotion;

/**
 * Created by rujianbin on 2018/1/30.
 */
public class PromotionRule {

    /**
     * 促销名称
     */
    private String ruleName;

    /**
     * 标签
     */
    private String label;
    /**
     * 维度：件数，金额
     */
    private String dimension;
    /**
     * 判断 less great
     */
    private String judge;
    /**
     * 条件
     */
    private String condition;
    /**
     * 行为：满金额送优惠券，满减
     */
    private String behave;
    /**
     * 行为结果
     */
    private String result;


    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getBehave() {
        return behave;
    }

    public void setBehave(String behave) {
        this.behave = behave;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getExpress(){
        return "如果 商品标签匹配 并且 ("+dimension+" "+judge+" "+condition+") 则 "+behave+" 否则 无行为;";
    }
}
