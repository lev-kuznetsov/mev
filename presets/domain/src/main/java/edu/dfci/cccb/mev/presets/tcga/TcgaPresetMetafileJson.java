package edu.dfci.cccb.mev.presets.tcga;

import java.nio.file.Paths;

/**
 * Created by antony on 5/23/16.
 */
public class TcgaPresetMetafileJson extends TcgaPresetMetafile {


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

    @Override
    protected String getRowUrlSpec() {
        //ACC-clinical_annotations-tsv.openrefine.tar.gz
        if(this.dataLevel().trim().equalsIgnoreCase ("3"))
            //geneSymbol_goAnnotations-tsv.google-refine.tar.gz
            return "openrefine/geneSymbol_goAnnotations-tsv.google-refine.tar.gz";
        else
            //HG-U133_Plus_2-na33-annot-out-tsv.google-refine.tar.gz
            return "openrefine/"+this.platform()+"-na33-annot-out-tsv.google-refine.tar.gz";
    }

}
