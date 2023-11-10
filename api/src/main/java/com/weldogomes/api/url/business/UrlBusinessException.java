package com.weldogomes.api.url.business;

@SuppressWarnings("serial")
public class UrlBusinessException extends Exception{
	
	public UrlBusinessException() {}
	
	public UrlBusinessException(String message) {
		super(message);
	}
	
	public UrlBusinessException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UrlBusinessException(Throwable cause) {
		super(cause);
	}
}
