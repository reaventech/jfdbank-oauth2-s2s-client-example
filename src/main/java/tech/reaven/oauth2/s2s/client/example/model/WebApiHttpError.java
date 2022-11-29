package tech.reaven.oauth2.s2s.client.example.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebApiHttpError {
	
	public HttpStatus status;
	public String timestamp;
	public String code;
	public String message;
	public String error;
	public String exception;
	
	public WebApiHttpError(HttpStatus status, String code, String error, String message) {
		this.status = status;
		this.code = code;
		this.error = error;
		this.message = message;
		this.timestamp = LocalDateTime.now(ZoneOffset.UTC).toString();
	}
}
