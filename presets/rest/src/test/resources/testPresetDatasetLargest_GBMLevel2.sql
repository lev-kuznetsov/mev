--DROP TABLE "GBM.AgilentG4502A_07_2.Level_2.tsv" IF EXISTS;
--CREATE TABLE "GBM.AgilentG4502A_07_2.Level_2.tsv" AS SELECT * FROM CSVREAD('/home/antony/mev/data/tcga/tcga_data/GBM/Level_2/GBM.AgilentG4502A_07_2.Level_2.tsv', null, 'charset=UTF-8 fieldSeparator='||chr(9));
CREATE TABLE IF NOT EXISTS "GBM.AgilentG4502A_07_2.Level_2.tsv" AS SELECT * FROM CSVREAD('/home/antony/mev/data/tcga/tcga_data/GBM/Level_2/GBM.AgilentG4502A_07_2.Level_2.tsv', null, 'charset=UTF-8 fieldSeparator='||chr(9));
CREATE UNIQUE INDEX ON "GBM.AgilentG4502A_07_2.Level_2.tsv"(COLUMN0);
	