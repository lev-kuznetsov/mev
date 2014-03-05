package edu.dfci.cccb.mev.presets.util.timer;

import java.text.DecimalFormat;

import lombok.extern.log4j.Log4j;
@Log4j
public class Timer{
  private static DecimalFormat formatter = new DecimalFormat("0.######E0");    
  String name;
  long startTime=0;
  long lastPolledTime=0;    
  public Timer(){this("timer");};
  public Timer(String name){
    this.name=name;
    this.startTime=System.nanoTime();
  }
  
  public long start(){
    return this.startTime=System.nanoTime();
  }
  public long poll(){
    this.lastPolledTime = System.nanoTime();
    return this.lastPolledTime - startTime;      
  }
  public long stop(){
    long endTime = System.nanoTime();
    return endTime - startTime;      
  }
  public long read(){
    long duration = stop();
    log.debug ("Timer::"+name+":"+formatter.format(duration));
    return duration;
  }
  public static Timer start(String name){
    return new Timer(name);      
  }
}
