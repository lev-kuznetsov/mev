project ('mev-common-domain', 'CRYSTAL-SNAPSHOT', script (sources = file.path ('src', 'main', 'r'),
                                                          tests = file.path ('src', 'test', 'r')));
dependency ('injectoR', '0.0.4-SNAPSHOT');
dependency ('logging', '0.7-103');
