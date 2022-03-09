# Set ubuntu as the base image
FROM ubuntu:20.04
WORKDIR /nodes

# Install OpenJDK-8_321
COPY java/jdk-8u321-linux-x64.tar.gz /tmp
RUN mkdir -p /usr/lib/jvm && \
    tar zxvf /tmp/jdk-8u321-linux-x64.tar.gz -C /usr/lib/jvm && \
    apt-get update && \
    update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk1.8.0_321/bin/java" 1 && \
    update-alternatives --set java /usr/lib/jvm/jdk1.8.0_321/bin/java

# Install nodes
# This command assumes that the nodes/ directory is already populated
# By running `java -jar corda-network-bootstrapper.jar --dir=nodes`
COPY nodes/ .

# Expose all RPC and ssh ports
EXPOSE 10010-10013 10030-10033

# Run nodes as services
ENTRYPOINT ["/bin/bash", "runnodes.sh" ]
