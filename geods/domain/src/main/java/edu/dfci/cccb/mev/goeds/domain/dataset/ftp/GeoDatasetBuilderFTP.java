package edu.dfci.cccb.mev.goeds.domain.dataset.ftp;

import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Analyses;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.gzip.GzipTsvInput;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDataset;
import edu.dfci.cccb.mev.goeds.domain.contract.GeoDatasetBuilder;
import edu.dfci.cccb.mev.goeds.domain.contract.GeoSource;

@Accessors(chain=true)
public class GeoDatasetBuilderFTP extends AbstractDatasetBuilder implements GeoDatasetBuilder{

  @Setter private GeoSource geoSource;
  
  private Dimension findByType(Dimension.Type type, Dimension[] dimensions){
    for(Dimension dim : dimensions){
      if(dim.type ()==type)
        return dim;      
    }
    return null;
  }
  
  @Override
  protected Dataset aggregate (String name, Values values, Analyses analyses, Dimension... dimensions) throws DatasetBuilderException,
                                                                                                      InvalidDatasetNameException {
    Dimension column = findByType (Type.COLUMN, dimensions);    
    if(column==null)
      throw new DatasetBuilderException("Could not find COLUMN dimension in "+dimensions + " for dataset " + name);
    
    Dimension row = findByType (Type.ROW, dimensions);
    if(row==null)
      throw new DatasetBuilderException("Could not find ROW dimension in "+dimensions + " for dataset " + name);
    
    
    column.annotation (new GeoAnnotationMapBased (geoSource.getSamples ()));
    row.annotation (new GeoAnnotationStream (new GzipTsvInput(geoSource.getPlatformUrl ()), geoSource.getPlatformUrl ())); 
    return new SimpleDataset (name, values, analyses, dimensions);
  }  

}
