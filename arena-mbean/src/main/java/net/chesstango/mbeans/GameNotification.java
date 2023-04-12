package net.chesstango.mbeans;


import javax.management.Notification;

/**
 * @author Mauricio Coria
 */
public class GameNotification extends Notification {
    private static final long serialVersionUID = 1L;
    public static final String ATTRIBUTE_CHANGE = "net.chesstango.game";

    private final GameDescriptionInitial gameDescriptionInitial;

    public GameNotification(Object source,
                            long sequenceNumber,
                            GameDescriptionInitial gameDescriptionInitial) {
        super(GameNotification.ATTRIBUTE_CHANGE, source, sequenceNumber);
        this.setUserData(gameDescriptionInitial.getGameId());
        this.gameDescriptionInitial = gameDescriptionInitial;
    }

    public GameDescriptionInitial getGameDescriptionInitial() {
        return gameDescriptionInitial;
    }

}
