package com.weldogomes.api.url;

import jakarta.validation.constraints.NotBlank;

public record UrlEntryVO(
		@NotBlank
		String originalUrl, 
		String customAlias
) {}
