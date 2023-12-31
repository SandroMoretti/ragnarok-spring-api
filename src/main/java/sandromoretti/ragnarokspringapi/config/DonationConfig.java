package sandromoretti.ragnarokspringapi.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DonationConfig {
    public static final int CASH_PER_AMOUNT = 1000;  // $1.00 == 100 cash
    public static final int MIN_DONATION_AMOUNT = 1;
}
