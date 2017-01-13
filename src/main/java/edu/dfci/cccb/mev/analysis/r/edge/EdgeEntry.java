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
package edu.dfci.cccb.mev.analysis.r.edge;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Basic;
import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Edge result entry
 * 
 * @author levk
 */
@JsonInclude (NON_EMPTY)
@Entity
public class EdgeEntry {

  /**
   * Identifier
   */
  private @Id @GeneratedValue (strategy = AUTO) long id;

  /**
   * Log FC
   */
  private @JsonProperty @Column @Basic double logFc;
  /**
   * Log CPM
   */
  private @JsonProperty @Column @Basic double logCpm;
  /**
   * p-value
   */
  private @JsonProperty @Column @Basic double pValue;
  /**
   * FWER
   */
  private @JsonProperty @Column @Basic double fwer;
  /**
   * FDR
   */
  private @JsonProperty @Column @Basic double fdr;
}
