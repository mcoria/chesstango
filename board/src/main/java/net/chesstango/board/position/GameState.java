package net.chesstango.board.position;

import net.chesstango.board.representations.fen.FEN;

/**
 * @author Mauricio Coria
 */
public interface GameState extends GameStateReader, GameStateWriter {
    void setInitialFEN(FEN chessRepresentation);

    FEN getInitialFEN();

    GameStateReader takeSnapshot();

    void restoreSnapshot(GameStateReader snapshot);
}
