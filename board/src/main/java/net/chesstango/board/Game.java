package net.chesstango.board;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public interface Game extends GameVisitorAcceptor {
    String getInitialFEN();

    Game executeMove(Move move);

    Game undoMove();

    GameStateReader getState();

    ChessPositionReader getChessPosition();

    GameStatus getStatus();

    void fiftyMovesRule(boolean flag);

    void threefoldRepetitionRule(boolean flag);

    MoveContainerReader getPossibleMoves();

    //******* FOR DEBUGGING
    Move getMove(Square from, Square to);

    Move getMove(Square from, Square to, Piece promotionPiece);

    Game executeMove(Square from, Square to);

    Game executeMove(Square from, Square to, Piece promotionPiece);
    //*******

    Game mirror();
}
