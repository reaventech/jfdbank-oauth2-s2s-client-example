package tech.reaven.oauth2.s2s.client.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@SpringBootApplication
@EnableScheduling
public class OAuth2ClientExampleSpringBootApplication {

	public static void main(String[] args) {
		// Impediment to recovery can be DNS caching performed by the JVM, or the operating system 
    	// itself. For example, if the hostname of the database remains constant, but the IP address
    	// may change during recovery, a DNS cache can interfere with recovery by returning a stale address.
    	java.security.Security.setProperty("networkaddress.cache.ttl", "30");

		new SpringApplicationBuilder(OAuth2ClientExampleSpringBootApplication.class).run(args);
	}

}
