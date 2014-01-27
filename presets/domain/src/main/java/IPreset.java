import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;


public interface IPreset {
  String name();
  PresetDescriptor getDescriptor();
}
