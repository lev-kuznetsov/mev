package com.google.refine;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lombok.extern.log4j.Log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Log4j
public class SessionListener implements HttpSessionListener {
  static final Logger logger = LoggerFactory.getLogger ("refine");
  private int sessionCount = 0;

  public void sessionCreated (HttpSessionEvent event) {

    synchronized (this) {
      sessionCount++;
      /* HttpSession session = event.getSession(); FileProjectManager
       * fileProjectManager = new FileProjectManager(
       * event.getSession().getServletContext().getInitParameter("refine.data")
       * ); session.setAttribute("projectManager", value); */
    }

    logger.info ("DEBUG {}", "Session Created: " + event.getSession ().getId ());
    log.debug ("Session Created: " + event.getSession ().getId ());
    log.debug ("Total Sessions: " + sessionCount);
  }

  public void sessionDestroyed (HttpSessionEvent event) {
    synchronized (this) {
      sessionCount--;
    }
    logger.info ("DEBUG {}", "Session Destroyed: " + event.getSession ().getId ());
    log.debug ("Session Destroyed: " + event.getSession ().getId ());
    log.debug ("Total Sessions: " + sessionCount);
  }

}
