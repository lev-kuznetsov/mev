--CREATE TABLE TEST AS SELECT * FROM CSVREAD('/home/antony/work/danafarber/mev/data/array_annotations/AG.na33.annot.out.tsv', null, 'charset=UTF-8 fieldSeparator='||chr(9));
--INSERT INTO TEST SELECT * FROM CSVREAD('/home/antony/work/danafarber/mev/data/array_annotations/ATH1-121501.na33.annot.out.tsv', null, 'charset=UTF-8 fieldSeparator='||chr(9));


CREATE CACHED TABLE IF NOT EXISTS PUBLIC.MEV_PROBE_ANNOTATIONS(
    CHIP_VENDOR VARCHAR,
    CHIP_TYPE VARCHAR,
    CREATE_DATE VARCHAR,
    genome_species VARCHAR,
    genome_version VARCHAR,
    netaffx_annotation_netaffx_build VARCHAR,
    PROBESET_ID VARCHAR PRIMARY KEY,
    GENE_SYMBOL VARCHAR,
    GENE_DESC VARCHAR,
    CHR_LOCATION VARCHAR,
    STRAND VARCHAR,
    REFSEQ_ACCN VARCHAR
);
CREATE UNIQUE INDEX IF NOT EXISTS MEV_PROBE_ANNOTATIONS_PK ON PUBLIC.MEV_PROBE_ANNOTATIONS(PROBESET_ID);

