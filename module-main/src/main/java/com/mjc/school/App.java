package com.mjc.school;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);
        server.start();
    }
}
