#
# Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
# All rights reserved.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

FROM firethorn/java:2.1.36
MAINTAINER Dave Morris <docker-admin@metagrid.co.uk>

#
# Create our cache directory.
RUN mkdir -p /var/local/cache
VOLUME /var/local/cache

#
# Create our build directory.
RUN mkdir -p /var/local/build
VOLUME /var/local/build

#
# Install admin tools.
RUN dnf -y install rsync
RUN dnf -y install iputils
RUN dnf -y install bind-utils

#
# Install Mercurial and Git
RUN dnf -y install git
RUN dnf -y install mercurial

#
# Update SELinux library.
# https://bugzilla.redhat.com/show_bug.cgi?id=1596409 
# https://bugzilla.redhat.com/show_bug.cgi?id=1595316
RUN dnf -y update libsemanage

#
# Install Docker CE from the Docker repository.
RUN dnf -y install dnf-plugins-core
RUN dnf -y config-manager \
    --add-repo \
    https://download.docker.com/linux/fedora/docker-ce.repo
RUN dnf -y makecache
RUN dnf -y install docker-ce

#
# Install Docker compose from GitHub.
RUN curl -L -s https://github.com/docker/compose/releases/download/1.21.2/docker-compose-$(uname -s)-$(uname -m) -o /usr/bin/docker-compose
RUN chmod +x /usr/bin/docker-compose

#
# Install Maven
RUN dnf -y install maven
COPY maven-cache.sh /tmp 
RUN chmod a+x /tmp/maven-cache.sh
RUN /tmp/maven-cache.sh
RUN rm /tmp/maven-cache.sh

#
# Install our build scripts
RUN mkdir -p  /builder
COPY bin/     /builder/bin 
RUN chmod a+x /builder/bin/*


