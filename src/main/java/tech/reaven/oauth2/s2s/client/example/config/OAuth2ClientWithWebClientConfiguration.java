package tech.reaven.oauth2.s2s.client.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuth2ClientWithWebClientConfiguration {
	
	@Autowired
	private SecurityParametersConfig securityParametersConfig;

	// Create the OAuth2 Token Provider client registration
    @Bean    
    ReactiveClientRegistrationRepository clientRegistrationForWebFluxWebClient(

    ) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("reaven.tech")
                .tokenUri(securityParametersConfig.getIssuerTokenUri())
                .clientId(securityParametersConfig.getClientId())
                .clientSecret(securityParametersConfig.getClientSecret())
                .scope(securityParametersConfig.getScopes())
                .authorizationGrantType(new AuthorizationGrantType(securityParametersConfig.getAuthorizationGrantType()))
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations) {
        InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("reaven.tech");
        return WebClient.builder()
                .filter(oauth)
                .build();

    }

}