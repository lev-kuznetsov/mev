project ('mev-cluster-domain', 'CRYSTAL-SNAPSHOT', script (sources = file.path ('src', 'main', 'r'),
                                                           tests = file.path ('src', 'test', 'r')));
dependency ('mev-common-domain', 'CRYSTAL-SNAPSHOT');
dependency ('ctc', '1.40.0');
# Requires CUDA toolkit, tested on version 6.5
dependency ('gputools', '0.28.0.9000', required = FALSE);