package emergon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    @Mock
    private PricingRepository pricingRepository;

    @InjectMocks
    private PricingService pricingService;

    @Test
    void shouldReturnExpensivePriceWhenProductIsComputer() {
        Mockito.when(pricingRepository.getPricing("computer"))
                        .thenReturn(new BigDecimal(10.0));
        assertEquals(new BigDecimal(10.0), pricingService.getPricing("computer"));
    }

    @Test
    void shouldReturnCheapPriceWhenProductIsNotComputer() {
        Mockito.when(pricingRepository.getPricing("computer"))
                .thenReturn(new BigDecimal(5.0));

        assertEquals(new BigDecimal(5.0), pricingService.getPricing("computer"));
    }
}