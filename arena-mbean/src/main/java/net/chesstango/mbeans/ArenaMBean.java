package net.chesstango.mbeans;

import javax.management.MXBean;

/**
 * @author Mauricio Coria
 */
public interface ArenaMBean {
    String getCurrentGameId();

    GameDescriptionInitial getGameDescriptionInitial(String gameId);

    GameDescriptionCurrent getGameDescriptionCurrent(String gameId);
}

