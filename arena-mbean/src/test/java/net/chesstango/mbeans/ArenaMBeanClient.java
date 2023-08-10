package net.chesstango.mbeans;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Mauricio Coria
 */
public class ArenaMBeanClient implements NotificationListener {

    public static void main(String[] args) throws Exception {
        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:19999/jmxrmi");

        new ArenaMBeanClient().connect(url);

    }

    private MBeanServerConnection mbsc;

    private List<ObjectName> mbeanNameList;

    private ObjectName currentMBeanName;

    private ArenaMBean arenaProxy;


    public void connect(JMXServiceURL url) throws Exception {

        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

        mbsc = jmxc.getMBeanServerConnection();

        mbeanNameList = searchArenaMBeans("net.chesstango.uci.arena:type=Arena,name=*");

        printBeanList();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean quit = false;
        do {
            String inputStr = reader.readLine();

            if ("q".equals(inputStr)) {
                quit = true;
            } else if( isNumber(inputStr) ) {
                selectMBean(Integer.valueOf(inputStr).intValue());
            } else {
                stopOutput();
                printBeanList();
            }
        } while (!quit);

        stopOutput();

        jmxc.close();
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

    private void printBeanList() {
        int i = 1;
        for (ObjectName mbeanName: mbeanNameList) {
            System.out.printf("%d - %s\n", i++, mbeanName.getCanonicalName());
        }
    }

    private void selectMBean(int idx) throws InstanceNotFoundException, IOException {
        currentMBeanName = mbeanNameList.get(idx - 1);

        arenaProxy = JMX.newMBeanProxy(mbsc, currentMBeanName, ArenaMBean.class, true);

        GameDescriptionInitial currentGame = arenaProxy.getCurrentGame();

        printInitialStatus(currentGame);

        printCurrentStatus(arenaProxy.getGameDescriptionCurrent(currentGame.getGameId()));

        mbsc.addNotificationListener(currentMBeanName, this, null, null);
    }


    private void stopOutput() throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        if(arenaProxy != null){
            System.out.println("Removing notification from " + currentMBeanName.toString());
            mbsc.removeNotificationListener(currentMBeanName, this, null, null);

            arenaProxy = null;
            currentMBeanName = null;
        }
    }

    private boolean isNumber(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
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

                //currentGame = (String) gameNotification.getUserData();

                printInitialStatus(gameDescriptionInitial);
            }

            if (notification instanceof MoveNotification) {
                MoveNotification moveNotification = (MoveNotification) notification;

                GameDescriptionCurrent gameDescriptionCurrent = moveNotification.getGameDescriptionCurrent();

                System.out.println("SequenceNumber: " + moveNotification.getSequenceNumber());

                System.out.println("Selected move: " + moveNotification.getUserData());

                printCurrentStatus(gameDescriptionCurrent);
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
}
