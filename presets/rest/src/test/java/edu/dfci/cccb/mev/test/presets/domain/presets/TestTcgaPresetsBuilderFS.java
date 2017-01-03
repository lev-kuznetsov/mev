package edu.dfci.cccb.mev.test.presets.domain.presets;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetNotFoundException;
import edu.dfci.cccb.mev.presets.dataset.fs.PresetDescriptorFlatFileAdaptor;
import edu.dfci.cccb.mev.presets.simple.SimplePresests;
import edu.dfci.cccb.mev.presets.tcga.TcgaPresetsBuilderFS;
import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;
import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.inject.Named;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by antony on 12/15/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={PresetsRestConfigurationTest.class})
@Log4j
public class TestTcgaPresetsBuilderFS {
    @Inject @Named("tcgaPresetBiulderFS") TcgaPresetsBuilderFS builder;
    @Inject @Named("tcgaPresetRoot") URL tcgaPresetRoot;
    @Inject @Named ("probe-annotations-root") URL rowAnnotationsRoot;
    Presets tcgaPresets;
    URL metadataURL;

    @Before
    public void setup() throws MalformedURLException, PresetException {
        metadataURL = new URL(tcgaPresetRoot, "tcga_cleaned_datasets/", null);
        tcgaPresets = new SimplePresests(metadataURL, builder);
    }

    @Test
    public void testGetTcgaPresets () throws MalformedURLException, PresetException {

        assertNotNull (tcgaPresets);
        assertNotNull( tcgaPresets.list ());
        assertEquals(6, tcgaPresets.list ().size ());

    }

    @Test
    public void testGetDataFile () throws PresetNotFoundException, PresetException {
        Preset preset = tcgaPresets.get ("20150821-COAD-RNAseqGene-median_length_normalized.txt");
        assertNotNull (preset);

        PresetDescriptor descriptor = preset.descriptor ();
        File checkFile = new File( descriptor.dataUrl ().getFile() );
        assertTrue(checkFile.exists ());
    }

//    @Test
//    public void testGetColumnFile () throws PresetNotFoundException, PresetException {
//        Preset preset = tcgaPresets.get ("20150821-COAD-RNAseqGene-median_length_normalized.txt");
//        assertNotNull (preset);
//
//        PresetDescriptor descriptor = preset.descriptor ();
//        File checkFile = new File( descriptor.columnUrl ().getFile() );
//        assertTrue(checkFile.exists ());
//    }

    @Test
    public void testGetDescriptor () throws PresetException, MalformedURLException, PresetNotFoundException {

        Preset preset = tcgaPresets.get ("20150821-COAD-RNAseqGene-median_length_normalized.txt");
        assertNotNull (preset);
        PresetDescriptor descriptor = preset.descriptor ();

        URL expectedDataURL = new URL(tcgaPresetRoot, "tcga_cleaned_datasets/COAD/20150821-COAD-RNAseqGene-median_length_normalized.txt");
        assertEquals (expectedDataURL, descriptor.dataUrl ());
        File checkFile = new File( descriptor.dataUrl ().getFile() );
        assertTrue(checkFile.exists ());

        URL expectedColumnURL = new URL(tcgaPresetRoot, "openrefine/clinical/tcga_cleaned_datasets/COAD-clinical_annotations-tsv.openrefine.tar.gz");
        assertEquals (expectedColumnURL, descriptor.columnUrl ());

        URL expectedRowURL = new URL(rowAnnotationsRoot, "openrefine/geneSymbol_goAnnotations-tsv.google-refine.tar.gz");
        assertEquals (expectedRowURL, descriptor.rowUrl ());

    }

    @Test
    public void testGetBinaryDescriptor () throws PresetException, MalformedURLException, PresetNotFoundException {

        Preset preset = tcgaPresets.get ("20150821-COAD-RNAseqGene-median_length_normalized.txt");
        assertNotNull (preset);
        PresetDescriptorFlatFileAdaptor descriptor = new PresetDescriptorFlatFileAdaptor(preset.descriptor ());

        URL expectedColumnSourceUrl = new URL(tcgaPresetRoot, "tcga_cleaned_datasets/COAD/20150821-COAD-RNAseqGene-Clinical.txt");
        assertEquals (expectedColumnSourceUrl, descriptor.columnSourceUrl());
        File checkFile = new File( descriptor.dataUrl ().getFile() );
        assertTrue(checkFile.exists ());

        URL expectedBinaryURL = new URL(tcgaPresetRoot, "binary/tcga_cleaned_datasets/COAD/20150821-COAD-RNAseqGene-median_length_normalized.txt/datasetValues.matrix");
        assertEquals (expectedBinaryURL, descriptor.binaryUrl());

        URL expectedRowListUrl = new URL(tcgaPresetRoot, "binary/tcga_cleaned_datasets/COAD/20150821-COAD-RNAseqGene-median_length_normalized.txt/rows.tsv");
        assertEquals (expectedRowListUrl, descriptor.rowListUrl ());

        URL expectedColumnURL = new URL(tcgaPresetRoot, "binary/tcga_cleaned_datasets/COAD/20150821-COAD-RNAseqGene-median_length_normalized.txt/columns.tsv");
        assertEquals (expectedColumnURL, descriptor.columnListUrl ());

    }
}
