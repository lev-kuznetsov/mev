package edu.dfci.cccb.mev.annotation.domain.probe.simple;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotation;

@RequiredArgsConstructor
@Accessors(fluent=true)
public class SimpleProbeAnnotation implements ProbeAnnotation {
  @Getter private String chipVendor;
  @Getter private String chipType;
  @Getter private String createDate;
  @Getter private String genomeSpecies;
  @Getter private String genomeVersion;
  @Getter private String netaffxAnnotationNetaffxBuild;
  @Getter private String probesetId;
  @Getter private String geneSymbol;
  @Getter private String geneDesc;
  @Getter private String chrLocation;
  @Getter private String strand;
  @Getter private String refseqAccn;
  @Getter private String goBioProcess;
  @Getter private String goCellularComponent;
  @Getter private String goMolecularFunction;
  @Getter private String pathway;
  
  @Override
  public String toString(){
    return chipVendor+"\t"+chipType+"\t"+createDate+"\t"+genomeSpecies+"\t"+genomeVersion+"\t"+netaffxAnnotationNetaffxBuild+"\t"+probesetId+"\t"+geneSymbol+"\t"+geneDesc+"\t"+chrLocation+"\t"+strand+"\t"+refseqAccn+"\t"+goBioProcess+"\t"
            +goCellularComponent+"\t"+goMolecularFunction+"\t"+pathway;
  }
}
