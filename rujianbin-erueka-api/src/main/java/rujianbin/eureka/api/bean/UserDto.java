package rujianbin.eureka.api.bean;

import java.math.BigDecimal;

/**
 * Created by rujianbin on 2018/2/7.
 */
public class UserDto {
    private String name;
    private Integer age;
    private BigDecimal amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
