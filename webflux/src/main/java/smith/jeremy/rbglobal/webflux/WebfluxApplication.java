package smith.jeremy.rbglobal.webflux;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@SpringBootApplication
public class WebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxApplication.class, args);
	}

	@Bean
	public ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
		var initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);

		var databasePopulator = new CompositeDatabasePopulator();
		databasePopulator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
		initializer.setDatabasePopulator(databasePopulator);

		return initializer;
	}
}
