package edu.dfci.cccb.mev.test.annotation.support;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.support.FileChecker;

public class TestFileChecker {

  @Test
  public void testGetModDate () throws URISyntaxException, IOException {
    
    URL url = TestFileChecker.class.getResource ("/fileChecker/test.txt");
    Path path = Paths.get (url.toURI ());
    FileTime fileTime = FileChecker.touch (path);
    assertEquals (fileTime.to (TimeUnit.MINUTES), FileChecker.getModDate (path).to (TimeUnit.MINUTES));
  }

  @Test 
  public void testHasAFileChangedSince_True () throws IOException, URISyntaxException {
    URL rootFolder = this.getClass ().getResource ("/fileChecker/changed/");
    URL changedFile = this.getClass ().getResource ("/fileChecker/changed/fileChanged.txt");
    FileChecker.touch (Paths.get (changedFile.toURI ()));
    
    assertTrue(FileChecker.hasAFileChangedSince (Paths.get (rootFolder.toURI ()), "*.txt", 2000L));
  }

  @Test 
  public void testHasAFileChangedSince_False () throws IOException, URISyntaxException {
    URL rootFolder = this.getClass ().getResource ("/fileChecker/noChange/");
    URL changedFile = this.getClass ().getResource ("/fileChecker/noChange/fileChanged.wrong.extension");
    FileChecker.touch (Paths.get (changedFile.toURI ()));
    
    assertFalse(FileChecker.hasAFileChangedSince (Paths.get (rootFolder.toURI ()), "*.txt", 2000L));
  }

  
  @Test 
  public void testTouch() throws URISyntaxException, IOException{
    URL url = TestFileChecker.class.getResource ("/fileChecker/test.txt");
    Path path = Paths.get (url.toURI ());
    FileTime fileTime = FileChecker.touch (path);
    assertEquals (fileTime.to (TimeUnit.MINUTES), FileChecker.getModDate (path).to (TimeUnit.MINUTES));
  }
}
