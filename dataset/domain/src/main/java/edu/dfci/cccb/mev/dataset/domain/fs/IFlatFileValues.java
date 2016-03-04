package edu.dfci.cccb.mev.dataset.domain.fs;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface IFlatFileValues {

	public abstract boolean skipJson();

	public abstract InputStream asInputStream() throws FileNotFoundException;

}