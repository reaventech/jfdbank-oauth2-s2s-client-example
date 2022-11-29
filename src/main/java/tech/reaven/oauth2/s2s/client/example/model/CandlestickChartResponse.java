package tech.reaven.oauth2.s2s.client.example.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandlestickChartResponse {
	
	private List<CandlestickChart> list;
}
