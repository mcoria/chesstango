package net.chesstango.arenaui.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.chesstango.mbeans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
@Service
public class ArenaMBeanClient implements NotificationListener {
    private JMXConnector jmxc;

    private MBeanServerConnection mbsc;

    private List<ObjectName> mbeanNameList;

    private ObjectName currentMBeanName;

    private ArenaMBean arenaProxy;


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostConstruct
    public void connect() throws Exception {
        System.out.println("Connecting to JMX server");

        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:19999/jmxrmi");

        jmxc = JMXConnectorFactory.connect(url, null);

        mbsc = jmxc.getMBeanServerConnection();

        mbeanNameList = searchArenaMBeans("net.chesstango.uci.arena:type=Arena,name=*");
    }

    @PreDestroy
    private void close() throws IOException {
        System.out.println("Disconnecting from JMX server");
        jmxc.close();
    }


    public List<String> getMBeanNames(){
        return mbeanNameList.stream().map(ObjectName::getCanonicalName).collect(Collectors.toList());
    }

    public void selectProxy(String beanName){
        unselectProxy();

        Optional<ObjectName> mbeanSelectedOpt = mbeanNameList.stream().filter(mbean -> mbean.getCanonicalName().equals(beanName)).findFirst();
        if(mbeanSelectedOpt.isPresent()) {

            currentMBeanName = mbeanSelectedOpt.get();

            arenaProxy = JMX.newMBeanProxy(mbsc, currentMBeanName, ArenaMBean.class, true);

            try {
                mbsc.addNotificationListener(currentMBeanName, this, null, null);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new RuntimeException(e);
            }
        }
    }

    public GameDescriptionInitial getCurrentGame() {
        return arenaProxy.getCurrentGame();
    }

    public GameDescriptionInitial getGameDescriptionInitial(String gameId) {
        return arenaProxy.getGameDescriptionInitial(gameId);
    }


    public GameDescriptionCurrent getGameDescriptionCurrent(String gameId) {
        return arenaProxy.getGameDescriptionCurrent(gameId);
    }


    @Override
    public void handleNotification(Notification notification,
                                   Object handback) {

        try {
            if (notification instanceof GameNotification) {
                GameNotification gameNotification = (GameNotification) notification;

                //printInitialStatus(gameDescriptionInitial);

                notifyNewGame(gameNotification);
            }

            if (notification instanceof MoveNotification) {
                MoveNotification moveNotification = (MoveNotification) notification;

                //printCurrentStatus(gameDescriptionCurrent);

                notifyMove(moveNotification);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static void printInitialStatus(GameNotification gameNotification) {
        GameDescriptionInitial gameDescriptionInitial = gameNotification.getGameDescriptionInitial();
        System.out.println("SequenceNumber: " + gameNotification.getSequenceNumber());
        System.out.println("Game ID: " + gameNotification.getUserData());
        System.out.println(String.format("White = %s vs Black = %s", gameDescriptionInitial.getWhite(), gameDescriptionInitial.getBlack()));
        System.out.println(String.format("Initial FEN = %s", gameDescriptionInitial.getInitialFEN()));
        System.out.println(String.format("---------------------------------------------------------------------------"));
    }

    public static void printCurrentStatus(MoveNotification moveNotification) {
        GameDescriptionCurrent gameDescriptionCurrent = moveNotification.getGameDescriptionCurrent();
        System.out.println("SequenceNumber: " + moveNotification.getSequenceNumber());
        System.out.println("Selected move: " + moveNotification.getUserData());
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

    private List<ObjectName> searchArenaMBeans(final String objectNameStr)
    {
        final List<ObjectName> matchedMBeans = new ArrayList<>();
        try {
            ObjectName objectName = new ObjectName(objectNameStr);

            final Set<ObjectName> matchingMBeans = mbsc.queryNames(objectName, null);

            for ( final ObjectName mbeanName : matchingMBeans )
            {
                matchedMBeans.add(mbeanName);
            }

            Collections.sort(matchedMBeans, Comparator.comparing(ObjectName::getCanonicalName));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
        return matchedMBeans;
    }

    private void unselectProxy() {
        if(arenaProxy != null){
            System.out.println("Removing notification from " + currentMBeanName.toString());

            try {
                mbsc.removeNotificationListener(currentMBeanName, this, null, null);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new RuntimeException(e);
            }

            arenaProxy = null;
            currentMBeanName = null;
        }
    }
}
