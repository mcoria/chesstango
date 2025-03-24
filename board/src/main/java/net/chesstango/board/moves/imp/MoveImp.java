package net.chesstango.board.moves.imp;

import net.chesstango.board.GameImp;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public abstract class MoveImp implements MoveCommandImp {
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
        gameImp.executeMove(this);
    }

    @Override
    public void undoMove() {
        gameImp.undoMove(this);
    }

    @Override
    public void doMove(ChessPosition chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        BitBoardWriter bitBoard = chessPosition.getBitBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCache();
        ZobristHashWriter hash = chessPosition.getZobrist();

        doMove(squareBoard);

        doMove(bitBoard);

        doMove(positionState);

        doMove(moveCache);

        doMove(hash, chessPosition);
    }

    @Override
    public void undoMove(ChessPosition chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        BitBoardWriter bitBoard = chessPosition.getBitBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCache();
        ZobristHashWriter hash = chessPosition.getZobrist();

        undoMove(squareBoard);

        undoMove(bitBoard);

        undoMove(positionState);

        undoMove(moveCache);

        undoMove(hash);
    }

    @Override
    public void undoMove(PositionStateWriter positionStateWriter) {
        positionStateWriter.popState();
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
    public void doMove(KingSquareWriter kingSquareWriter) {
    }

    @Override
    public void undoMove(KingSquareWriter kingSquareWriter) {
    }

    @Override
    public void undoMove(ZobristHashWriter hash) {
        hash.popState();
    }

    public boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMove(this);
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
    public long getZobristHash(ChessPosition chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        ZobristHash hash = chessPosition.getZobrist();

        doMove(squareBoard);

        doMove(positionState);

        doMove(hash, chessPosition);

        long zobristHash = hash.getZobristHash();

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
