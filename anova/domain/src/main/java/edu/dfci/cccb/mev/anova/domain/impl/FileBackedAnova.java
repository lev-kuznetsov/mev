package edu.dfci.cccb.mev.anova.domain.impl;

import static edu.dfci.cccb.mev.anova.domain.prototype.AbstractAnovaBuilder.FULL_FILENAME;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import edu.dfci.cccb.mev.anova.domain.prototype.AbstractAnova;
import edu.dfci.cccb.mev.anova.domain.prototype.AbstractAnovaBuilder;
import edu.dfci.cccb.mev.io.implementation.TemporaryFolder;

public class FileBackedAnova extends AbstractAnova implements AutoCloseable{
  private @Getter final File full;
  private @Getter final TemporaryFolder tempFolder;
  private @Getter List<Entry.Pairing> logFoldChangePairings;

  public FileBackedAnova (TemporaryFolder tempFolder) {
    this.tempFolder = tempFolder;
    this.full = new File (this.tempFolder, FULL_FILENAME);
    stripHeaderLine();
  }
  
  /**
   * Output from R script has a header line denoting the pairwise log-fold-changes.
   * Strip out this header line and save it in a class member variable
   */
  @SneakyThrows (IOException.class)
  private void stripHeaderLine () {
    BufferedReader reader = new BufferedReader (new FileReader (this.full));
    String headerLine=reader.readLine ();
    String[] allFields=headerLine.split ("\t"); //all the fields of the header line (gene, p value,...)
    String[] pairings=Arrays.copyOfRange (allFields, 2, allFields.length); //remove the gene and p_value-- what's left is just the pairings for the log-fold-change 
    //parse out the pairings from the header line:
    logFoldChangePairings=new ArrayList<Entry.Pairing>();
    int count;
    for (String pair: pairings){
      String[] splitPair=pair.trim().split (AbstractAnovaBuilder.PAIRING_DELIMITER);

      count=0;
      String[] partners=new String[2];
      for(String s: splitPair){
        if(s.length ()>0 && !s.equalsIgnoreCase (AbstractAnovaBuilder.PAIRING_DELIMITER)){
          partners[count]=s.trim ();
          count++;
        }
      }
      if(count==2){
        logFoldChangePairings.add (new SimplePairing (partners[0], partners[1]));
      }
      else{
        //throw some parsing error
      }
    }
    
    reader.close ();
  }

  @Override
  public Iterable<Entry> fullResults () {
    return iterateEntries (full);
  }

  private Iterable<Entry> iterateEntries (final File file) {
    return new Iterable<Entry> () {

      /* (non-Javadoc)
       * @see java.lang.Iterable#iterator() */
      @Override
      @SneakyThrows (IOException.class)
      public Iterator<Entry> iterator () {
        return new Iterator<Entry> () {

          private final BufferedReader reader = new BufferedReader (new FileReader (file));
          private String current = null;

          @Override
          @Synchronized
          @SneakyThrows (IOException.class)
          public boolean hasNext () {
            return current == null ? (current = reader.readLine ()) != null : true;
          }

          @Override
          @Synchronized
          public Entry next () {
            hasNext ();
            Entry result = parse (current);
            current = null;
            return result;
          }

          @Override
          public void remove () {
            throw new UnsupportedOperationException ();
          }
        };
      }
    };
  }
  

  private Entry parse (String line) {
    try{
    final String[] split = line.split ("\t");
    Map<Entry.Pairing, Double> map=new HashMap<Entry.Pairing, Double>();
    for (int i=0; i<split.length-2; i++){
      map.put (logFoldChangePairings.get (i), Double.parseDouble (split[i+2]));
    }
    return new SimpleEntry (Double.parseDouble (split[1]),split[0],map);
    }
    //if it reads the header line, it will throw a NumberFormatExcpetion-- catch and return a null
    catch(NumberFormatException e){
      return null;
    }
  }

  
  /* (non-Javadoc)
   * @see java.lang.Object#finalize() */
  @Override
  protected void finalize () throws Throwable {
    close ();
  }
  @Override
  public void close () throws Exception {
    tempFolder.close ();
  }

  @Override
  public List<Entry.Pairing> logFoldChangePairings () {
    return logFoldChangePairings;
  }
}
