package tech.reaven.oauth2.s2s.client.example.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CandlestickChart {

	@JsonProperty("hi")
	private BigDecimal priceHigh;
	
	@JsonProperty("lo")
	private BigDecimal priceLow;
	
	@JsonProperty("op")
	private BigDecimal openPrice;

	@JsonProperty("cl")
	private BigDecimal closePrice;
	
	// YYYYMMDDHHMM
	@JsonProperty("dt")
	private String dateTime;
	
	@JsonProperty("vol")
	private int volume;
}
