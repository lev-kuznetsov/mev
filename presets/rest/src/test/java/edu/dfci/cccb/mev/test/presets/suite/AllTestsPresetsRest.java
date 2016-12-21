package edu.dfci.cccb.mev.test.presets.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.dfci.cccb.mev.test.presets.domain.presets.TestPresetsSimpleEmpty;
import edu.dfci.cccb.mev.test.presets.domain.presets.TestTcgaPresetMetafile;
import edu.dfci.cccb.mev.test.presets.domain.presets.TestTcgaPresets;
import edu.dfci.cccb.mev.test.presets.rest.TestPresetRest;

@RunWith (Suite.class)
@SuiteClasses ({TestPresetsSimpleEmpty.class, TestTcgaPresetMetafile.class, TestTcgaPresets.class, TestPresetRest.class})
public class AllTestsPresetsRest {

}
