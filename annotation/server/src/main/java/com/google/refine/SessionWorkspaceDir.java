package com.google.refine;

import java.io.File;
import java.util.UUID;

public class SessionWorkspaceDir extends File {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionWorkspaceDir(){
		super("/tmp/textxxx", UUID.randomUUID().toString());
		super.mkdirs();
	}
}
