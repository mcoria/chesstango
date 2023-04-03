package net.chesstango.arenaui;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Arrays;

import net.chesstango.mbeans.*;

/**
 * @author Mauricio Coria
 */
public class ArenaJMXClient {
    private String currentGameId;

    private JMXConnector jmxc;

    public static void main(String[] args) throws Exception {
        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:19999/jmxrmi");


        ArenaJMXClient client = new ArenaJMXClient();

        client.connect("service:jmx:rmi:///jndi/rmi://:19999/jmxrmi");

        Thread.sleep(Long.MAX_VALUE);

        client.close();

    }

    public void connect(String serviceUrl) throws Exception  {
        JMXServiceURL url =
                new JMXServiceURL(serviceUrl);

        jmxc = JMXConnectorFactory.connect(url, null);

        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        ObjectName mbeanName = new ObjectName("net.chesstango.uci.arena:type=Arena,name=game1");

        ArenaMBean arenaProxy = JMX.newMBeanProxy(mbsc, mbeanName, ArenaMBean.class, true);

        mbsc.addNotificationListener(mbeanName, new ClientListener(), null, arenaProxy);

        currentGameId = arenaProxy.getCurrentGameId();

        printInitialStatus(arenaProxy.getGameDescriptionInitial(currentGameId));
    }

    private void close() throws IOException {
        jmxc.close();
    }

    public class ClientListener implements NotificationListener {

        @Override
        public void handleNotification(Notification notification,
                                       Object handback) {
            try {
                if (notification instanceof MoveNotification) {
                    MoveNotification moveNotification = (MoveNotification) notification;
                    GameDescriptionCurrent gameDescriptionCurrent = moveNotification.getGameDescriptionCurrent();

                    if(!gameDescriptionCurrent.getGameId().equals(currentGameId)){
                        System.out.println("--------------------------- NEW GAME ---------------------------");

                        ArenaMBean arenaProxy = (ArenaMBean) handback;

                        currentGameId = gameDescriptionCurrent.getGameId();

                        printInitialStatus(arenaProxy.getGameDescriptionInitial(currentGameId));
                    }

                    System.out.println("SequenceNumber: " + moveNotification.getSequenceNumber());

                    System.out.println("Selected move: " + moveNotification.getMove());

                    printCurrentStatus(gameDescriptionCurrent);
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

    }

    public static void printInitialStatus(GameDescriptionInitial gameDescriptionInitial) {
        System.out.println(String.format("White = %s vs Black = %s", gameDescriptionInitial.getWhite(), gameDescriptionInitial.getBlack()));
        System.out.println(String.format("Initial FEN = %s", gameDescriptionInitial.getInitialFEN()));
        System.out.println(String.format("---------------------------------------------------------------------------"));
    }

    public static void printCurrentStatus(GameDescriptionCurrent gameDescriptionCurrent) {
        System.out.println(String.format("Current FEN = %s", gameDescriptionCurrent.getCurrentFEN()));
        System.out.println(String.format("Moves = %s", Arrays.toString(gameDescriptionCurrent.getMoves())));
        System.out.println(String.format("Turn = %s", gameDescriptionCurrent.getTurn()));
        System.out.println(String.format("---------------------------------------------------------------------------"));
    }
}
