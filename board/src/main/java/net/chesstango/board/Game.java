package net.chesstango.board;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
public interface Game {
    String getInitialFen();

    void threefoldRepetitionDetection(boolean flag);

    Game executeMove(Move move);

    Game undoMove();

    <V extends GameVisitor> V accept(V gameVisitor);

    GameStateReader getState();

    ChessPositionReader getChessPosition();

    GameStatus getStatus();

    MoveContainerReader getPossibleMoves();

    //******* FOR DEBUGGING
    Move getMove(Square from, Square to);

    Move getMove(Square from, Square to, Piece promotionPiece);

    Game executeMove(Square from, Square to);

    Game executeMove(Square from, Square to, Piece promotionPiece);
    //*******

    <T> T getObject(Class<T> theClass);

    Game mirror();
}
