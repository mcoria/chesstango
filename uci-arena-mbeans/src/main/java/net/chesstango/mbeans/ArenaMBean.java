package net.chesstango.mbeans;

import javax.management.MXBean;

/**
 * @author Mauricio Coria
 */
public interface ArenaMBean {
    String getCurrentGameId();

    GameDescriptionInitial getGameDescriptionInitial(String id);

    GameDescriptionCurrent getGameDescriptionCurrent(String id);
}

