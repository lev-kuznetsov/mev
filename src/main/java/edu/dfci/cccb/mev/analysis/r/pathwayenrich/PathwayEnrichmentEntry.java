/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Dana-Farber Cancer Institute
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package edu.dfci.cccb.mev.analysis.r.pathwayenrich;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Pathway enrichment result entry
 * 
 * @author levk
 */
@Entity
public class PathwayEnrichmentEntry {
  /**
   * Identifier
   */
  private @Id @GeneratedValue (strategy = AUTO) long id;
  /**
   * Description
   */
  private @Column @Basic @JsonProperty String description;
  /**
   * Gene ration
   */
  private @Column @Basic @JsonProperty String geneRatio;
  /**
   * BG ration
   */
  private @Column @Basic @JsonProperty String bgRatio;
  /**
   * p-value
   */
  private @Column @Basic @JsonProperty double pValue;
  /**
   * p adjustment
   */
  private @Column @Basic @JsonProperty double pAdjust;
  /**
   * q-value
   */
  private @Column @Basic @JsonProperty double qValue;
  /**
   * Gene ID
   */
  private @Column @Basic @JsonProperty String geneId;
  /**
   * Count
   */
  private @Column @Basic @JsonProperty int count;
}
