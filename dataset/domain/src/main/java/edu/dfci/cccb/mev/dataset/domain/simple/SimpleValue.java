package edu.dfci.cccb.mev.dataset.domain.simple;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValue;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
public class SimpleValue extends AbstractValue {

	private @Getter String row;
	private @Getter String column;
	private @Getter double value;  

}
