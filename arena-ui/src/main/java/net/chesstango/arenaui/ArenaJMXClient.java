package net.chesstango.arenaui;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.chesstango.mbeans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Mauricio Coria
 */
@Component
public class ArenaJMXClient {
    private JMXConnector jmxc;
    private ArenaMBean arenaProxy;
    private String currentGameId;
    private GameDescriptionInitial gameDescriptionInitial;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostConstruct
    public void connect() throws Exception  {
        System.out.println("Connecting to JMX server");

        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:19999/jmxrmi");

        jmxc = JMXConnectorFactory.connect(url, null);

        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        ObjectName mbeanName = new ObjectName("net.chesstango.uci.arena:type=Arena,name=game1");

        arenaProxy = JMX.newMBeanProxy(mbsc, mbeanName, ArenaMBean.class, true);

        currentGameId = arenaProxy.getCurrentGameId();

        gameDescriptionInitial = arenaProxy.getGameDescriptionInitial(currentGameId);

        //printInitialStatus(gameDescriptionInitial);

        mbsc.addNotificationListener(mbeanName, new ClientListener(), null, arenaProxy);
    }

    @PreDestroy
    private void close() throws IOException {
        System.out.println("Disconnecting from JMX server");
        jmxc.close();
    }


    public GameDescriptionInitial getGameDescription(String gameId){
        if(gameId == null){
            return gameDescriptionInitial;
        }
        return arenaProxy.getGameDescriptionInitial(currentGameId);
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

                        //printInitialStatus(arenaProxy.getGameDescriptionInitial(currentGameId));
                    }

                    System.out.println("SequenceNumber: " + moveNotification.getSequenceNumber());

                    System.out.println("Selected move: " + moveNotification.getMove());

                    //printCurrentStatus(gameDescriptionCurrent);

                    simpMessagingTemplate.convertAndSend("/topic/move_messages", moveNotification);
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
