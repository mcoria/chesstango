package net.chesstango.arenaui;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.chesstango.mbeans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
@Component
public class ArenaJMXClient implements NotificationListener, ArenaMBean {
    private JMXConnector jmxc;
    private ArenaMBean arenaProxy;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostConstruct
    public void connect() throws Exception {
        System.out.println("Connecting to JMX server");

        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:19999/jmxrmi");

        jmxc = JMXConnectorFactory.connect(url, null);

        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        ObjectName mbeanName = new ObjectName("net.chesstango.uci.arena:type=Arena,name=game1");

        arenaProxy = JMX.newMBeanProxy(mbsc, mbeanName, ArenaMBean.class, true);

        mbsc.addNotificationListener(mbeanName, this, null, arenaProxy);
    }

    @PreDestroy
    private void close() throws IOException {
        System.out.println("Disconnecting from JMX server");
        jmxc.close();
    }

    @Override
    public String getCurrentGameId() {
        return arenaProxy.getCurrentGameId();
    }

    public GameDescriptionInitial getGameDescriptionInitial(String gameId) {
        return arenaProxy.getGameDescriptionInitial(gameId);
    }

    @Override
    public GameDescriptionCurrent getGameDescriptionCurrent(String gameId) {
        return arenaProxy.getGameDescriptionCurrent(gameId);
    }


    @Override
    public void handleNotification(Notification notification,
                                   Object handback) {

        try {
            if (notification instanceof GameNotification) {
                System.out.println("--------------------------- NEW GAME --------------------------------------");

                GameNotification gameNotification = (GameNotification) notification;

                GameDescriptionInitial gameDescriptionInitial = gameNotification.getGameDescriptionInitial();

                System.out.println("SequenceNumber: " + gameNotification.getSequenceNumber());

                System.out.println("Game ID: " + gameNotification.getUserData());

                printInitialStatus(gameDescriptionInitial);

                notifyNewGame(gameNotification);
            }

            if (notification instanceof MoveNotification) {
                MoveNotification moveNotification = (MoveNotification) notification;
                GameDescriptionCurrent gameDescriptionCurrent = moveNotification.getGameDescriptionCurrent();

                System.out.println("SequenceNumber: " + moveNotification.getSequenceNumber());

                System.out.println("Selected move: " + moveNotification.getUserData());

                printCurrentStatus(gameDescriptionCurrent);

                notifyMove(moveNotification);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
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

    protected void notifyNewGame(GameNotification gameNotification) {
        simpMessagingTemplate.convertAndSend("/topic/game_messages", gameNotification);
    }

    protected void notifyMove(MoveNotification moveNotification) {
        simpMessagingTemplate.convertAndSend("/topic/move_messages", moveNotification);
    }
}
