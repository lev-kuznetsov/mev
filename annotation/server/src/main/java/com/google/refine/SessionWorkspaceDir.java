package com.google.refine;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import edu.dfci.cccb.mev.io.implementation.TemporaryFolder;


public class SessionWorkspaceDir extends TemporaryFolder {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionWorkspaceDir() throws IOException{
	  super("openrefine-","");
//		super("/tmp/textxxx", UUID.randomUUID().toString());
//		super.mkdirs();
		
	}
}
