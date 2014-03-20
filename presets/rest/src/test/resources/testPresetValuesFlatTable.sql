DROP TABLE TEST_PRESET_VALUES_FLAT_TABLE IF EXISTS;
CREATE TEMPORARY TABLE TEST_PRESET_VALUES_FLAT_TABLE(COLUMN0 VARCHAR(255), SAMPLE1 VARCHAR, Sample2 VARCHAR, SAMPLE3 VARCHAR);
insert into test_preset_values_flat_table values('g1', '0.1', '0.2', '0.3');
insert into test_preset_values_flat_table values('g2', '0.21', '0.22', '0.23');
insert into test_preset_values_flat_table values('g3', '0.31', '0.32', '0.33');
insert into test_preset_values_flat_table values('g4', '0.41', '0.42', '0.43');