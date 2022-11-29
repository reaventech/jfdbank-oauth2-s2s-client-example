package tech.reaven.oauth2.s2s.client.example.model;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebApiHttpException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WebApiHttpError webApiHttpError;
	
	public WebApiHttpException(HttpStatus status, String code, String error, String message) {
		super(message);
		this.webApiHttpError = new WebApiHttpError(status, code, error, message);
	}
}
