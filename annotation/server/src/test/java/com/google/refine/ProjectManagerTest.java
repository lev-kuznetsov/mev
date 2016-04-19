package com.google.refine;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

import org.junit.Test;

public class ProjectManagerTest {
	String getRootProjectName(String name){
		if(name.contains("--")){
			String type = null;
			if(name.endsWith("column"))
				type = "column";
			else if(name.endsWith("row"))
				type = "row";
			
			String subset = name.substring(name.indexOf("--"), name.length()-type.length());
			name = name.replace(subset, "");
		}
		return name;
	}
	@Test
	public void testColumn() {
		String name = getRootProjectName("mouse_test_data.tsv--s1column");
		assertThat(name, is("mouse_test_data.tsvcolumn"));		
	}
	@Test
	public void testColumnNoop() {
		String name = getRootProjectName("mouse_test_data.tsvcolumn");
		assertThat(name, is("mouse_test_data.tsvcolumn"));		
	}
	@Test
	public void testRow() {
		String name = getRootProjectName("mouse_test_data.tsv--s1row");
		assertThat(name, is("mouse_test_data.tsvrow"));		
	}
	@Test
	public void testRowNoop() {
		String name = getRootProjectName("mouse_test_data.tsvrow");
		assertThat(name, is("mouse_test_data.tsvrow"));		
	}

}
