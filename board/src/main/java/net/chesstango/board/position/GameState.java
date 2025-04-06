package net.chesstango.board.position;

import net.chesstango.board.representations.fen.FEN;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public interface GameState extends GameStateReader, GameStateWriter {
    void setInitialFEN(FEN chessRepresentation);

    FEN getInitialFEN();

    Iterator<GameStateReader> stateIterator();

    Iterator<GameStateReader> stateIteratorReverse();

    GameStateReader peekLastState();
}
