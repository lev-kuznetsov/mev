package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

public interface TypeCopier {

  public abstract void process (String sourceIndexName, String sourceDocumentType, String targetIndexName);

}