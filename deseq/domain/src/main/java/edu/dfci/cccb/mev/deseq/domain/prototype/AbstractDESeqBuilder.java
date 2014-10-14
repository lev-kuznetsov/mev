/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.deseq.domain.prototype;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.script.ScriptEngine;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.ComposerFactory;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysisBuilder;
import edu.dfci.cccb.mev.deseq.domain.contract.DESeq;
import edu.dfci.cccb.mev.deseq.domain.contract.DESeqBuilder;
import edu.dfci.cccb.mev.deseq.domain.contract.DESeq.Species;

/**
 * @author levk
 * 
 */
@ToString
@Accessors (fluent = true, chain = true)
public abstract class AbstractDESeqBuilder extends AbstractAnalysisBuilder<DESeqBuilder, DESeq> implements DESeqBuilder {

  private @Getter @Setter Selection control;
  private @Getter @Setter Selection experiment;
  private @Getter @Setter @Resource (name = "R") ScriptEngine r;
  private @Getter @Setter @Inject ComposerFactory composerFactory;

  /**
   */
  public AbstractDESeqBuilder () {
    super ("DESeq Differential Expression Analysis");
  }
  
  //some static strings for use in the child classes
  public static final String DATASET_FILENAME = "dataset.tsv";
  public static final String ANNOTATION_FILENAME = "annotations.tsv";
  public static final String CONTRAST_FLAG = "_VS_"; //for naming the output result file
  public static final String NORMALIZED_COUNT_FILE_TAG = "normalized_counts"; //for naming the output result file 

}
