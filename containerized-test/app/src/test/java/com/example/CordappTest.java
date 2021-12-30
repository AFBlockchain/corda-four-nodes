package com.example;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.transactions.SignedTransaction;
import net.corda.samples.logging.flows.YoFlow;
import net.corda.samples.logging.states.YoState;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.Utils.*;

/**
 * Test that the container can load a set of cordapp automatically with TestContainers
 */
@RunWith(JUnitParamsRunner.class)
public class CordappTest {
    private static final Logger logger = LoggerFactory.getLogger(CordappTest.class);

    @ClassRule
    @SuppressWarnings("rawtypes")
    public static GenericContainer network = new GenericContainer(DockerImageName.parse(IMAGE_NAME+":"+IMAGE_VERSION))
            .withClasspathResourceMapping("cordapps", "/nodes/Notary/cordapps", BindMode.READ_ONLY)
            .withClasspathResourceMapping("cordapps", "/nodes/PartyA/cordapps", BindMode.READ_ONLY)
            .withClasspathResourceMapping("cordapps", "/nodes/PartyB/cordapps", BindMode.READ_ONLY)
            .withClasspathResourceMapping("cordapps", "/nodes/PartyC/cordapps", BindMode.READ_ONLY)
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
    @Parameters({
            "PartyA, PartyB",
            "PartyA, PartyC",
            "PartyB, PartyC",
            "PartyB, PartyA"
    })
    public void canSendYos(String sender, String receiver) throws ExecutionException, InterruptedException {
        logger.info("Sending Yo from {} to {}", sender, receiver);

        CordaRPCOps senderProxy = getProxy(host, portMap.get(sender)); // this may be problematic since a new connection is built everytime
        CordaRPCOps receiverProxy = getProxy(host, portMap.get(receiver));
        Party senderParty = senderProxy.nodeInfo().getLegalIdentities().get(0);
        Party receiverParty = receiverProxy.nodeInfo().getLegalIdentities().get(0);

        SignedTransaction tx = senderProxy.startFlowDynamic(YoFlow.class, receiverParty).getReturnValue().get();
        YoState yo = tx.getCoreTransaction().outputsOfType(YoState.class).get(0);

        // check that the vault has this yo for both parties
        // this works for now because sender and receiver pair can identify a Yo
        // TODO: how to match transaction id?
        assert receiverProxy.vaultQuery(YoState.class).getStates().stream().anyMatch(yoStateStateAndRef -> {
            YoState fetchedYo = yoStateStateAndRef.getState().getData();
            return fetchedYo.getOrigin().equals(senderParty) &&
                    fetchedYo.getTarget().equals(receiverParty) &&
                    fetchedYo.getYo().equals(yo.getYo());
        });
    }
}
