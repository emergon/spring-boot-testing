package emergon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    private PricingRepository pricingRepository;

    @Autowired
    public PricingService(PricingRepository pricingRepository){
        this.pricingRepository = pricingRepository;
    }

    public BigDecimal getPricing(String product){
        return this.pricingRepository.getPricing(product);
    }


}
