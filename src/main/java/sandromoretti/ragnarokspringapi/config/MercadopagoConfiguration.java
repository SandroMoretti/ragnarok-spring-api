package sandromoretti.ragnarokspringapi.config;

import com.mercadopago.MercadoPagoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sandromoretti.ragnarokspringapi.service.DonationService;

@Configuration
public class MercadopagoConfiguration {
    Logger logger = LoggerFactory.getLogger(MercadopagoConfiguration.class);

    @Value("${mercadopago.token}")
    private String mercadopagoToken;

    @Bean
    public boolean initialize(){
        if(mercadopagoToken == null){
            logger.info("No token setted for mercado pago integration");
            return false;
        }

        MercadoPagoConfig.setAccessToken(mercadopagoToken);
        logger.info("Mercadopago configured");
        return true;
    }
}
