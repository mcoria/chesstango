package net.chesstango.uci.arena.mbeans;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * @author Mauricio Coria
 */
public class ArenaJMXClient {
    /**
     * Inner class that will handle the notifications.
     */
    public static class ClientListener implements NotificationListener {
        public void handleNotification(Notification notification,
                                       Object handback) {
            /*
            echo("\nReceived notification:");
            echo("\tClassName: " + notification.getClass().getName());
            echo("\tSource: " + notification.getSource());
            echo("\tType: " + notification.getType());
            echo("\tMessage: " + notification.getMessage());
            if (notification instanceof AttributeChangeNotification) {
                AttributeChangeNotification acn =
                        (AttributeChangeNotification) notification;
                echo("\tAttributeName: " + acn.getAttributeName());
                echo("\tAttributeType: " + acn.getAttributeType());
                echo("\tNewValue: " + acn.getNewValue());
                echo("\tOldValue: " + acn.getOldValue());
            }
             */
        }


    }


    public static void main(String[] args) throws Exception {
        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:19999/jmxrmi");

        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        // ----------------------
        // Manage the Hello MBean
        // ----------------------


        // Construct the ObjectName for the Hello MBean
        //
        ObjectName mbeanName = new ObjectName("net.chesstango.uci.arena:type=Arena");

        // Create a dedicated proxy for the MBean instead of
        // going directly through the MBean server connection
        //
        ArenaMBean arenaProxy = JMX.newMBeanProxy(mbsc, mbeanName, ArenaMBean.class, true);

        // Add notification listener on Hello MBean
        //

        //mbsc.addNotificationListener(mbeanName, listener, null, null);

        printStatus(arenaProxy);

        Thread.sleep(Long.MAX_VALUE);

        jmxc.close();
    }

    private static void printStatus(ArenaMBean arenaProxy) {
        System.out.println(String.format("White = %s vs Black = %s", arenaProxy.getWhite(), arenaProxy.getBlack()));
        System.out.println(String.format("FEN = %s", arenaProxy.getFEN()));
        System.out.println(String.format("Moves = %s", arenaProxy.getMoveList()));
        System.out.println(String.format("Turn = %s", arenaProxy.getTurn()));
        System.out.println(String.format("---------------------------------------------------------------------------"));
    }
}
