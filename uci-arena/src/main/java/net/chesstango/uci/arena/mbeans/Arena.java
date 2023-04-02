package net.chesstango.uci.arena.mbeans;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mauricio Coria
 */
public class Arena extends NotificationBroadcasterSupport implements ArenaMBean {

    private AtomicLong sequenceNumber = new AtomicLong();

    protected GameDescriptionInitial gameDescriptionInitial;
    protected GameDescriptionCurrent gameDescriptionCurrent;

    @Override
    public GameDescriptionInitial getGameDescriptionInitial() {
        return gameDescriptionInitial;
    }

    @Override
    public GameDescriptionCurrent getGameDescriptionCurrent() {
        return gameDescriptionCurrent;
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


    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{MoveNotification.ATTRIBUTE_CHANGE};
        String name = MoveNotification.class.getName();
        String description = "A move has been selected";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);

        return new MBeanNotificationInfo[]{info};
    }


    public void notifyMove(String move) {
        Notification notification =
                new MoveNotification(this,
                        sequenceNumber.getAndIncrement(),
                        System.currentTimeMillis(),
                        "Move",
                        move,
                        gameDescriptionCurrent);

        sendNotification(notification);
    }
}

