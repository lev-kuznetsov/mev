--DROP TABLE "KIRP.AgilentG4502A_07_3.Level_3.tsv" IF EXISTS;
--CREATE TABLE "KIRP.AgilentG4502A_07_3.Level_3.tsv" AS SELECT * FROM CSVREAD('~/mev/data/tcga/tcga_data/KIRP/Level_3/KIRP.AgilentG4502A_07_3.Level_3.tsv', null, 'charset=UTF-8 fieldSeparator='||chr(9));
CREATE TABLE IF NOT EXISTS "KIRP.AgilentG4502A_07_3.Level_3.tsv" AS SELECT * FROM CSVREAD('~/mev/data/tcga/tcga_data/KIRP/Level_3/KIRP.AgilentG4502A_07_3.Level_3.tsv', null, 'charset=UTF-8 fieldSeparator='||chr(9));
CREATE UNIQUE INDEX ON "KIRP.AgilentG4502A_07_3.Level_3.tsv"(COLUMN0);