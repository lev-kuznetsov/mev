package edu.dfci.cccb.mev.configuration.util.contract;

public interface Config {

	public abstract String getTest() throws Throwable;

	public abstract String getProperty(String name);
	
	public abstract String[] getStringArray(String key);

  String[] getStringArray (String key, String valueIfNull);

}