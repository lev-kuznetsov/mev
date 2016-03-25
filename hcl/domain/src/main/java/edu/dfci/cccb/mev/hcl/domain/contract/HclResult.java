package edu.dfci.cccb.mev.hcl.domain.contract;


public interface HclResult {
	  Node column();
	  HclResult column(Node root);
	  Node row();
	  HclResult row(Node root);
}
