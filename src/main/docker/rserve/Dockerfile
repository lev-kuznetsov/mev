FROM debian:testing

RUN apt-get update \
  && apt-get install -y --no-install-recommends locales wget ca-certificates supervisor \
  && rm -rf /var/lib/apt/lists/*

RUN echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen \
  && locale-gen en_US.utf8 \
  && /usr/sbin/update-locale LANG=en_US.UTF-8

ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8

ADD install.R /tmp/install.R

RUN apt-get update \
  && apt-get install -y --no-install-recommends \
    build-essential autoconf r-base r-base-dev r-recommended \
  && echo 'options (repos = "http://cran.rstudio.com/", download.file.method = "libcurl")' >> /etc/R/Rprofile.site \
  && Rscript /tmp/install.R \
  && rm -rf /tmp/* /var/lib/apt/lists/*

ADD Rserv.conf /etc/Rserv.conf
ADD supervisord.conf /etc/supervisord.conf

EXPOSE 6311

CMD [ "/usr/bin/supervisord", "-c", "/etc/supervisord.conf" ]
