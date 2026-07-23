package util.properties;

import java.util.Set;

public interface ConsumableProperties {

	public Set<String> listUnconsumedKeysStartingWith(String s);


	public Boolean consumeBooleanProperty(String key, Boolean defaultValue);

	public Integer consumeIntegerProperty(String key, Integer defaultValue);

	public Double consumeDoubleProperty(String key, Double defaultValue);

	public String consumeProperty(String key, String defaultValue);

	public boolean consumeBooleanProperty(String key);

	public int consumeIntegerProperty(String key);

	public double consumeDoubleProperty(String key);

	public String consumeProperty(String key);
	
	public String getContext();

	public String[] consumeMultipleKeyProperties(String key);
	
}
