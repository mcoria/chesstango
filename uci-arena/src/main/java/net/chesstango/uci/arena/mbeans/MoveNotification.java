package net.chesstango.uci.arena.mbeans;


import javax.management.Notification;

/**
 * @author Mauricio Coria
 */
public class MoveNotification extends Notification {

    private static final long serialVersionUID = 1L;

    public static final String ATTRIBUTE_CHANGE = "net.chesstango.move";

    private String move;


    public MoveNotification(Object source, long sequenceNumber, long timeStamp, String msg,
                            String move) {

        super(MoveNotification.ATTRIBUTE_CHANGE, source, sequenceNumber, timeStamp, msg);
        this.move = move;
    }


    public String getMove() {
        return move;
    }
}
