package com.example;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Run nodes before running this test
 */
public class ConnectionTest {
    Logger logger = LoggerFactory.getLogger(ConnectionTest.class);

    @Test
    public void test() {
        String host = "localhost";
        int port = 10011;

        NetworkHostAndPort networkHostAndPort = new NetworkHostAndPort(host, port);
        CordaRPCClient client = new CordaRPCClient(networkHostAndPort);
        CordaRPCConnection connection = client.start("user1", "test");
        CordaRPCOps proxy = connection.getProxy();

        logger.info(proxy.nodeInfo().toString());
    }
}
