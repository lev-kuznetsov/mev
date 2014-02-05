package edu.dfci.cccb.mev.annotation.support;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileChecker {

  public static FileTime getModDate(Path path) throws IOException{
    BasicFileAttributeView basicView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
    if (basicView != null) {
        BasicFileAttributes basic = basicView.readAttributes(); //Get the attributes of the view
        
        FileTime result = basic.lastModifiedTime();
        if(result == null)
          throw new IOException ("Attribute lastModifiedTime() is not suupported on file: "+path); 
        else{
          if(log.isDebugEnabled ())
            log.debug ("File "+path+"last modified at  : " + result);
          return result;
          
        }
        
    }else{
      throw new IOException ("File not found: "+path);
    }
    
  }
  
  public static boolean hasAFileChangedSince(Path rootFolder, String suffix, long miliseconds) throws IOException{
    try(DirectoryStream<Path> ds = Files.newDirectoryStream (rootFolder, suffix)){
      for(Path path : ds){
        if(getModDate (path).toMillis () > (System.currentTimeMillis ()-miliseconds))
          return true;
      }
    }
    return false;
  }
  
  public static FileTime touch(Path path) throws IOException{
    FileTime fileTime = FileTime.fromMillis(System.currentTimeMillis ());
    Files.setAttribute(path, "lastModifiedTime", fileTime);
    return fileTime;
  }
}
