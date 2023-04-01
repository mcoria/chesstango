package net.chesstango.uci.arena.mbeans;

import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.security.SecureRandom;

/**
 * @author Mauricio Coria
 */
public class Arena extends NotificationBroadcasterSupport implements ArenaMBean {

    private long sequenceNumber = 1;
    protected String fen;
    protected String white;
    protected String black;
    protected String turn;
    protected String[] moveList;


    @Override
    public String getFEN() {
        return fen;
    }

    @Override
    public String getWhite() {
        return white;
    }

    @Override
    public String getBlack() {
        return black;
    }

    @Override
    public String getTurn() {
        return turn;
    }

    @Override
    public String[] getMoveList() {
        return moveList;
    }

    public void registerMBean() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            ObjectName name = new ObjectName("net.chesstango.uci.arena:type=Arena");

            mbs.registerMBean(this, name);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

    /*
    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{AttributeChangeNotification.ATTRIBUTE_CHANGE};
        String name = AttributeChangeNotification.class.getName();
        String description = "An attribute of this MBean has changed";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[]{info};
    }

 */
}

