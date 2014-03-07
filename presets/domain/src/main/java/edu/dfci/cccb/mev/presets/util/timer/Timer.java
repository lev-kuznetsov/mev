package edu.dfci.cccb.mev.presets.util.timer;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j;
@Log4j
public class Timer{
  private static DecimalFormat formatter = new DecimalFormat("0.######E0");    
  String name;
  long startTime=0;
  long lastPolledTime=0;
  final Map<String, Long> marks;
  
  public Timer(){this("timer");};
  public Timer(String name){
    this.name=name;
    this.startTime=System.nanoTime();
    this.lastPolledTime=startTime;
    this.marks=new LinkedHashMap<String, Long>();
  }
  
  public long start(){
    this.startTime=System.nanoTime();
    return this.lastPolledTime=startTime;
  }
  public static Timer start(String name){
    return new Timer(name);      
  }
  public long poll(){
    long endTime = System.nanoTime();
    return endTime - startTime;      
  }
  
  public long readAll(){
    long duration = poll();
    long lastMark = startTime;
    for(Map.Entry<String, Long> entry: marks.entrySet ()){
      log.debug ("Timer:"+name+":"+entry.getKey ()+":"+formatter.format(entry.getValue ()-lastMark));
      lastMark=entry.getValue ();
    }
    log.debug ("Timer:"+name+":"+formatter.format(duration));
    return duration;
  }  
  public long read(){
    long duration = poll();
    log.debug ("Timer:"+name+":"+formatter.format(duration));
    return duration;
  }
  public long read(String label){
    long now =System.nanoTime ();
    long duration = now-lastPolledTime;
    lastPolledTime=now;
    log.debug ("Timer:"+name+":"+label+":"+formatter.format(duration));
    return duration;
  }
  
  @Synchronized
  public void mark(String label){
    marks.put (label, System.nanoTime ());
  }
  
}
