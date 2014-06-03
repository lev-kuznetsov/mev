package edu.dfci.cccb.mev.goeds.domain.dataset.ftp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import edu.dfci.cccb.mev.goeds.domain.contract.GeoSource;

@RequiredArgsConstructor
public class GeoSourceFTP implements GeoSource {
  
  @Getter private final String gds;
  @Getter private final String gpl;
  @Getter private final Map<String, String> samples;
  private final URL NCBI_FTP_ROOT;
  
  //ftp://ftp.ncbi.nlm.nih.gov/geo/datasets/GDS1nnn/GDS1001/
  private final String DATASET_SOFT_URL="datasets/GDS%s/GDS%s/soft/GDS%s.soft.gz";
  //ftp://ftp.ncbi.nlm.nih.gov/geo/platforms/GPLnnn/GPL570/annot/GPL570.annot.gz
  private final String PLATFORM_ANNOT_URL="platforms/GPL%s/GPL%s/annot/GPL%s.annot.gz";
  
  private String getSubfolderString(String id){
    //per ncbi instrutions
    //replace last three digits of id with "nnn"
    if(id.length()<3)
      return id;
    else if(id.length ()==3)
      return "nnn";
    else
      return id.substring (0, id.length()-3)+"nnn";
  }
  
  @Override
  @SneakyThrows(MalformedURLException.class)
  public URL getPlatformUrl () {    
    return new URL(NCBI_FTP_ROOT, String.format (PLATFORM_ANNOT_URL, getSubfolderString (gpl), gpl, gpl));
  }

  @Override
  @SneakyThrows(MalformedURLException.class)
  public URL getDatasetUrl () {    
    return new URL(NCBI_FTP_ROOT, String.format (DATASET_SOFT_URL, getSubfolderString (gds), gds, gds));
  }

}
