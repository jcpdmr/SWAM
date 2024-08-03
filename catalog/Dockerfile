FROM openjdk:22-jdk

# Install wget
RUN microdnf install -y wget nc && microdnf clean all

# Add EPEL repository manually
RUN wget https://dl.fedoraproject.org/pub/epel/epel-release-latest-9.noarch.rpm && \
    rpm -ivh epel-release-latest-9.noarch.rpm && \
    rm epel-release-latest-9.noarch.rpm

# Install inotify-tools
RUN microdnf install -y inotify-tools && microdnf clean all

ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME