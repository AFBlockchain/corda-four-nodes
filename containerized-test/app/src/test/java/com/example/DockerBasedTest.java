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

import static com.example.Utils.*;

@RunWith(JUnitParamsRunner.class)
public class DockerBasedTest {
    private static final Logger logger = LoggerFactory.getLogger(DockerBasedTest.class);

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

        portMap.put("Notary", network.getMappedPort(10010));
        portMap.put("PartyA", network.getMappedPort(10011));
        portMap.put("PartyB", network.getMappedPort(10012));
        portMap.put("PartyC", network.getMappedPort(10013));
    }

    @Test
    @Parameters({"Notary", "PartyA", "PartyB", "PartyC"})
    public void canConnectToNodes(String name) {
        logger.info("Connecting to " + name);

        CordaRPCOps proxy = getProxy(host, portMap.get(name));
        String nodeLegalName = proxy.nodeInfo().getLegalIdentities().get(0).getName().toString();
        logger.info("Legal name: " + nodeLegalName);

        assert  nodeLegalName.contains(name);
    }
}
