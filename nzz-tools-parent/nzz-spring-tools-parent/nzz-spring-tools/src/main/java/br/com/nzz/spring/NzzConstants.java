package br.com.nzz.spring;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class NzzConstants {

	public static final String INTERNAL_ERROR = "internal.error";
	public static final String INVALID_JSON_ERROR = "invalid.json";
	public static final String INVALID_REQUEST_ERROR = "invalid.request";
	public static final String INTEGRATION_ERROR = "integration.error";

}
