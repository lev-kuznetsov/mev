package edu.dfci.cccb.mev.configuration.util.composite;

import java.util.List;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.configuration.util.prototype.AbstractConfig;

public class CompositeConfig extends AbstractConfig implements Config {

	private final List<Config> configs;
	public CompositeConfig(List<Config> configs){
		this.configs = configs;
	}
	
	@Override
	public String getProperty(String name) {
		for(Config config : this.configs){
			String value = config.getProperty(name);
			if(value!=null) return value;
		}
		return null;
	}

}
