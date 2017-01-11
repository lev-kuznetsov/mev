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

# FIXME
# The product of this should be tagged docker.io/cccb/mev-web:`date +'%Y-%m-%d`
# the image updated in src/main/kubernetes/web-worker, image pushed
# to dockerhub, docker stuff can be done by the maven plugin

FROM jboss/wildfly

# TODO
# Eventually add custom configuration here
#ADD standalone.xml /opt/wildfly/standalone/configuration/

# FIXME
# Copy the war from target/; I should really use the docker maven
# plugin which will copy whatever is in this folder to some place
# in target/ then build and push the image from there
ADD mev.war /opt/jboss/wildfly/standalone/deployments/
