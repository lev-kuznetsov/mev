project ('mev-hclust-domain', 'CRYSTAL-SNAPSHOT', script (sources = file.path ('src', 'main', 'r'),
                                                          tests = file.path ('src', 'test', 'r')));
dependency ('rpud', '0.0.2', FALSE);
dependency ('mev-common-domain', 'CRYSTAL-SNAPSHOT');
dependency ('ctc', '1.40.0');