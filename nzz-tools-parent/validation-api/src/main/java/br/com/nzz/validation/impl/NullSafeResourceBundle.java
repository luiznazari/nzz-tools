package br.com.nzz.validation.impl;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Luiz.Nazari
 */
@Slf4j
public class NullSafeResourceBundle extends ResourceBundle {

	private ResourceBundle delegate;

	public NullSafeResourceBundle(String bundleBaseName) {
		try {
			this.delegate = ResourceBundle.getBundle(bundleBaseName);
		} catch (MissingResourceException e) {
			this.delegate = null;
			log.warn(" Can't find bundle for base name " + bundleBaseName, e);
		}
	}

	@Override
	protected Object handleGetObject(String key) {
		if (this.delegate != null && this.delegate.containsKey(key)) {
			return this.delegate.getString(key);
		} else {
			return "[" + key + "]";
		}
	}

	@Override
	public Enumeration<String> getKeys() {
		if (this.delegate != null) {
			return this.delegate.getKeys();
		}
		return Collections.emptyEnumeration();
	}

	@Override
	public boolean containsKey(String key) {
		return true;
	}

}