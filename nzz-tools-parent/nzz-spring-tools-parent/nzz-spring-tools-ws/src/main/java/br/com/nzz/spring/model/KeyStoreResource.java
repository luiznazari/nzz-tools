package br.com.nzz.spring.model;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Optional;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.Setter;

@Getter
public class KeyStoreResource {

	@Setter
	private String password;
	@Setter
	private String certificatePassword;

	private final KeyStoreType type;
	private final InputStream inputStream;

	public KeyStoreResource(@Nonnull KeyStoreType type, @Nonnull InputStream inputStream) {
		this.type = type;
		this.inputStream = inputStream;
	}

	public String getCertificatePassword() {
		return Optional.ofNullable(this.certificatePassword).orElseGet(this::getPassword);
	}

	public String getPassword() {
		return Optional.ofNullable(this.password).orElse(StringUtils.EMPTY);
	}

}
