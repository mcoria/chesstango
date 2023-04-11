package net.chesstango.mbeans;


import javax.management.Notification;

/**
 * @author Mauricio Coria
 */
public class MoveNotification extends Notification {
    private static final long serialVersionUID = 1L;
    public static final String ATTRIBUTE_CHANGE = "net.chesstango.move";
    private final GameDescriptionCurrent gameDescriptionCurrent;

    public MoveNotification(Object source,
                            long sequenceNumber,
                            String move,
                            GameDescriptionCurrent gameDescriptionCurrent) {
        super(MoveNotification.ATTRIBUTE_CHANGE, source, sequenceNumber);
        this.setUserData(move);
        this.gameDescriptionCurrent = gameDescriptionCurrent;
    }

    public GameDescriptionCurrent getGameDescriptionCurrent(){
        return gameDescriptionCurrent;
    }
}
