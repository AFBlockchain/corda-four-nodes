package com.example;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class DockerBasedTest {
    private static final Logger logger = LoggerFactory.getLogger(DockerBasedTest.class);
    public static final String IMAGE_NAME = "corda-four-nodes";
    public static final String IMAGE_VERSION = "0.1.0";

    @ClassRule
    @SuppressWarnings("rawtypes")
    public static GenericContainer network = new GenericContainer(DockerImageName.parse(IMAGE_NAME+":"+IMAGE_VERSION))
            .withExposedPorts(10010, 10011, 10012, 10013)
            .withStartupTimeout(Duration.ofSeconds(180));

    private static String host;
    private static Integer port_n;
    private static Integer port_a;
    private static Integer port_b;
    private static Integer port_c;

    @Before
    public void setUp() {
        host = network.getHost();
        port_n = network.getMappedPort(10010);
        port_a = network.getMappedPort(10011);
        port_b = network.getMappedPort(10012);
        port_c = network.getMappedPort(10013);
    }

    @Test
    public void notaryConnectionTest() {
        String address = host + ":" + port_n;
        CordaRPCClient client = new CordaRPCClient(NetworkHostAndPort.parse(address));
        CordaRPCConnection connection = client.start("user1", "test");
        CordaRPCOps proxy = connection.getProxy();

        logger.info(proxy.nodeInfo().toString());
    }
}
