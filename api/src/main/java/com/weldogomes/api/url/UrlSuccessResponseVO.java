package com.weldogomes.api.url;

public record UrlSuccessResponseVO(String originalUrl, String miniMyUrl) {

	private static final String API_URL_PATH = "http://minimize.my/url/a/";
	
	public UrlSuccessResponseVO(Url url) {
		this(url.getOriginalUrl(), API_URL_PATH + url.getAlias());
	}
}
