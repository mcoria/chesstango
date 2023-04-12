package net.chesstango.mbeans;

/**
 * @author Mauricio Coria
 */
public interface ArenaMBean {
    String getCurrentGameId();

    GameDescriptionInitial getGameDescriptionInitial(String gameId);

    GameDescriptionCurrent getGameDescriptionCurrent(String gameId);
}

