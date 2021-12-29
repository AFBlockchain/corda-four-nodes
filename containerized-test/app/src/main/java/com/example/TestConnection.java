package com.example;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Connect to a started container.
 */
public class TestConnection {
    private static final String host = "localhost";
    private static final Map<String, Integer> portMap = new HashMap<>();
    static {
        portMap.put("Notary", 10010);
        portMap.put("PartyA", 10011);
        portMap.put("PartyB", 10012);
        portMap.put("PartyC", 10013);
    }

    private static final Logger logger = LoggerFactory.getLogger(TestConnection.class);

    public static void main(String[] args) {
        for (String name : portMap.keySet()) {
            try {
                logger.info("Connecting to " + name);

                CordaRPCOps proxy = getProxy(name);
                String nodeLegalName = proxy.nodeInfo().getLegalIdentities().get(0).getName().toString();

                logger.info("Legal name: " + nodeLegalName);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static CordaRPCOps getProxy(String name) {
        NetworkHostAndPort networkHostAndPort = new NetworkHostAndPort(host, portMap.get(name));
        CordaRPCClient client = new CordaRPCClient(networkHostAndPort);
        CordaRPCConnection connection = client.start("user1", "test");

        return connection.getProxy();
    }
}
