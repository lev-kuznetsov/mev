package edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.reqeust.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.contract.AnnotationPutRequest;
import edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.request.CsvRiverPutRequest;

public class TestCsvRiverPutRequest {

  @Test
  public void testToString () {
   AnnotationPutRequest request = new CsvRiverPutRequest ("tcga_clinical", "acc", "tcga_clinical_river", "/folder/to/index", "file.*pattern");
   assertEquals ("{\n" + 
           "    \"type\" : \"csv\",\n" + 
           "    \"csv_file\" : {\n" + 
           "        \"folder\" : \"/folder/to/index\",\n" + 
           "        \"filename_pattern\" : \"file.*pattern\",\n" + 
           "        \"poll\":\"5m\",        \n" + 
           "        \"first_line_is_header\" : \"true\",\n" + 
           "        \"field_separator\" : \"\\t\",\n" + 
           "        \"escape_character\" : \"\",\n" + 
           "        \"quote_character\" : \"'\",\n" + 
           "        \"field_id\" : \"id\",\n" + 
           "        \"field_timestamp\" : \"imported_at\",\n" + 
           "        \"concurrent_requests\" : \"1\",\n" + 
           "        \"charset\" : \"UTF-8\"\n" + 
           "    },\n" + 
           "    \"index\" : {\n" + 
           "        \"index\" : \"tcga_clinical\",\n" + 
           "        \"type\" : \"acc\",\n" + 
           "        \"bulk_size\" : 100,\n" + 
           "        \"bulk_threshold\" : 10\n" + 
           "    }\n" + 
           "}", request.toString ());
  }

}
