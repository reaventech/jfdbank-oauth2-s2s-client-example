package tech.reaven.oauth2.s2s.client.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import tech.reaven.oauth2.s2s.client.example.config.PriceFeedApiConfiguration;
import tech.reaven.oauth2.s2s.client.example.config.SecurityParametersConfig;
import tech.reaven.oauth2.s2s.client.example.model.CandlestickChart;
import tech.reaven.oauth2.s2s.client.example.model.WebApiHttpError;
import tech.reaven.oauth2.s2s.client.example.model.WebApiHttpException;

@Configuration
@RestController
@RequestMapping("api/oauth2-api-call-with-webclient")
@Log4j2
@NoArgsConstructor
// Used to make test calls from this to JFD Bank API which is protected by OAuth2. Uses WebClient method.
public class OAuth2ApiCallWithWebClientRestController {
	
	@Autowired
	private SecurityParametersConfig securityParametersConfig;
	
	@Autowired
	private PriceFeedApiConfiguration priceFeedApiConfiguration;
	
	@Autowired
	private WebClient webClient;
	
	public SecurityParametersConfig getSecurityParametersConfig() {
		try {
			log.info("OAuth2ApiCallWithWebClientRestController.getSecurityParametersConfig(): securityParametersConfig {}"
					, securityParametersConfig);		
			
            return securityParametersConfig;
		}
		catch (Exception e) {
			log.error("Exception at OAuth2ApiCallWithWebClientRestController.getSecurityParametersConfig()", e);

			return null;
		}
	}
	
	// Calls a JFD Bank API with GET protected with OAuth2 server. 
	// This method candle stick formatted price feed of a given ISIN code financial instrument
	// This uses WebClient for this authorized API call
	// GET http://localhost:9080/api/oauth2-api-call-with-webclient/{isinCode}	
	@GetMapping("/{isinCode}")
	List<CandlestickChart> getCandlestickChartDataViaWebClient(@PathVariable(required = true) String isinCode) {
		try {
			if (isinCode == null) {
				throw new WebApiHttpException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), "isinCode null", "Could not do GET request to protected JFD Bank API");
			}
			
			String priceFeedAPICandlestickUrl = null;
			if (priceFeedApiConfiguration.getPort() == null || priceFeedApiConfiguration.getPort().isBlank()) {
				priceFeedAPICandlestickUrl = priceFeedApiConfiguration.getUrl() + "/api/candlestick/" + isinCode + "?range=d1";
			}
			else {
				priceFeedAPICandlestickUrl = priceFeedApiConfiguration.getUrl() + ":" + priceFeedApiConfiguration.getPort() + "/api/candlestick/" + isinCode + "?range=d1";
			}
			
			// Call JFD Bank Price Feed API with Authorization
			Mono<List<CandlestickChart>> response = 
					webClient.get()
					.uri(priceFeedAPICandlestickUrl)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<CandlestickChart>>() {});
			
			List<CandlestickChart> candlestickCharts = response.block();
					
			return candlestickCharts;
			
		} catch(Exception e) {	
			throw new WebApiHttpException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), "Could not do GET request to protected JFD Bank API");
		}		
	}		
	
	// Calls a JFD Bank API with POST protected with OAuth2 server.
	// This uses WebClient for this authorized API call
	@PostMapping("")
	List<CandlestickChart> postIsinCodeToGetCandlestickChartDataViaWebClient(
			@RequestBody tech.reaven.oauth2.s2s.client.example.model.IsinCode isinCode) throws Exception {
		try {			
			if (isinCode == null) {
				throw new WebApiHttpException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), "isinCode null", "Could not do GET request to protected JFD Bank API");
			}
			
			String priceFeedAPICandlestickUrl = null;
			if (priceFeedApiConfiguration.getPort() == null || priceFeedApiConfiguration.getPort().isBlank()) {
				priceFeedAPICandlestickUrl = priceFeedApiConfiguration.getUrl() + "/api/candlestick/" + isinCode.getIsin() + "?range=d1";
			}
			else {
				priceFeedAPICandlestickUrl = priceFeedApiConfiguration.getUrl() + ":" + priceFeedApiConfiguration.getPort() + "/api/candlestick/" + isinCode.getIsin() + "?range=d1";
			}
			
			// Call JFD Bank Price Feed API with Authorization
			Mono<List<CandlestickChart>> response = 
					webClient.get()
					.uri(priceFeedAPICandlestickUrl)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<CandlestickChart>>() {});
			
			List<CandlestickChart> candlestickCharts = response.block();
					
			return candlestickCharts;
			
		} catch(Exception e) {	
			throw new WebApiHttpException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), "Could not do GET request to protected JFD Bank API");
		}
	}
}
