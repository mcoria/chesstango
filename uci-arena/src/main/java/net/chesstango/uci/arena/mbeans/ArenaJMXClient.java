package net.chesstango.uci.arena.mbeans;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class ArenaJMXClient {


    public static void main(String[] args) throws Exception {
        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:19999/jmxrmi");

        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        // ----------------------
        // Manage the Hello MBean
        // ----------------------

        ObjectName mbeanName = new ObjectName("net.chesstango.uci.arena:type=Arena,name=game1");

        ArenaMBean arenaProxy = JMX.newMBeanProxy(mbsc, mbeanName, ArenaMBean.class, true);

        mbsc.addNotificationListener(mbeanName, new ClientListener(), null, arenaProxy);

        printInitialStatus(arenaProxy.getGameDescription());

        Thread.sleep(Long.MAX_VALUE);

        jmxc.close();
    }

    public static class ClientListener implements NotificationListener {

        @Override
        public void handleNotification(Notification notification,
                                       Object handback) {
            try {
                if (notification instanceof MoveNotification) {
                    MoveNotification moveNotification = (MoveNotification) notification;
                    System.out.println("SequenceNumber: " + moveNotification.getSequenceNumber());
                    System.out.println("Selected move: " + moveNotification.getMove());

                    ArenaMBean arenaProxy = (ArenaMBean) handback;
                    GameDescription gameDescription = arenaProxy.getGameDescription();

                    printCurrentStatus(gameDescription);
                }
            }catch (Exception e){
                e.printStackTrace(System.err);
            }
        }

    }

    public static void printInitialStatus(GameDescription gameDescription) {
        System.out.println(String.format("White = %s vs Black = %s", gameDescription.getWhite(), gameDescription.getBlack()));
        System.out.println(String.format("Initial FEN = %s", gameDescription.getInitialFEN()));
        System.out.println(String.format("---------------------------------------------------------------------------"));
    }

    public static void printCurrentStatus(GameDescription gameDescription) {
        System.out.println(String.format("Current FEN = %s", gameDescription.getCurrentFEN()));
        System.out.println(String.format("Moves = %s", Arrays.toString(gameDescription.getMoves())));
        System.out.println(String.format("Turn = %s", gameDescription.getTurn()));
        System.out.println(String.format("---------------------------------------------------------------------------"));
    }
}
