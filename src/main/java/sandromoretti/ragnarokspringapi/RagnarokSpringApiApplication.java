package sandromoretti.ragnarokspringapi;

import com.mercadopago.MercadoPagoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RagnarokSpringApiApplication {

	public static void main(String[] args) {
		MercadoPagoConfig.setAccessToken("TEST-6045022108465753-121320-e4a95e05358774909329f519a7bfce01-148448694");
		SpringApplication.run(RagnarokSpringApiApplication.class, args);
	}

}
