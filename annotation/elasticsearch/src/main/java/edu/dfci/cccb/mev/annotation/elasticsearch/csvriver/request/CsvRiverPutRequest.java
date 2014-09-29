package edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.request;

import edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.contract.AnnotationPutRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CsvRiverPutRequest implements AnnotationPutRequest {
  
  private final @Getter String index;
  private final @Getter String type;
  private final @Getter String riverName;
  private final String folder;
  private final String filenamePattern;
  
  
  @Override
  public String toString () {   
    return String.format("{\n" + 
            "    \"type\" : \"csv\",\n" + 
            "    \"csv_file\" : {\n" + 
            "        \"folder\" : \"%1$s\",\n" + 
            "        \"filename_pattern\" : \"%2$s\",\n" + 
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
            "        \"index\" : \"%3$s\",\n" + 
            "        \"type\" : \"%4$s\",\n" + 
            "        \"bulk_size\" : 100,\n" + 
            "        \"bulk_threshold\" : 10\n" + 
            "    }\n" + 
            "}", folder, filenamePattern, index, type);
  }

  
}
