package edu.dfci.cccb.mev.test.io.utils;

import static edu.dfci.cccb.mev.io.utils.CCCPHelpers.FileUtils.getModDate;
import static edu.dfci.cccb.mev.io.utils.CCCPHelpers.FileUtils.touch;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestCCCPHelpers_FileUtils {

  @Test
  public void testGetModDate () throws URISyntaxException, IOException {
    
    URL url = this.getClass ().getResource ("/CCCPHelper/FileUtils/test.txt");
    Path path = Paths.get (url.toURI ());
    FileTime fileTime = touch (path);
    assertEquals (fileTime.to (TimeUnit.MINUTES), getModDate (path).to (TimeUnit.MINUTES));
  }

  @Test 
  public void testTouch() throws URISyntaxException, IOException{
    URL url = this.getClass ().getResource ("/CCCPHelper/FileUtils/test.txt");
    Path path = Paths.get (url.toURI ());
    FileTime fileTime = touch (path);
    assertEquals (fileTime.to (TimeUnit.MINUTES), getModDate (path).to (TimeUnit.MINUTES));
  }

}
