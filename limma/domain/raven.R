project ('mev-limma-domain', 'CRYSTAL-SNAPSHOT', script (sources = file.path ('src', 'main', 'r'),
                                                         tests = file.path ('src', 'test', 'r')));
dependency ('mev-common-domain', 'CRYSTAL-SNAPSHOT');
dependency ('limma', '3.22.1');
