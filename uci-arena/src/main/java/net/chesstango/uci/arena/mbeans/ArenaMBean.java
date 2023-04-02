package net.chesstango.uci.arena.mbeans;

/**
 * @author Mauricio Coria
 */
public interface ArenaMBean {

    GameDescriptionInitial getGameDescriptionInitial(int id);

    GameDescriptionCurrent getGameDescriptionCurrent(int id);
}

