package net.chesstango.board.internal.moves;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.internal.GameImp;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public abstract class MoveImp implements PseudoMove, Command {
    protected final GameImp gameImp;
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Cardinal direction;

    protected PositionStateReader positionStateSnapshot;
    protected ZobristHashReader zobristHashSnapshot;
    protected GameStateReader gameStateSnapshot;

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
        doMove(gameImp.getState());

        doMove(gameImp.getPosition());

        doMove(gameImp.getHistory());

        gameImp.notifyDoMove(this);
    }

    @Override
    public void undoMove() {
        undoMove(gameImp.getHistory());

        undoMove(gameImp.getPosition());

        undoMove(gameImp.getState());

        gameImp.notifyUndoMove(this);
    }

    @Override
    public void doMove(GameState gameState) {
        gameStateSnapshot = gameState.takeSnapshot();
        gameState.reset();
    }

    @Override
    public void undoMove(GameState gameState) {
        gameState.restoreSnapshot(gameStateSnapshot);
    }

    @Override
    public void doMove(Position chessPosition) {
        SquareBoard squareBoard = chessPosition.getSquareBoard();
        BitBoard bitBoard = chessPosition.getBitBoard();
        PositionState positionState = chessPosition.getPositionState();
        MoveCacheBoard moveCache = chessPosition.getMoveCache();
        ZobristHash hash = chessPosition.getZobrist();

        doMove(squareBoard);

        doMove(bitBoard);

        doMove(positionState);

        doMove(moveCache);

        doMove(hash);
    }

    @Override
    public void undoMove(Position chessPosition) {
        SquareBoard squareBoard = chessPosition.getSquareBoard();
        BitBoard bitBoard = chessPosition.getBitBoard();
        PositionState positionState = chessPosition.getPositionState();
        MoveCacheBoard moveCache = chessPosition.getMoveCache();
        ZobristHash hash = chessPosition.getZobrist();

        undoMove(squareBoard);

        undoMove(bitBoard);

        undoMove(positionState);

        undoMove(moveCache);

        undoMove(hash);
    }

    @Override
    public void undoMove(PositionState positionState) {
        positionState.restoreSnapshot(positionStateSnapshot);
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
    public void undoMove(ZobristHash hash) {
        hash.restoreSnapshot(zobristHashSnapshot);
    }

    @Override
    public void doMove(GameHistoryWriter gameHistoryWriter) {
        gameHistoryWriter.push(new GameHistoryRecord(gameStateSnapshot, zobristHashSnapshot, this));
    }

    @Override
    public void undoMove(GameHistoryWriter gameHistoryWriter) {
        gameHistoryWriter.pop();
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
        Position position = gameImp.getPosition();
        SquareBoard squareBoard = position.getSquareBoard();
        PositionState positionState = position.getPositionState();
        ZobristHash hash = position.getZobrist();

        doMove(squareBoard);

        doMove(positionState);

        doMove(hash);

        long zobristHash = position.getZobristHash();

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
