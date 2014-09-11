package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

public interface TypeCopier {

  public abstract void process (String sourceIndexName, String sourceDocumentType, String targetIndexName);

}