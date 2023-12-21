package sandromoretti.ragnarokspringapi;

import com.mercadopago.MercadoPagoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class RagnarokSpringApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(RagnarokSpringApiApplication.class, args);
	}

}
