package com.weldogomes.api;

import org.springframework.stereotype.Component;

@Component
public class ApiUtils {
	public static boolean isNullOrEmpty(String str) {
		if(str != null)
			if(!str.isBlank())
				return false;
		return true;
	}
}
