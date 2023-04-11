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
        String[] gameTypes = new String[]{MoveNotification.ATTRIBUTE_CHANGE};
        String gameName = GameNotification.class.getName();
        String gameDescription = "A move has been selected";
        MBeanNotificationInfo gameInfo = new MBeanNotificationInfo(gameTypes, gameName, gameDescription);

        String[] moveTypes = new String[]{MoveNotification.ATTRIBUTE_CHANGE};
        String moveClassName = MoveNotification.class.getName();
        String moveDescription = "A move has been selected";
        MBeanNotificationInfo moveInfo = new MBeanNotificationInfo(moveTypes, moveClassName, moveDescription);

        return new MBeanNotificationInfo[]{gameInfo, moveInfo};
    }


    public void newGame(GameDescriptionInitial gameDescriptionInitial) {
        currentGameId = gameDescriptionInitial.getGameId();
        initialMap.put(currentGameId, gameDescriptionInitial);
        notifyNewGame(gameDescriptionInitial);
    }

    public void newMove(GameDescriptionCurrent gameDescriptionCurrent) {
        currentMap.put(gameDescriptionCurrent.getGameId(), gameDescriptionCurrent);
        notifyMove(gameDescriptionCurrent);
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


    protected void notifyNewGame(GameDescriptionInitial gameDescriptionInitial) {
        Notification notification =
                new GameNotification(this,
                        sequenceNumber.getAndIncrement(),
                        gameDescriptionInitial);

        sendNotification(notification);
    }

    protected void notifyMove(GameDescriptionCurrent gameDescriptionCurrent) {
        Notification notification =
                new MoveNotification(this,
                        sequenceNumber.getAndIncrement(),
                        gameDescriptionCurrent.getLastMove(),
                        gameDescriptionCurrent);

        sendNotification(notification);
    }

}

