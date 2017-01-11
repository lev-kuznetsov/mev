# The MIT License (MIT)
# Copyright (c) 2017 Dana-Farber Cancer Institute
#
# Permission is hereby granted, free of charge, to any person
# obtaining a copy of this software and associated documentation
# files (the "Software"), to deal in the Software without
# restriction, including without limitation the rights to use,
# copy, modify, merge, publish, distribute, sublicense, and/or
# sell copies of the Software, and to permit persons to whom the
# Software is furnished to do so, subject to the following
# conditions:
#
# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
# OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
# HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
# WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
# FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
# OTHER DEALINGS IN THE SOFTWARE.

# This is an iterative image, do not add stuff to this definition,
# delete all content after this comment and start a new with
# FROM docker.io/cccb/mev-rserve:<current>

FROM debian:testing

RUN apt-get update \
  && apt-get install -y --no-install-recommends locales wget ca-certificates supervisor \
  && rm -rf /var/lib/apt/lists/*

RUN echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen \
  && locale-gen en_US.utf8 \
  && /usr/sbin/update-locale LANG=en_US.UTF-8

ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8

RUN apt-get update \
  && apt-get install -y --no-install-recommends \
    build-essential autoconf r-base r-base-dev r-recommended \
  && echo 'options (repos = "http://cran.rstudio.com/", download.file.method = "libcurl")' >> /etc/R/Rprofile.site \
  && echo "source ('http://bioconductor.org/biocLite.R');" >> /tmp/install.R \
  && echo "biocLite (c ('topGO', 'org.Hs.eg.db', 'org.Mm.eg.db', 'limma', 'DESeq', 'edgeR', 'ReactomePA', 'metagenomeSeq', 'DESeq2', 'impute', 'preprocessCore'));" >> /tmp/install.R \
  && echo "install.packages (c ('Rserve', 'rafalib', 'amap'));" >> /tmp/install.R \
  && Rscript /tmp/install.R \
  && echo "fileio enable" >> /etc/Rserve.conf \
  && echo "workdir.clean enable" >> /etc/Rserve.conf \
  && echo "log.io enable" >> /etc/Rserve.conf \
  && echo "daemon disable" >> /etc/Rserve.conf \
  && echo "remote enable" >> /etc/Rserve.conf \
  && echo "[supervisord]" >> /etc/supervisord.conf \
  && echo "nodaemon=true" >> /etc/supervisord.conf \
  && echo "user=root" >> /etc/supervisord.conf \
  && echo "logfile=/var/log/supervisord.log" >> /etc/supervisord.conf \
  && echo "pidfile=/var/run/supervisord.pid" >> /etc/supervisord.conf \
  && echo "logfile_maxbytes=50MB" >> /etc/supervisord.conf \
  && echo "logfile_backups=10" >> /etc/supervisord.conf \
  && echo "" >> /etc/supervisord.conf \
  && echo "[program:rserve]" >> /etc/supervisord.conf \
  && echo "command=R -e \"Rserve::Rserve(debug=TRUE,args='--no-save')\"" >> /etc/supervisord.conf \
  && echo "stdout_logfile = /var/log/rserve.log" >> /etc/supervisord.conf \
  && echo "" >> /etc/supervisord.conf \
  && rm -rf /tmp/* /var/lib/apt/lists/*

ADD supervisord.conf /etc/supervisord.conf

EXPOSE 6311

CMD [ "/usr/bin/supervisord", "-c", "/etc/supervisord.conf" ]
