package net.chesstango.board.position;

import net.chesstango.gardel.fen.FEN;

/**
 * @author Mauricio Coria
 */
public interface GameState extends GameStateReader, GameStateWriter {
    void setInitialFEN(FEN chessRepresentation);

    FEN getInitialFEN();

    GameStateReader takeSnapshot();

    void restoreSnapshot(GameStateReader snapshot);
}
