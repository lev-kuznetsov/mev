package edu.dfci.cccb.mev.analysis.r.histogram;

import static javax.persistence.CascadeType.ALL;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.analysis.Define;
import edu.dfci.cccb.mev.analysis.Resolve;
import edu.dfci.cccb.mev.analysis.r.Adapter;
import edu.dfci.cccb.mev.analysis.r.Rscript;
import edu.dfci.cccb.mev.dataset.Dataset;
import io.fabric8.annotations.Path;

/**
 * Histogram analysis
 * 
 * @author levk
 */
@Entity
@Rscript ("/histogram.R")
public class Histogram extends Adapter {
  /**
   * Dataset
   */
  private @Define @OneToOne (cascade = ALL) Dataset dataset;
  /**
   * Breaks
   */
  private @Resolve @ElementCollection List <Integer> breaks;
  /**
   * Counts
   */
  private @Resolve @ElementCollection List <Integer> counts;
  /**
   * Density
   */
  private @Resolve @ElementCollection List <Double> density;
  /**
   * Mids
   */
  private @Resolve @ElementCollection List <Double> mids;

  /**
   * @param dataset
   */
  @PUT
  @Path ("dataset")
  @JsonProperty (required = false)
  public void dataset (Dataset dataset) {
    this.dataset = dataset;
  }

  /**
   * @return breaks
   */
  @GET
  @Path ("breaks")
  @JsonIgnore
  public List <Integer> breaks () {
    return breaks;
  }

  /**
   * @return counts
   */
  @GET
  @Path ("counts")
  @JsonIgnore
  public List <Integer> counts () {
    return counts;
  }

  /**
   * @return density
   */
  @GET
  @Path ("density")
  @JsonIgnore
  public List <Double> density () {
    return density;
  }

  /**
   * @return mids
   */
  @GET
  @Path ("mids")
  @JsonIgnore
  public List <Double> mids () {
    return mids;
  }
}
