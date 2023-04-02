package net.chesstango.mbeans;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mauricio Coria
 */
public class Arena extends NotificationBroadcasterSupport implements ArenaMBean {
    private AtomicLong sequenceNumber = new AtomicLong();

    private String currentGameId;

    private Map<String, GameDescriptionInitial> initialMap = new HashMap<>();

    private Map<String, GameDescriptionCurrent> currentMap = new HashMap<>();

    @Override
    public String getCurrentGameId() {
        return currentGameId;
    }

    @Override
    public GameDescriptionInitial getGameDescriptionInitial(String id) {
        return initialMap.get(id);
    }

    @Override
    public GameDescriptionCurrent getGameDescriptionCurrent(String id) {
        return currentMap.get(id);
    }


    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{MoveNotification.ATTRIBUTE_CHANGE};
        String name = MoveNotification.class.getName();
        String description = "A move has been selected";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);

        return new MBeanNotificationInfo[]{info};
    }


    public void notifyMove(String move, GameDescriptionCurrent gameDescriptionCurrent) {
        Notification notification =
                new MoveNotification(this,
                        sequenceNumber.getAndIncrement(),
                        System.currentTimeMillis(),
                        "Move",
                        move,
                        gameDescriptionCurrent);

        sendNotification(notification);
    }

    public void putNewGame(String gameId, GameDescriptionInitial gameDescriptionInitial) {
        initialMap.put(gameId, gameDescriptionInitial);
    }

    public void updateDescriptionCurrent(String gameId, GameDescriptionCurrent gameDescriptionCurrent) {
        currentGameId = gameId;
        currentMap.put(gameId, gameDescriptionCurrent);
    }


    public void registerMBean() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            ObjectName name = new ObjectName("net.chesstango.uci.arena:type=Arena,name=game1");

            mbs.registerMBean(this, name);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }
}

