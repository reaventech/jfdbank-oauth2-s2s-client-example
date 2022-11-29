package tech.reaven.oauth2.s2s.client.example.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
@ConfigurationProperties(prefix = "tech.reaven.pricefeed.api.config")
@Configuration
public class PriceFeedApiConfiguration {	
	@NotNull
	private String url;
	
	@NotNull
	private String port;

}
