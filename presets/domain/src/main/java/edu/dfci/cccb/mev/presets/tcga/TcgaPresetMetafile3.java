package edu.dfci.cccb.mev.presets.tcga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by antony on 5/23/16.
 */
public class TcgaPresetMetafile3 extends TcgaPresetMetafile {

    public TcgaPresetMetafile3(URL tcgaPresetRoot, URL rowAnnotationsRoot){
        super(tcgaPresetRoot, rowAnnotationsRoot);
    }

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

    @NoArgsConstructor()
    @AllArgsConstructor()
    @Accessors(fluent = true)
    public static class TcgaPresetEntry extends ATcgaPresetEntry {
        private @JsonProperty
        @Getter
        String center;
        private @JsonProperty @Getter String disease;
        private @JsonProperty @Getter String platform;
        private @JsonProperty @Getter String level;
        private @JsonProperty @Getter String location;
        @Override
        public Object[] formatPreset(){
            return new String[]{
                    this.location(),   //0: preset name is the location folder name
                    "data.tsv",         //1: assume file name is "data.tsv"
                    this.location,    //2: folder
                    this.disease(),   //3: disease abbreviation
                    this.disease(),   //4: disease full name (not present in the Json file, using abbreviation instead)
                    this.platform(),  //5: platform abbreviation
                    this.platform(),  //6: platform full name (not present in the Json file, using abbreviation instead)
                    this.level(),     //7: level (2 or 3)
                    null                //8: scale - since normalization analysis was introduced we no longer scale presets
            };
        }
    }
}
