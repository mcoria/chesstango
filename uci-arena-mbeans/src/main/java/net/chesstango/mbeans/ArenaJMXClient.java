package net.chesstango.mbeans;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class ArenaJMXClient {
    private String currentGame;

    public static void main(String[] args) throws Exception {
        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:19999/jmxrmi");

        new ArenaJMXClient().connect(url);

    }

    public void connect(JMXServiceURL url) throws Exception  {
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        ObjectName mbeanName = new ObjectName("net.chesstango.uci.arena:type=Arena,name=game1");

        ArenaMBean arenaProxy = JMX.newMBeanProxy(mbsc, mbeanName, ArenaMBean.class, true);

        mbsc.addNotificationListener(mbeanName, new ClientListener(), null, arenaProxy);

        currentGame = arenaProxy.getCurrentGameId();

        printInitialStatus(arenaProxy.getGameDescriptionInitial(currentGame));

        Thread.sleep(Long.MAX_VALUE);

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

                    if(!gameDescriptionCurrent.getGameId().equals(currentGame)){
                        System.out.println("--------------------------- NEW GAME ---------------------------");

                        ArenaMBean arenaProxy = (ArenaMBean) handback;

                        currentGame = gameDescriptionCurrent.getGameId();

                        printInitialStatus(arenaProxy.getGameDescriptionInitial(currentGame));
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
