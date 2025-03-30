package net.chesstango.board.moves.imp;

import net.chesstango.board.*;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCommand;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public abstract class MoveImp implements MoveCommand, Command {
    protected final GameImp gameImp;
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Cardinal direction;

    public MoveImp(GameImp gameImp, PiecePositioned from, PiecePositioned to, Cardinal direction) {
        /*
        if (direction != null && !direction.equals(Cardinal.calculateSquaresDirection(from.getSquare(), to.getSquare()))) {
            throw new RuntimeException(String.format("Direccion %s however %s %s %s", direction, Cardinal.calculateSquaresDirection(from.getSquare(), to.getSquare()), from, to));
        }
         */
        this.gameImp = gameImp;
        this.from = from;
        this.to = to;
        this.direction = direction;
        assert (direction == null || direction.equals(Cardinal.calculateSquaresDirection(from.getSquare(), to.getSquare())));
    }

    public MoveImp(GameImp gameImp, PiecePositioned from, PiecePositioned to) {
        this.gameImp = gameImp;
        this.from = from;
        this.to = to;
        this.direction = calculateMoveDirection();
    }

    @Override
    public PiecePositioned getFrom() {
        return from;
    }

    @Override
    public PiecePositioned getTo() {
        return to;
    }

    @Override
    public void executeMove() {
        doMove(gameImp.getChessPosition());

        doMove(gameImp.getState());

        gameImp.notifyDoMove(this);
    }

    @Override
    public void undoMove() {
        undoMove(gameImp.getState());

        undoMove(gameImp.getChessPosition());

        gameImp.notifyUndoMove(this);
    }

    @Override
    public void doMove(GameStateWriter gameState) {
        gameState.setSelectedMove(this);
        gameState.push();
    }

    @Override
    public void undoMove(GameStateWriter gameState) {
        gameState.pop();
    }

    @Override
    public void doMove(ChessPositionWriter chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoardWriter();
        BitBoardWriter bitBoard = chessPosition.getBitBoardWriter();
        PositionStateWriter positionState = chessPosition.getPositionStateWriter();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCacheWriter();
        ZobristHashWriter hash = chessPosition.getZobristWriter();

        doMove(squareBoard);

        doMove(bitBoard);

        doMove(positionState);

        doMove(moveCache);

        doMove(hash);
    }

    @Override
    public void undoMove(ChessPositionWriter chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoardWriter();
        BitBoardWriter bitBoard = chessPosition.getBitBoardWriter();
        PositionStateWriter positionState = chessPosition.getPositionStateWriter();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCacheWriter();
        ZobristHashWriter hash = chessPosition.getZobristWriter();

        undoMove(squareBoard);

        undoMove(bitBoard);

        undoMove(positionState);

        undoMove(moveCache);

        undoMove(hash);
    }

    @Override
    public void undoMove(PositionStateWriter positionState) {
        positionState.popState();
    }

    @Override
    public void doMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare());
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare());
        moveCache.pop();
    }

    @Override
    public void doMove(KingSquareWriter kingSquare) {
    }

    @Override
    public void undoMove(KingSquareWriter kingSquare) {
    }

    @Override
    public void undoMove(ZobristHashWriter hash) {
        hash.popState();
    }

    public boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMove(this, this);
    }

    @Override
    public Cardinal getMoveDirection() {
        return direction;
    }

    @Override
    public boolean isQuiet() {
        return to.getPiece() == null;
    }

    @Override
    public long getZobristHash() {
        ChessPosition chessPosition = gameImp.getChessPosition();
        SquareBoardWriter squareBoard = chessPosition.getSquareBoardWriter();
        PositionStateWriter positionState = chessPosition.getPositionStateWriter();
        ZobristHashWriter hash = chessPosition.getZobristWriter();

        doMove(squareBoard);

        doMove(positionState);

        doMove(hash);

        long zobristHash = chessPosition.getZobristHash();

        undoMove(hash);

        undoMove(positionState);

        undoMove(squareBoard);

        return zobristHash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Move theOther) {
            return from.equals(theOther.getFrom()) && to.equals(theOther.getTo());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", from, to, getClass().getSimpleName());
    }

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
