package edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.reqeust.test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.log4j.Log4j;

import org.junit.Test;
import edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.contract.CsvRiverAnnotationPutRequest;
import edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.request.CsvRiverPutRequestTcgaPreset;
import edu.dfci.cccb.mev.annotation.elasticsearch.preset.TcgaPresestMetafileColumnCsv;
import edu.dfci.cccb.mev.presets.contract.TcgaPreset;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.tcga.TcgaPresetMetafile;

@Log4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes={PresetsImportAppConfiguration.class})
public class TestCsvRiverPutRequestTcgaPreset {

   
  @Test
  public void testToString () throws PresetException, MalformedURLException {
    TcgaPresetMetafile preset = new TcgaPresestMetafileColumnCsv ();
    preset.tcgaPresetRoot (new URL("file:/tcga_root/"));
    preset.rowAnnotationsRoot (new URL("file:/row_annotations/"));    
    preset.init ("file.txt", "test/path", "flu", "birdflue", "affimetrix", "affimetrix", "level_2", null);
    CsvRiverAnnotationPutRequest request = new CsvRiverPutRequestTcgaPreset ((TcgaPreset)preset);
    
    log.debug (request);        
    assertEquals (request.toString (), "{\n" + 
            "    \"type\" : \"csv\",\n" + 
            "    \"csv_file\" : {\n" + 
            "        \"folder\" : \"/tcga_root/clinical/flu/clinical\",\n" + 
            "        \"filename_pattern\" : \"flu.clinical_annotations.tsv\",\n" + 
            "        \"poll\":\"5m\",        \n" + 
            "        \"first_line_is_header\" : \"true\",\n" + 
            "        \"field_separator\" : \"\\t\",\n" + 
            "        \"escape_character\" : \"\\\\\",\n" + 
            "        \"quote_character\" : \"'\",\n" + 
            "        \"field_id\" : \"id\",\n" + 
            "        \"field_timestamp\" : \"imported_at\",\n" + 
            "        \"concurrent_requests\" : \"1\",\n" + 
            "        \"charset\" : \"UTF-8\"\n" + 
            "    },\n" + 
            "    \"index\" : {\n" + 
            "        \"index\" : \"tcga_clinical\",\n" + 
            "        \"type\" : \"flu\",\n" + 
            "        \"bulk_size\" : 100,\n" + 
            "        \"bulk_threshold\" : 10\n" + 
            "    }\n" + 
            "}");
    
    log.debug (request.getIndex ());
    assertThat(request.getIndex (), is("tcga_clinical"));
    log.debug (request.getType ());
    assertThat(request.getType (), is("flu"));
    log.debug (request.getRiverName ());
    assertThat(request.getRiverName (), is("tcga_clinical_river_flu"));
  }
  
  
}
