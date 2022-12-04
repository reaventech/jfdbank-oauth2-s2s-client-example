package tech.reaven.oauth2.s2s.client.example.config;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
@ConfigurationProperties(prefix = "reaven.tech.security")
@Configuration
public class SecurityParametersConfig {

	@NotNull
	@URL
	private String issuerUri;
	
	@NotNull
	@URL
	private String issuerTokenUri;
	
	@NotNull
	@URL
	private String jwksUri;
	
	@NotNull
	@URL
	private String audienceUri;
	
	@NotNull
	private String clientId;
	
	@NotNull
	private String clientSecret;
	
	@NotNull
	private List<String> scopes;
	
	@NotNull
	private String authorizationGrantType;
}
