# Set ubuntu as the base image
FROM ubuntu:20.04
WORKDIR /nodes

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean;

# Install nodes
# This command assumes that the nodes/ directory is already populated
# By running `java -jar corda-network-bootstrapper.jar --dir=nodes`
COPY nodes/ .

# Expose all RPC and ssh ports
EXPOSE 10010-10013 10030-10033

# Run nodes
CMD ["/bin/bash", "runnodes.sh" ]

# Keep the container running
ENTRYPOINT ["tail", "-f", "/dev/null"]