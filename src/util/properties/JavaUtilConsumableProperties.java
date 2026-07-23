package util.properties;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import util.Files;

public class JavaUtilConsumableProperties extends AbstractConsumableProperties {

	private java.util.Properties internal;

	public JavaUtilConsumableProperties(String file) {
		this(loadProperties(file));
	}

	private static Properties loadProperties(String file) {
		try {
			return Files.loadProperties(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public JavaUtilConsumableProperties(java.util.Properties javaUtilProps) {
		super();
		internal = javaUtilProps;
		Set<String> spn = internal.stringPropertyNames();
		loadUnconsumedKeys(spn);
	}

	@Override
	protected String readProperty(String key) {
		return internal.getProperty(key);
	}

	@Override
	public String getContext() {
		return "";
	}

	@Override
	protected String readProperty(String key, String defaultValue) {
		return internal.getProperty(key, defaultValue);
	}

	@Override
	protected boolean containsKey(String key) {
		return internal.containsKey(key);
	}

	@Override
	public String[] readMultipleKeyProperties(String key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("ainda não implementado");
	}

}
