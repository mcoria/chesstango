package net.chesstango.uci.arena.mbeans;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface ArenaMBean {
    String getFEN();
    String getWhite();
    String getBlack();
    String getTurn();
    String[] getMoveList();
}

