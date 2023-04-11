package net.chesstango.mbeans;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mauricio Coria
 */
public class Arena extends NotificationBroadcasterSupport implements ArenaMBean {
    private AtomicLong sequenceNumber = new AtomicLong();

    private volatile String currentGameId;

    private Map<String, GameDescriptionInitial> initialMap = Collections.synchronizedMap(new HashMap<>());

    private Map<String, GameDescriptionCurrent> currentMap = Collections.synchronizedMap(new HashMap<>());

    @Override
    public String getCurrentGameId() {
        return currentGameId;
    }

    @Override
    public GameDescriptionInitial getGameDescriptionInitial(String gameId) {
        return initialMap.get(gameId);
    }

    @Override
    public GameDescriptionCurrent getGameDescriptionCurrent(String gameId) {
        return currentMap.get(gameId);
    }


    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] moveTypes = new String[]{MoveNotification.ATTRIBUTE_CHANGE};
        String moveClassName = MoveNotification.class.getName();
        String moveDescription = "A move has been selected";
        MBeanNotificationInfo moveInfo = new MBeanNotificationInfo(moveTypes, moveClassName, moveDescription);

        String[] gameTypes = new String[]{MoveNotification.ATTRIBUTE_CHANGE};
        String gameName = GameNotification.class.getName();
        String gameDescription = "A move has been selected";
        MBeanNotificationInfo gameInfo = new MBeanNotificationInfo(gameTypes, gameName, gameDescription);

        return new MBeanNotificationInfo[]{moveInfo, gameInfo};
    }


    public void newGame(GameDescriptionInitial gameDescriptionInitial) {
        currentGameId = gameDescriptionInitial.getGameId();
        initialMap.put(currentGameId, gameDescriptionInitial);
        notifyGame(gameDescriptionInitial);
    }

    public void updateDescriptionCurrent(GameDescriptionCurrent gameDescriptionCurrent, String moveStr) {
        currentMap.put(gameDescriptionCurrent.getGameId(), gameDescriptionCurrent);
        notifyMove(moveStr, gameDescriptionCurrent);
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


    protected void notifyMove(String move, GameDescriptionCurrent gameDescriptionCurrent) {
        Notification notification =
                new MoveNotification(this,
                        sequenceNumber.getAndIncrement(),
                        move,
                        gameDescriptionCurrent);

        sendNotification(notification);
    }

    private void notifyGame(GameDescriptionInitial gameDescriptionInitial) {
        Notification notification =
                new GameNotification(this,
                        sequenceNumber.getAndIncrement(),
                        gameDescriptionInitial);

        sendNotification(notification);
    }
}

