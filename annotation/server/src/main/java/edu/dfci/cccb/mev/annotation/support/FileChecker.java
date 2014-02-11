package edu.dfci.cccb.mev.annotation.support;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.extern.log4j.Log4j;
import static edu.dfci.cccb.mev.io.utils.CCCPHelpers.FileUtils.getModDate;;

@Log4j
public class FileChecker {


  public static boolean hasAFileChangedSince(Path rootFolder, String suffix, long miliseconds) throws IOException{
    try(DirectoryStream<Path> ds = Files.newDirectoryStream (rootFolder, suffix)){
      for(Path path : ds){
        if(getModDate (path).toMillis () > (System.currentTimeMillis ()-miliseconds))
          return true;
      }
    }
    return false;
  }
  
  
}
