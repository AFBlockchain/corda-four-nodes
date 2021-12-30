package com.example;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;

public class Utils {
    public static final String IMAGE_NAME = "corda-four-nodes";
    public static final String IMAGE_VERSION = "0.1.1";

    public static CordaRPCOps getProxy(String host, int port) {
        NetworkHostAndPort networkHostAndPort = new NetworkHostAndPort(host, port);
        CordaRPCClient client = new CordaRPCClient(networkHostAndPort);
        CordaRPCConnection connection = client.start("user1", "test");

        return connection.getProxy();
    }
}
