package edu.dfci.cccb.mev.annotation.server.configuration;

import javax.annotation.PostConstruct;

import lombok.ToString;
import lombok.extern.log4j.Log4j;

@Log4j
public class MyBeanClass {

  public MyBeanClass (int i) {
    log.warn ("CALLED MyBeanClass() for " + System.identityHashCode (this) + " with i=" + i);
  }
  
  @PostConstruct
  private void init () {
    log.warn ("CALLED MyBeanClass.@PostConstruct for " + + System.identityHashCode (this));
  }
}
