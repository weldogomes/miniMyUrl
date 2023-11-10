package com.weldogomes.api.url;

public record UrlSuccessResponseWithTimeVO(String originalUrl, String miniMyUrl, String timeTaken) {
	
	public UrlSuccessResponseWithTimeVO(UrlSuccessResponseVO urlVO, String timeTaken) {
		this(urlVO.originalUrl(), urlVO.miniMyUrl(), timeTaken);
	}
}
