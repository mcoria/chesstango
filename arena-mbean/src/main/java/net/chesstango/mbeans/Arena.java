package net.chesstango.mbeans;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mauricio Coria
 */
public class Arena extends NotificationBroadcasterSupport implements ArenaMBean {

    private static AtomicInteger arenaObjectCounter = new AtomicInteger();

    private AtomicLong sequenceNumber = new AtomicLong();

    private volatile GameDescriptionInitial currentGame;

    private Map<String, GameDescriptionInitial> initialMap = Collections.synchronizedMap(new HashMap<>());

    private Map<String, GameDescriptionCurrent> currentMap = Collections.synchronizedMap(new HashMap<>());

    private List<GameDescriptionInitial> gameList = Collections.synchronizedList( new ArrayList<>() );

    private Arena(){};

    @Override
    public GameDescriptionInitial getCurrentGame() {
        return currentGame;
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
        String gameDescription = "A new game has started";
        MBeanNotificationInfo gameInfo = new MBeanNotificationInfo(gameTypes, gameName, gameDescription);

        String[] moveTypes = new String[]{MoveNotification.ATTRIBUTE_CHANGE};
        String moveClassName = MoveNotification.class.getName();
        String moveDescription = "A move has been selected";
        MBeanNotificationInfo moveInfo = new MBeanNotificationInfo(moveTypes, moveClassName, moveDescription);

        return new MBeanNotificationInfo[]{gameInfo, moveInfo};
    }


    public void newGame(GameDescriptionInitial gameDescriptionInitial) {
        gameList.add(gameDescriptionInitial);
        currentGame = gameDescriptionInitial;
        initialMap.put(currentGame.getGameId(), gameDescriptionInitial);
        notifyNewGame(gameDescriptionInitial);
    }

    public void newMove(GameDescriptionCurrent gameDescriptionCurrent) {
        /*
        if(! currentGameId.equals(gameDescriptionCurrent.getGameId()) ){
            throw new RuntimeException("! currentGameId.equals(gameDescriptionCurrent.getGameId()) ");
        }*/

        currentMap.put(gameDescriptionCurrent.getGameId(), gameDescriptionCurrent);
        notifyMove(gameDescriptionCurrent);
    }


    public static Arena createAndRegisterMBean() {
        try {
            Arena arena = new Arena();

            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            ObjectName name = new ObjectName(String.format("net.chesstango.uci.arena:type=Arena,name=game%d", arenaObjectCounter.getAndIncrement()));

            mbs.registerMBean(arena, name);

            return arena;
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

