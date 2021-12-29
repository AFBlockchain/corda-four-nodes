package com.example;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Run nodes before running this test
 */
@RunWith(JUnitParamsRunner.class)
public class ConnectionTest {
    Logger logger = LoggerFactory.getLogger(ConnectionTest.class);

    @Test
    @Parameters({"10010", "10011", "10012", "10013"})
    public void test(int port) {
        String host = "localhost";

        NetworkHostAndPort networkHostAndPort = new NetworkHostAndPort(host, port);
        CordaRPCClient client = new CordaRPCClient(networkHostAndPort);
        CordaRPCConnection connection = client.start("user1", "test");
        CordaRPCOps proxy = connection.getProxy();

        logger.info(proxy.nodeInfo().toString());
    }
}
