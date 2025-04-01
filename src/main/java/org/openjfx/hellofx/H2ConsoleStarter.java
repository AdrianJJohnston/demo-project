package org.openjfx.hellofx;

import org.h2.tools.Server;

import java.sql.SQLException;

public class H2ConsoleStarter {
    public static void main(String[] args) {
        try {
            // Start the H2 web console
            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Console started at: http://localhost:8082");

            // Start TCP Server (Optional)
            Server tcpServer = Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "9092").start();
            System.out.println("H2 TCP Server started at port 9092");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
