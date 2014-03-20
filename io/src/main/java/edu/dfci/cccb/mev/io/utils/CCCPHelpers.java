package edu.dfci.cccb.mev.io.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import org.apache.commons.io.FilenameUtils;

import lombok.extern.log4j.Log4j;

@Log4j
public class CCCPHelpers {

  public static class FileUtils {
    public static FileTime getModDate (Path path) throws IOException {
      BasicFileAttributeView basicView =Files.getFileAttributeView (path, BasicFileAttributeView.class,LinkOption.NOFOLLOW_LINKS);
      if (basicView != null) {
        BasicFileAttributes basic = basicView.readAttributes (); // Get the
                                                                 // attributes
                                                                 // of the view

        FileTime result = basic.lastModifiedTime ();
        if (result == null)
          throw new IOException ("Attribute lastModifiedTime() is not suupported on file: " + path);
        else {
          if (log.isDebugEnabled ())
            log.debug ("File " + path + "last modified at  : " + result);
          return result;

        }

      } else {
        throw new IOException ("File not found: " + path);
      }

    }

    public static FileTime touch (Path path) throws IOException {
      FileTime fileTime = FileTime.fromMillis (System.currentTimeMillis ());
      Files.setAttribute (path, "lastModifiedTime", fileTime);
      return fileTime;
    }
  };

  public static class UrlUtils{
    public static boolean checkExists (URL url) {
      try (InputStream in = url.openStream () ) {
        return true;
      } catch (Exception e) {
        return false;
      }
    }
    
    public static String getFileName(URL url){
      return FilenameUtils.getName (url.getFile ());
    }
  }
  
}
