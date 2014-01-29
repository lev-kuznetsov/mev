package edu.dfci.cccb.mev.test.presets.domain;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetNotFoundException;
import edu.dfci.cccb.mev.presets.simple.SimplePresests;
import edu.dfci.cccb.mev.presets.simple.TcgaPresetMetafile;

public class PresetsSimpleTestEmpty {

  private Presets presets=null;
  
  @Before
  public void createEmpty(){
    this.presets = new SimplePresests ();
  }
  @After
  public void destroy(){
    this.presets = null;
  }
  
  @Test(expected=PresetNotFoundException.class)
  public void testGet () throws PresetNotFoundException {
    this.presets.get ("notthere");
  }
  
  @Test
  public void testPut () throws PresetNotFoundException, PresetException {
    //this.presets.put (new TcgaPresetMetafile ("", "", "", "", "", ""));    
  }
  
  @Test(expected=PresetNotFoundException.class)
  public void testRemove() throws PresetNotFoundException, MalformedURLException {
    this.presets.remove ("");   
  }

  @Test
  public void testList() throws PresetNotFoundException, MalformedURLException {
    assert(this.presets.list ().size ()==0);   
  }
  
  @Test
  public void testGetAll() throws PresetNotFoundException, MalformedURLException {
    assert(this.presets.getAll ().size ()==0);   
  }
  

}
