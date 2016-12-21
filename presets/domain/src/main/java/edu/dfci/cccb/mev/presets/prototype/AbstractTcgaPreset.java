package edu.dfci.cccb.mev.presets.prototype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.dfci.cccb.mev.presets.contract.TcgaPreset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;

@Accessors(fluent = true)
@NoArgsConstructor
public abstract class AbstractTcgaPreset extends AbstractPreset  implements TcgaPreset {
    @JsonIgnore(value=true) private @Getter @Setter @Inject @Named("tcgaPresetRoot") URL tcgaPresetRoot;
    @JsonIgnore(value=true) private @Getter @Setter @Inject @Named ("probe-annotations-root") URL rowAnnotationsRoot;
}
