package net.chesstango.board;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FEN;

/**
 * @author Mauricio Coria
 */
public interface Game extends GameVisitorAcceptor {
    FEN getInitialFEN();

    FEN getCurrentFEN();

    GameStateReader getState();

    ChessPositionReader getChessPosition();

    GameStatus getStatus();

    void fiftyMovesRule(boolean flag);

    void threefoldRepetitionRule(boolean flag);

    MoveContainerReader<Move> getPossibleMoves();

    //******* FOR DEBUGGING
    Move getMove(Square from, Square to);

    Move getMove(Square from, Square to, Piece promotionPiece);

    Game executeMove(Square from, Square to);

    Game executeMove(Square from, Square to, Piece promotionPiece);

    Game undoMove();
    //*******

    Game mirror();
}
