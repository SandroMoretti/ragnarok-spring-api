package sandromoretti.ragnarokspringapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.HashMap;

@SpringBootApplication
@EnableAsync
public class RagnarokSpringApiApplication{
	public static void main(String[] args) {
		SpringApplication.run(RagnarokSpringApiApplication.class, args);
	}
}
