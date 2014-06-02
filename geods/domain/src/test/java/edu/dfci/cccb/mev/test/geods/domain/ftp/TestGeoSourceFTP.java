package edu.dfci.cccb.mev.test.geods.domain.ftp;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.goeds.domain.contract.GeoSource;
import edu.dfci.cccb.mev.goeds.domain.dataset.ftp.GeoSourceFTP;

public class TestGeoSourceFTP {

  URL root;
  
  @Before
  public void setUp() throws MalformedURLException{
    root = new URL ("ftp://ftp.ncbi.nlm.nih.gov/geo/");
  }
  
  @Test 
  public void testGetPlatformUrl () throws MalformedURLException {  
    GeoSource geoSource = new GeoSourceFTP("4092", "570", new HashMap<String, String>(), root);
    assertThat (geoSource.getPlatformUrl(), 
                is(new URL("ftp://ftp.ncbi.nlm.nih.gov/geo/platforms/GPLnnn/GPL570/annot/GPL570.annot.gz")));
  }

  @Test
  public void testGetDatasetUrl () throws MalformedURLException {
    //ftp://ftp.ncbi.nlm.nih.gov/geo/datasets/GDSnnn/GDS4092/soft/4092.soft.gz
    GeoSource geoSource = new GeoSourceFTP ("4092", "570", new HashMap<String, String>(), root);
    assertThat (geoSource.getDatasetUrl (), 
                is(new URL("ftp://ftp.ncbi.nlm.nih.gov/geo/datasets/GDS4nnn/GDS4092/soft/GDS4092.soft.gz")));
  }

}
