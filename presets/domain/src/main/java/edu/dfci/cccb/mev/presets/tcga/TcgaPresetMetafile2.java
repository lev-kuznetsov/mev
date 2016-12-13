package edu.dfci.cccb.mev.presets.tcga;

import java.nio.file.Paths;

/**
 * Created by antony on 5/23/16.
 */
public class TcgaPresetMetafile2 extends TcgaPresetMetafile {


    @Override
    protected String getColumnSourceUrlSpec(){
        return Paths.get("tcga_data",
                this.path(),
                "annotation.tsv"
        ).toString();
    }

    @Override
    protected String getColumnUrlSpec() {
        //ACC-clinical_annotations-tsv.openrefine.tar.gz
        return "openrefine/clinical/"+name()+"-clinical_annotations-tsv.openrefine.tar.gz";
    }

}
