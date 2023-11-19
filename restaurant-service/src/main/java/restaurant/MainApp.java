package restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.transaction.Transactional;

@SpringBootApplication
public class MainApp implements CommandLineRunner {

	Logger log = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(MainApp.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// Data for users will be initialized in file data.sql
		log.info("[MAIN] Start app [restaurant-service] ...");
		log.info("--------------------------------------------------------");
	}
}
