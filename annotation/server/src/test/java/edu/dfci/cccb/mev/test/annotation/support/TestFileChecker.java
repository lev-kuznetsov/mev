package edu.dfci.cccb.mev.test.annotation.support;

import static edu.dfci.cccb.mev.io.utils.CCCPHelpers.FileUtils.touch;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.Test;

import edu.dfci.cccb.mev.annotation.support.FileChecker;

public class TestFileChecker {

  @Test 
  public void testHasAFileChangedSince_True () throws IOException, URISyntaxException {
    URL rootFolder = this.getClass ().getResource ("/fileChecker/changed/");
    URL changedFile = this.getClass ().getResource ("/fileChecker/changed/fileChanged.txt");
    touch (Paths.get (changedFile.toURI ()));
    
    assertTrue(FileChecker.hasAFileChangedSince (Paths.get (rootFolder.toURI ()), "*.txt", 2000L));
  }

  @Test 
  public void testHasAFileChangedSince_False () throws IOException, URISyntaxException {
    URL rootFolder = this.getClass ().getResource ("/fileChecker/noChange/");
    URL changedFile = this.getClass ().getResource ("/fileChecker/noChange/fileChanged.wrong.extension");
    touch (Paths.get (changedFile.toURI ()));
    
    assertFalse(FileChecker.hasAFileChangedSince (Paths.get (rootFolder.toURI ()), "*.txt", 2000L));
  }
  
}
