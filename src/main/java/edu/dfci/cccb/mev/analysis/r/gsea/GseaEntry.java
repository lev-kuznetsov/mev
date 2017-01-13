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
package edu.dfci.cccb.mev.analysis.r.gsea;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GSEA result entry
 * 
 * @author levk
 */
@JsonInclude (NON_EMPTY)
@Entity
public class GseaEntry {
  /**
   * Identifier
   */
  private @Id @GeneratedValue (strategy = AUTO) long id;
  /**
   * Description
   */
  private @JsonProperty String description;
  /**
   * Set size
   */
  private @JsonProperty int size;
  /**
   * Entrichment score
   */
  private @JsonProperty double enrichmentScore;
  /**
   * NES
   */
  private @JsonProperty double nes;
  /**
   * p-value
   */
  private @JsonProperty double pValue;
  /**
   * p adjust
   */
  private @JsonProperty double pAdjust;
  /**
   * q-value
   */
  private @JsonProperty double qValue;
}
