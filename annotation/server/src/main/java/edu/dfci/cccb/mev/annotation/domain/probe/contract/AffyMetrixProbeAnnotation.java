package edu.dfci.cccb.mev.annotation.domain.probe.contract;

public interface AffyMetrixProbeAnnotation extends ProbeAnnotation{

  public abstract String chipVendor ();

  public abstract String chipType ();

  public abstract String createDate ();

  public abstract String genomeSpecies ();

  public abstract String genomeVersion ();

  public abstract String netaffxAnnotationNetaffxBuild ();

  public abstract String geneSymbol ();

  public abstract String geneDesc ();

  public abstract String chrLocation ();

  public abstract String strand ();

  public abstract String refseqAccn ();

  public abstract String goBioProcess ();

  public abstract String goCellularComponent ();

  public abstract String goMolecularFunction ();

  public abstract String pathway ();

}