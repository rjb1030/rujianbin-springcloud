package rujianbin.eureka.api.bean;

import java.util.List;

/**
 * Created by rujianbin on 2018/2/7.
 */
public class RjbParam {

    private String searchName;
    private int searchPageNo;
    private List<Long> productId;

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public int getSearchPageNo() {
        return searchPageNo;
    }

    public void setSearchPageNo(int searchPageNo) {
        this.searchPageNo = searchPageNo;
    }

    public List<Long> getProductId() {
        return productId;
    }

    public void setProductId(List<Long> productId) {
        this.productId = productId;
    }
}
