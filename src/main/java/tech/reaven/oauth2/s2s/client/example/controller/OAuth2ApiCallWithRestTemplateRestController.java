package tech.reaven.oauth2.s2s.client.example.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import tech.reaven.oauth2.s2s.client.example.config.PriceFeedApiConfiguration;
import tech.reaven.oauth2.s2s.client.example.model.CandlestickChart;
import tech.reaven.oauth2.s2s.client.example.model.CandlestickChartResponse;
import tech.reaven.oauth2.s2s.client.example.model.WebApiHttpError;
import tech.reaven.oauth2.s2s.client.example.model.WebApiHttpException;

@Configuration
@RestController
@RequestMapping("api/oauth2-api-call-with-rest-template")
@Log4j2
@NoArgsConstructor
// "Used to make test calls from this to JFD Bank API which is protected by OAuth2. Uses RestTemplate method."
public class OAuth2ApiCallWithRestTemplateRestController {

	
	// Inject the OAuth authorized client service and authorized client manager
	// from the OAuthClientConfiguration class
	@Autowired
	private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;
	
	@Autowired
	private PriceFeedApiConfiguration priceFeedApiConfiguration;
	
	// Calls a JFD Bank API with GET protected with OAuth2 server. 
	// This method candle stick formatted price feed of a given ISIN code financial instrument
	// This uses RestTemplate for this authorized API call
	// GET http://localhost:9080/api/oauth2-api-call-with-webclient/{isinCode}
	@GetMapping("/{isinCode}")
	List<CandlestickChart> getCandlestickChartDataViaRestTemplate(@PathVariable(required = true) String isinCode) {
		try {
			if (isinCode == null) {
				throw new WebApiHttpException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), "isinCode null", "Could not do GET request to protected JFD Bank API");
			}
			
			////////////////////////////////////////////////////
			//  STEP 1: Retrieve the authorized JWT
			////////////////////////////////////////////////////
			
			// Build an OAuth2 request for the OAuth2 Token provider
			OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
					.withClientRegistrationId("reaven.tech")
					.principal("Demo Service")
					.build();
			
			// Perform the actual authorization request using the authorized client service and authorized client
			// manager. This is where the JWT is retrieved from the OAuth2 Token provider.
			OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
			
			// Get the token from the authorized client object
			OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();
			
			// IMPORTANT: Please cache this accessToken, so that you use this until just before the lifetime expires !!! 
			
			log.info("Issued: " + accessToken.getIssuedAt().toString() + ", Expires:" + accessToken.getExpiresAt().toString());
			log.info("Scopes: " + accessToken.getScopes().toString());
			log.info("Token: " + accessToken.getTokenValue());
			
			////////////////////////////////////////////////////
			//  STEP 2: Use the JWT and call the service
			////////////////////////////////////////////////////
			
			// Add the JWT to the RestTemplate headers
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + accessToken.getTokenValue());
			HttpEntity request = new HttpEntity(headers);
			
			// Make the actual HTTP GET request
			RestTemplate restTemplate = new RestTemplate();
			
			String priceFeedAPICandlestickUrl = null;
			if (priceFeedApiConfiguration.getPort() == null || priceFeedApiConfiguration.getPort().isBlank()) {
				priceFeedAPICandlestickUrl = priceFeedApiConfiguration.getUrl() + "/api/candlestick/" + isinCode + "?range=d1";
			}
			else {
				priceFeedAPICandlestickUrl = priceFeedApiConfiguration.getUrl() + ":" + priceFeedApiConfiguration.getPort() + "/api/candlestick/" + isinCode + "?range=d1";
			}
			
			ResponseEntity<List<CandlestickChart>> response = 
					 restTemplate.exchange(
						 priceFeedAPICandlestickUrl,
						 HttpMethod.GET,
						 request,
						 new ParameterizedTypeReference<List<CandlestickChart>>() {
		                });

			 List<CandlestickChart> candlestickCharts = response.getBody();			
			
			log.info("OAuth2ApiCallWithRestTemplateRestController.get() response " + candlestickCharts);
					
			return candlestickCharts;
			
		} catch(Exception e) {	
			throw new WebApiHttpException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), "Could not do GET request to protected JFD Bank API");
		}		
	}
	
	// Calls a JFD Bank API with POST protected with OAuth2 server.
	// This uses WebClient for this authorized API call
	// POST http://localhost:9080/api/oauth2-api-call-with-webclient
	@PostMapping("")
	List<CandlestickChart> postIsinCodeToGetCandlestickChartDataViaRestTemplate(
			@RequestBody tech.reaven.oauth2.s2s.client.example.model.IsinCode isinCode) throws Exception {
		try {		
			if (isinCode == null || isinCode.getIsin() == null || isinCode.getIsin().isBlank()) {
				throw new WebApiHttpException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), "isinCode null", "Could not do GET request to protected JFD Bank API");
			}
			
			////////////////////////////////////////////////////
			//  STEP 1: Retrieve the authorized JWT
			////////////////////////////////////////////////////
			
			// Build an OAuth2 request for the OAuth2 Token provider
			OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
					.withClientRegistrationId("reaven.tech")
					.principal("Demo Service")
					.build();
			
			// Perform the actual authorization request using the authorized client service and authorized client
			// manager. This is where the JWT is retrieved from the OAuth2 Token provider.
			OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
			
			// Get the token from the authorized client object
			OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();
			
			// IMPORTANT: Please cache this accessToken, so that you use this until just before the lifetime expires !!!
			
			log.info("Issued: " + accessToken.getIssuedAt().toString() + ", Expires:" + accessToken.getExpiresAt().toString());
			log.info("Scopes: " + accessToken.getScopes().toString());
			log.info("Token: " + accessToken.getTokenValue());
			
			////////////////////////////////////////////////////
			//  STEP 2: Use the JWT and call the service
			////////////////////////////////////////////////////
			
			// Add the JWT to the RestTemplate headers
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + accessToken.getTokenValue());
			HttpEntity request = new HttpEntity(headers);
			
			// Make the actual HTTP GET request
			RestTemplate restTemplate = new RestTemplate();			
			
			String priceFeedAPICandlestickUrl = null;
			if (priceFeedApiConfiguration.getPort() == null || priceFeedApiConfiguration.getPort().isBlank()) {
				priceFeedAPICandlestickUrl = priceFeedApiConfiguration.getUrl() + "/api/candlestick/" + isinCode.getIsin() + "?range=d1";
			}
			else {
				priceFeedAPICandlestickUrl = priceFeedApiConfiguration.getUrl() + ":" + priceFeedApiConfiguration.getPort() + "/api/candlestick/" + isinCode.getIsin() + "?range=d1";
			}
			
			ResponseEntity<List<CandlestickChart>> response = 
				restTemplate.exchange(
						priceFeedAPICandlestickUrl,
					HttpMethod.GET,
					request,
					new ParameterizedTypeReference<List<CandlestickChart>>() {});
			
			List<CandlestickChart> candlestickCharts = response.getBody();			
			
			log.info("OAuth2ApiCallWithRestTemplateRestController.get() response " + candlestickCharts);
			
			return candlestickCharts;
			
		} catch(Exception e) {	
			throw new WebApiHttpException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), "Could not do GET request to protected JFD Bank API");
		}
	}

}
