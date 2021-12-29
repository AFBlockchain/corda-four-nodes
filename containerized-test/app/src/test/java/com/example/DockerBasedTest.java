package com.example;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnitParamsRunner.class)
public class DockerBasedTest {
    private static final Logger logger = LoggerFactory.getLogger(DockerBasedTest.class);
    public static final String IMAGE_NAME = "corda-four-nodes";
    public static final String IMAGE_VERSION = "0.1.1";

    @ClassRule
    @SuppressWarnings("rawtypes")
    public static GenericContainer network = new GenericContainer(DockerImageName.parse(IMAGE_NAME+":"+IMAGE_VERSION))
            .withExposedPorts(10010, 10011, 10012, 10013)
            .withStartupTimeout(Duration.ofSeconds(180));

    private static String host;
    private static Map<String, Integer> portMap;

    @Before
    public void setUp() {
        host = network.getHost();
        portMap = new HashMap<>();

        portMap.put("Notary", 10010);
        portMap.put("PartyA", 10011);
        portMap.put("PartyB", 10012);
        portMap.put("PartyC", 10013);
    }

    @Test
    @Parameters({"Notary", "PartyA", "PartyB", "PartyC"})
    public void canConnectToNodes(String name) {
        logger.info("Connecting to " + name);

        CordaRPCOps proxy = getProxy(name);
        String nodeLegalName = proxy.nodeInfo().getLegalIdentities().get(0).getName().toString();
        logger.info("Legal name: " + nodeLegalName);

        assert  nodeLegalName.contains(name);
    }


    private CordaRPCOps getProxy(String name) {
        int port = portMap.get(name);
        port = network.getMappedPort(port); // ports are mapped!

        NetworkHostAndPort networkHostAndPort = new NetworkHostAndPort(host, port);
        CordaRPCClient client = new CordaRPCClient(networkHostAndPort);
        CordaRPCConnection connection = client.start("user1", "test");

        return connection.getProxy();
    }
}
