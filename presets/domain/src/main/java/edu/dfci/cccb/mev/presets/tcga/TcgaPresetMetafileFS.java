
package edu.dfci.cccb.mev.presets.tcga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.io.FilenameUtils;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by antony on 5/23/16.
 */
public class TcgaPresetMetafileFS extends TcgaPresetMetafile {

    private static final String TCGA_FS_CLINICAL_SUFFIX = "-Clinical.txt";

    public TcgaPresetMetafileFS(URL tcgaPresetRoot, URL rowAnnotationsRoot){
        super(tcgaPresetRoot, rowAnnotationsRoot);
    }

    @Override
    protected String getDataUrlSpec() {
        return "tcga_cleaned_datasets/"+path()+"/"+filename();
    }

    @Override
    protected String getColumnSourceUrlSpec(){
        return Paths.get("tcga_cleaned_datasets",
                this.path(),
                this.filename().substring(0, this.filename().lastIndexOf("-"))+TCGA_FS_CLINICAL_SUFFIX
        ).toString();
    }

    @Override
    protected String getColumnUrlSpec() {
        //ACC-clinical_annotations-tsv.openrefine.tar.gz
        return "openrefine/clinical/tcga_cleaned_datasets/"+disease()+"-clinical_annotations-tsv.openrefine.tar.gz";
    }

    @NoArgsConstructor()
    @AllArgsConstructor()
    @Accessors(fluent = true)
    public static class TcgaPresetEntry extends ATcgaPresetEntry {
        private @JsonProperty @Getter String disease;
        private @JsonProperty @Getter String platform;
        private @JsonProperty @Getter String level;
        private @JsonProperty @Getter String location;
        private @JsonProperty @Getter String fileName;
        private @Getter Path filePath;
        public TcgaPresetEntry(Path filePath){
            this.filePath = filePath;
            this.fileName = FilenameUtils.getName(filePath().toFile().getName());
            String[] aFileName = fileName().split("-");
            this.disease=aFileName[1];
            this.platform=aFileName[2];
            this.level = this.platform.toLowerCase().endsWith("gene")
                    ? "3" : "2";
            this.location=disease;
        }
        @Override
        public boolean isValid(){
            return !fileName().endsWith(TCGA_FS_CLINICAL_SUFFIX);
        }
        @Override
        public Object[] formatPreset(){
            return new String[]{
                    this.fileName(),   //0: preset name is the file name
                    this.fileName(),       //1: assume file name is "data.tsv"
                    this.location(),    //2: folder - the folder is the desease name
                    this.disease(),   //3: disease abbreviation
                    this.disease(),   //4: disease full name (not present in the filename, using abbreviation instead)
                    this.platform(),  //5: platform abbreviation
                    this.platform(),  //6: platform full name (not present in the filename, using abbreviation instead)
                    this.level(),     //7: level (2 or 3)
                    null                //8: scale - since normalization analysis was introduced we no longer scale presets
            };
        }
    }
}
