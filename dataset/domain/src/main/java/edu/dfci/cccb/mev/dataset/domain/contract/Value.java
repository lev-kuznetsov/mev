package edu.dfci.cccb.mev.dataset.domain.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Value {
	@JsonProperty String row();
	@JsonProperty String column();
	@JsonProperty double value();
}
