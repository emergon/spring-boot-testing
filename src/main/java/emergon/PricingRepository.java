package emergon;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class PricingRepository {

    public BigDecimal getPricing(String product){
        BigDecimal price;
        if(product.equals("computer")){
            price = BigDecimal.valueOf(10.0);
        }else{
            price = BigDecimal.valueOf(5.0);
        }
        return price;
    }

}
