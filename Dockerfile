# Set ubuntu as the base image
FROM ubuntu:20.04
WORKDIR /nodes

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean;

# Install nodes
COPY nodes/ .

# Declare mount cordapp mount points
VOLUME [ "./Notary/cordapps", "./PartyA/cordapps", "./PartyB/cordapps", "./PartyC/cordapps" ]

# Expose all RPC ports
EXPOSE 10010 10011 10012 10013
# As well as ssh ports
EXPOSE 10030 10031 10032 10033

# Run nodes
CMD ["/bin/bash", "runnodes.sh" ]

# Keep the container running
ENTRYPOINT ["tail", "-f", "/dev/null"]
