project ('mev-webapp', 'CRYSTAL-SNAPSHOT', script (sources = file.path ('src', 'main', 'r'),
                                                   tests = file.path ('src', 'main', 'r')));
dependency ('mev-common-domain', 'CRYSTAL-SNAPSHOT');